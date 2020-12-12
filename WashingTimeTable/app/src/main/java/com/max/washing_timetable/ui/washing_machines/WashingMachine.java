package com.max.washing_timetable.ui.washing_machines;

public class WashingMachine {
    private String startTime;
    private String endTime;
    // @TODO Users list

    public WashingMachine() {

    }

    public WashingMachine(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
