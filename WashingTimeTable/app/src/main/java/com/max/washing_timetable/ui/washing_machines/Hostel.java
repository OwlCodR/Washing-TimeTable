package com.max.washing_timetable.ui.washing_machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hostel {
    private String address;
    private Map<String, Floor> floors;

    public Hostel() {

    }

    public Hostel(String address, Map<String, Floor> floors) {
        this.address = address;
        this.floors = floors;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Floor> getFloors() {
        return floors;
    }

    public void setFloors(Map<String, Floor> floors) {
        this.floors = floors;
    }
}
