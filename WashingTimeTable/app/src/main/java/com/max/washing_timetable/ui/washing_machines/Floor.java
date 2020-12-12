package com.max.washing_timetable.ui.washing_machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Floor {
    private Map<String, WashingMachine> washingMachines;

    public Floor() {

    }

    public Floor(Map<String, WashingMachine> washingMachines) {
        this.washingMachines = washingMachines;
    }

    public Map<String, WashingMachine> getWashingMachines() {
        return washingMachines;
    }

    public void setWashingMachines(Map<String, WashingMachine> washingMachines) {
        this.washingMachines = washingMachines;
    }
}
