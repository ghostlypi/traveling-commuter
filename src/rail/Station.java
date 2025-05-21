package rail;

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

    public Journey next_train(Station s, int time) {
        ArrayList<Journey> schedule = timetable.get(s);
        Journey next = null;
        for (Journey j : schedule) {
            if (j.departure >= time && (next == null || j.departure < next.departure)) {
                next = j;
            }
        }
        if (next == null) {
            try {
                throw new RuntimeException("Unable to find a train from " + this + " to " + s + " at time " + time + "!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return next;
    }

    public String toString() {
        return name;
    }
}
