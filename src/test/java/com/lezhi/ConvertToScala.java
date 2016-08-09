package com.lezhi;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Colin Yan on 2016/7/13.
 */
@Deprecated
public class ConvertToScala {

    public static void main(String[] args) throws IOException {

        List<String> lines = FileUtils.readLines(new File("D:\\projects\\addr_parser\\src\\main\\java\\com\\lezhi\\app\\util\\AddressExtractor.java"), "UTF-8");

        Map<String, String> toConst = new HashMap<>();
        Map<String, Integer> toIndex = new HashMap<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Pattern p = Pattern.compile("\\} else if \\(matchWithPattern\\(str, (p\\d+)\\)\\) \\{");
            Matcher m = p.matcher(line);
            if (m.find()) {
                String nextLine = lines.get(i + 1);
                Pattern p2 = Pattern.compile("address = \"(.+)\";");
                Matcher m2 = p2.matcher(nextLine);
                if (m2.find() && !nextLine.contains("extractValueFromStr")) {
                    toConst.put(m.group(1), m2.group(1));
                } else {
                    Pattern p3 = Pattern.compile("address = extractValueFromStr\\(str, (p\\d+), (\\d+)\\);");
                    Matcher m3 = p3.matcher(nextLine);
                    if (m3.find()) {
                        toIndex.put(m3.group(1), Integer.parseInt(m3.group(2)));
                    }
                }
            }

        }

        List<String> scalaLines = FileUtils.readLines(new File("D:\\projects\\addr_parser\\src\\main\\java\\com\\lezhi\\app\\util\\P.scala"), "UTF-8");

        List<String> scalaOutput = new ArrayList<>();

        for (int i = 0; i < scalaLines.size(); i++) {
            boolean ac = false;
            String line = scalaLines.get(i);
            Pattern p = Pattern.compile("val (p\\d+) = (\"\"\".+\"\"\".r)");
            Matcher m = p.matcher(line);
            if (m.find()) {
                String a = m.group(1);
                String b = m.group(2);
                if (toConst.containsKey(a)) {
                    scalaOutput.add("val " + a + " = " + "Expr(" + b + ",\"" + toConst.get(a) + "\")");
                    ac = true;
                } else if (toIndex.containsKey(a)) {
                    scalaOutput.add("val " + a + " = " + "Expr(" + b + "," + toIndex.get(a) + ")");
                    ac = true;
                }
            }
            if (!ac) {
                scalaOutput.add(line);
            }
        }

        for (String s : scalaOutput) {
            System.out.println(s);
        }

    }
}
