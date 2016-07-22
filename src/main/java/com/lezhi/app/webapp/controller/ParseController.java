package com.lezhi.app.webapp.controller;

import com.lezhi.app.model.AddressListInfo;
import com.lezhi.app.model.ProcessInfo;
import com.lezhi.app.util.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Colin Yan on 2016/7/15.
 */
@Controller
@RequestMapping("/")
public class ParseController {

    private static final String STORE_PATH = "D:" + File.separator + "www_upload";


    private Map<String, AddressListInfo> addressListInfoMap = new HashMap<>();
    private Map<String, ProcessInfo> processMap = new HashMap<>();

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @RequestMapping("parse")
    public String parse(MultipartHttpServletRequest request, HttpServletResponse response, @RequestParam("inputtype") Integer addressType) throws IOException {
        int size = 0;
        final String id = uuid();
        final List<String> lines;
        if (addressType == 2) {
            MultipartFile file = request.getFile("addressFile");
            if (file.getSize() == 0)
                throw new IllegalArgumentException("请提交非空文件");
            File dest = new File(STORE_PATH, id + ".address");
            file.transferTo(dest);
            lines = FileUtils.readLines(dest, "utf-8");
        } else {
            String addressList = request.getParameter("addressList");
            lines = new ArrayList<>();
            Collections.addAll(lines, addressList.split("\\r?\\n"));
        }
        size = lines.size();

        addressListInfoMap.put(id, new AddressListInfo(size));
        processMap.put(id, new ProcessInfo(size));

        execute(new MyCallable(id, lines));
        return "redirect:output.do?id=" + id;
    }

    private void execute(Callable callable) {
        new Thread() {
            @Override
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    static byte[] IO_SE = new byte[] { '\r', '\n' };
    private class MyCallable implements Callable<String> {

        private final String id;
        private final ProcessInfo processInfo;

        private final List<String> lines;

        MyCallable(String id, List<String> lines) {
            this.id = id;
            this.lines = lines;
            processInfo = processMap.get(id);
        }

        @Override
        public String call() throws Exception {
            File folder = new File(STORE_PATH + File.separator + id);
            if (!folder.exists() && !folder.mkdirs()) {
                return null;
            }

            File file1 = new File(folder, "标准格式1(XXX路XXX弄XXX号XXX室).txt");
            File file2 = new File(folder, "标准格式2(XXX(小区)XXX号XXX室).txt");
            File file3 = new File(folder, "标准格式3(XXX(村)XXX(组、队)XXX号).txt");
            File file4 = new File(folder, "无法解析的结果.txt");

            FileOutputStream fos1 = new FileOutputStream(file1);
            FileOutputStream fos2 = new FileOutputStream(file2);
            FileOutputStream fos3 = new FileOutputStream(file3);
            FileOutputStream fos4 = new FileOutputStream(file4);

            int successCount = 0;

            for (int i = 0; i < lines.size(); i++) {
                String oriInput = lines.get(i);
                String input = PreHandle.handle(oriInput);
                AddressModel addressModel = AddressExtractor.parseAll(input);
                if (addressModel == null) {
                    fos4.write(oriInput.getBytes("utf-8"));
                    fos4.write(IO_SE);
                    continue;
                }
                successCount++;
                FileOutputStream fos = null;
                if (addressModel instanceof Address1) {
                    fos = fos1;
                } else if (addressModel instanceof Address2) {
                    fos = fos2;
                } else if (addressModel instanceof Address3) {
                    fos = fos3;
                } else {
                    throw new RuntimeException("unknown");
                }
                fos.write(oriInput.getBytes("utf-8"));
                fos.write('\t');
                fos.write(addressModel.toString().getBytes("utf-8"));
                fos.write(IO_SE);
                processInfo.setCur(i + 1);
                processInfo.setSuccessCount(successCount);
            }
            processInfo.setFinish(true);

            fos1.close();
            fos2.close();
            fos3.close();
            fos4.close();
            if (file1.length() == 0) {
                file1.delete();
            }
            if (file2.length() == 0) {
                file2.delete();
            }
            if (file3.length() == 0) {
                file3.delete();
            }
            if (file4.length() == 0) {
                file4.delete();
            }

            ZipUtil.compressFolder(folder, new File(STORE_PATH, id + ".zip"));
            FileUtils.deleteDirectory(folder);
            return id;
        }
    }


    @RequestMapping("downloadResult")
    public ResponseEntity<byte[]> downloadResult(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) throws IOException {

        File file = new File(STORE_PATH, id + ".zip");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = FileUtils.readFileToByteArray(file);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping("output")
    public String output(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) throws IOException {

        AddressListInfo addressListInfo = addressListInfoMap.get(id);
        ProcessInfo processInfo = processMap.get(id);

        request.setAttribute("count", addressListInfo.getCount());
        request.setAttribute("time", processInfo.getTimeRemain());
        request.setAttribute("cur", processInfo.getCur());
        request.setAttribute("finish", processInfo.isFinish());
        request.setAttribute("id", id);
        return "waitDownload";
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
