package main;

import main.Station;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {

    public static void ingest(Service service, String f, HashMap<String, Station> stations) {
        try {
            String file = Files.readString(Paths.get(f));
            String[] lines = file.split("\n");
            for (int i = 0; i < lines.length-1; i++) {
                String line = lines[i];
                String linepp = lines[i+1];
                String s = line.substring(0, line.indexOf(','));
                String sn = linepp.substring(0, linepp.indexOf(','));
                Station curr = stations.get(s) == null ? new Station(s) : stations.get(s);
                Station next = stations.get(sn) == null ? new Station(sn) : stations.get(sn);
                stations.put(s, curr);
                stations.put(sn, next);
                String[] startTimes = line.split(",", -1);
                String[] endTimes = linepp.split(",", -1);
                for (int j = 1; j < startTimes.length; j++) {
                    String startTime = startTimes[j];
                    String endTime = endTimes[j];
                    if (!startTime.isEmpty() && !endTime.isEmpty() && !curr.name.equals(next.name)) {
                        curr.addEntry(next, service, startTime, endTime);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HashMap<String, Station> stations = new HashMap<>();

        for (Station s : stations.values())
            for (Station next : s.timetable.keySet())
                for (Journey t : s.timetable.get(next))
                    System.out.println(t);
    }
}