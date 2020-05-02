package com.MGureev;

import java.io.StreamTokenizer;

public class AccessWindow {

    private int minuteStart;
    private int minuteFinish;

    public AccessWindow(int hStart, int mStart, int hFinish, int mFinish) {
        minuteStart = hStart*60+mStart;
        minuteFinish = hFinish*60+mFinish;
    }

    public AccessWindow(int minuteStart, int minuteFinish) {
        this.minuteStart = minuteStart;
        this.minuteFinish = minuteFinish;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public int getMinuteFinish() {
        return minuteFinish;
    }

    public String getStandardStartTime(){
        String time = "";
        int h = minuteStart/60;
        int m = minuteStart-h*60;
        //time = String.format("Start time %d:%d ",h,m);
        if(Math.ceil(Math.log10(m+0.5))==2){
            time = String.format("%d:%d",h,m);
        }
        else{
            time = String.format("%d:0%d",h,m);
        }
        return time;
    }

    public String getStandardFinishTime(){
        String time;
        int h = minuteFinish/60;
        int m = minuteFinish-h*60;
        //time = String.format("Finish time %d:%d ",h,m);
        if(Math.ceil(Math.log10(m+0.5))==2){
            time = String.format("%d:%d",h,m);
        }
        else{
            time = String.format("%d:0%d",h,m);
        }
        return time;
    }

    public static String timeConvert(int min){
        String time;
        int h = min/60;
        int m = min-h*60;
        //time = String.format("Time %d:%d ",h,m);
        if(Math.ceil(Math.log10(m+0.5))==2){
            time = String.format("%d:%d",h,m);
        }
        else{
            time = String.format("%d:0%d",h,m);
        }
        return time;
    }

}
