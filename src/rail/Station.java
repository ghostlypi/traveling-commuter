package rail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class Station {
    public String name;
    public Hashtable<Station, ArrayList<Journey>> timetable;
    public HashSet<Service> lines;

    public Station() {
        new Station("Unnamed");
    }

    public Station(String name) {
        this.name = name;
        timetable = new Hashtable<>();
        lines = new HashSet<>();
    }

    public void addEntry(Station s, Service service, String startTime, String endTime) {
        ArrayList<Journey> schedule = timetable.getOrDefault(s, new ArrayList<>());
        int st = startTime.equals("null") ? -1 : Integer.parseInt(startTime);
        int et = endTime.equals("null") ? -1 : Integer.parseInt(endTime);
        if (st != -1 && et != -1 && !service.equals(Service.ERROR)) {
            schedule.add(new Journey(this, s, st, et, service));
            lines.add(service);
        }
        timetable.put(s, schedule);
    }

    public Journey next_train(Station s, int time) {
        ArrayList<Journey> schedule = timetable.get(s);
        Journey next = null;
        for (Journey j : schedule) {
            if (j.departure >= time && (next == null || j.departure < next.departure)) {
                next = j;
            }
        }
        if (next == null) {
//            System.err.println("Unable to find a train from " + this + " to " + s + " after time " + time + "!");
        }
        return next;
    }

    public Journey prev_train(Station s, int time) {
        ArrayList<Journey> schedule = timetable.get(s);
        Journey prev = null;
        for (Journey j : schedule) {
            if (j.arrival <= time && (prev == null || j.arrival > prev.arrival)) {
                prev = j;
            }
        }
        if (prev == null) {
            try {
                throw new RuntimeException("Unable to find a train from " + this + " to " + s + " before time " + time + "!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return prev;
    }

    public String toString() {
        return name;
    }
}
