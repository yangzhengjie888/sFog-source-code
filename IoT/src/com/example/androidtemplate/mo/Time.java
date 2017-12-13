package com.example.androidtemplate.mo;

import java.util.ArrayList;

/**
 *
 */
public class Time {
    private long count;
    private long uploadStart;
    private long uploadEnd;
    private long downloadStart;
    private long downloadEnd;

    //private String startTime;
   // private String endTime;



    public Time() {

    }


//    public Time(long count, long uploadTime, String startTime) {
//        this.count = count;
//        this.uploadTime = uploadTime;
//        this.startTime = startTime;
//    }

    //ArrayList<> timelist;
    @Override
    public String toString() {
        return "Time{" +
                "count=" + count +
                ", uploadStart=" + uploadStart +
                ", uploadEnd=" + uploadEnd +
                ", downloadStart=" + downloadStart +
                ", downloadEnd='" + downloadEnd + '\'' +

                '}';
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getUploadStart() {
        return uploadStart;
    }

    public void setUploadStart(long uploadStart) {
        this.uploadStart = uploadStart;
    }

    public long getUploadEnd() {
        return uploadEnd;
    }

    public void setUploadEnd(long uploadEnd) {
        this.uploadEnd = uploadEnd;
    }

//    public long getOperaTime() {
//        return operaTime;
//    }

//    public void setOperaTime(long operaTime) {
//        this.operaTime = operaTime;
//    }

    public long getDownloadStart() {
        return downloadStart;
    }

    public void setDownloadStart(long downloadStart) {
        this.downloadStart = downloadStart;
    }

    public long getDownloadEnd() {
        return downloadEnd;
    }

    public void setDownloadEnd(long downloadEnd) {
        this.downloadEnd = downloadEnd;
    }


//    public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }
}
