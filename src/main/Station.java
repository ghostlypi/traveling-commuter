package main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Station {
    public String name;
    public HashMap<Station, ArrayList<Journey>> timetable;

    public Station() {
        new Station("Unnamed");
    }

    public Station(String name) {
        this.name = name;
        timetable = new HashMap<>();
    }

    public void addEntry(Station s, Service service, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime st = LocalTime.parse(startTime, formatter);
        LocalTime et = LocalTime.parse(endTime, formatter);
        ArrayList<Journey> schedule = timetable.get(s);
        if (schedule == null) {
            schedule = new ArrayList<>();
            timetable.put(s, schedule);
        }
        schedule.add(new Journey(this, s, st, et, service));
    }

    public String toString() {
        return name;
    }
}
