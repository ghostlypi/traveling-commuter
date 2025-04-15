package main;

import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class Main {

    public static ArrayList<Pair<Integer, Station>> compute_time_to_dest(Station s, Station d, int time) {
        // Priority queue for Dijkstra's algorithm, ordered by arrival time
        PriorityQueue<Pair<Integer, Station>> pq = new PriorityQueue<>(
                Comparator.comparingInt(p -> p.getKey()));

        // Map to store best arrival time for each station
        HashMap<Station, Integer> bestTimes = new HashMap<>();

        // Map to store the previous step for path reconstruction
        HashMap<Station, Pair<Integer, Journey>> previous = new HashMap<>();



    }

    public static void main(String[] args) {
        int best_time = 86400;
        for (int clk = 0; clk < 1000000; clk++) {
            File urandomFile = new File("/dev/urandom");
            byte[] randomBytes = new byte[8];
            try (FileInputStream fis = new FileInputStream(urandomFile)) {
                int bytesRead = fis.read(randomBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long seed = ByteBuffer.wrap(randomBytes).getLong();
            Random r = new Random(seed);
            HashMap<String, Station> stations = ParseCSV.parse_stations();
            Station s = stations.get("gilroy");
            int time = ((ArrayList<Journey>) s.timetable.values().toArray()[0]).get(0).departure;
            HashSet<Station> bucket = new HashSet(stations.values());
            ArrayList<Pair<Integer, Station>> path = new ArrayList<>();
            for (int i = 0; i < stations.size() * 2; i++) {
                path.add(new Pair<Integer, Station>(time, s));
                bucket.remove(s);
                if (bucket.size() == 0)
                    break;
                Station next = (Station) bucket.toArray()[r.nextInt(0, Integer.MAX_VALUE) % bucket.size()];
                if (s.timetable.values().size() > 0) {
                    int n1 = r.nextInt(0, Integer.MAX_VALUE) % s.timetable.values().size();
                    ArrayList<Journey> arr = ((ArrayList<Journey>) (s.timetable.values().toArray()[n1]));
                    for (Journey j : arr) {
                        if (j.departure >= time && bucket.contains(j.end)) {
                            time = j.arrival;
                            next = j.end;
                            break;
                        }
                    }
                } else {
                    ArrayList<Pair<Integer, Station>> subpath = compute_time_to_dest(s, next, time);
                    if (subpath.size() > 0) {
                        for (Pair p : subpath)
                            path.add(p);
                        time = subpath.getLast().getKey();
                    } else {
                    }

                }
                s = next;
            }
            int sec = path.getLast().getKey() % 60;
            int min = (path.getLast().getKey() / 60) % 60;
            int hrs = (path.getLast().getKey() / 3600);
            if (path.getLast().getKey() < best_time) {
                best_time = path.getLast().getKey();
                System.out.println(hrs + ":" + min + ":" + sec);
                System.out.println("Seed: " + seed);
                System.out.println(path.size());
                System.out.println(path);
            }
        }
    }
}