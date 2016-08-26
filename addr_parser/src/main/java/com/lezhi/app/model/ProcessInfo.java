package com.lezhi.app.model;

/**
 * Created by Colin Yan on 2016/7/19.
 */
public class ProcessInfo {


    private static final int COUNTS_PS = 10000;

    private int count;

    private int cur;

    private boolean finish;

    private String outputFile;
    private int successCount;

    public ProcessInfo(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCur() {
        return cur;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getTimeRemain() {
       return String.format("(%.2f", (count - cur) / 1.0 / COUNTS_PS);
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getSuccessCount() {
        return successCount;
    }
}
