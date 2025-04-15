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
        ArrayList<Journey> schedule = timetable.get(s);
        int st = startTime.equals("null") ? -1 : Integer.parseInt(startTime);
        int et = endTime.equals("null") ? -1 : Integer.parseInt(endTime);
        if (schedule == null) {
            schedule = new ArrayList<>();
            timetable.put(s, schedule);
        }
        if (st != -1 && et != -1 && !s.equals(Service.ERROR))
            schedule.add(new Journey(this, s, st, et, service));
    }

    public String toString() {
        return name;
    }
}
