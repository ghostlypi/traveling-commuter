package main;

import javafx.util.Pair;
import rail.Journey;
import rail.Station;
import util.ParseCSV;
import util.Quadruple;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static HashMap<String, Station> stations = new HashMap<>();
    public static Quadruple out = new Quadruple(Integer.MAX_VALUE, new HashSet<>(), null, new ArrayList<>());

    public static void init() {
        ParseCSV.parse_nameMap(Paths.get("resources/names.map"));
        stations = ParseCSV.parse_stations();
        System.out.println(stations.size());
    }

    public static ArrayList<Journey> djikstra(Station head, int time, HashSet<Station> visited) {
        HashMap<Station, Integer> dist = new HashMap<Station, Integer>();
        HashMap<Station, Journey> prev = new HashMap<Station, Journey>();

        Comparator<Pair<Integer, Station>> pairComparator = Comparator.comparingInt(Pair::getKey);
        PriorityQueue<Pair<Integer, Station>> pq = new PriorityQueue<>(pairComparator);

        for (String s : stations.keySet()) {
            if (s.equals(head)) {
                dist.put(head, 0);
            } else {
                dist.put(stations.get(s), Integer.MAX_VALUE);
            }
            prev.put(stations.get(s), null);
            pq.add(new Pair<>(dist.get(stations.get(s)), stations.get(s)));
        }

        while (!pq.isEmpty()) {
            Station u = pq.remove().getValue();

            for (Station v : u.timetable.keySet()) {
                Journey next_train = u.next_train(v, time + dist.get(u));
                int alt = dist.get(u) + next_train.arrival - next_train.departure;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, next_train);
                }
                if (!visited.contains(v)) {
                    ArrayList<Journey> route = new ArrayList<>();
                    Station cursor = v;
                    while (cursor != null) {
                        if (prev.get(cursor) != null) {
                            route.add(prev.get(cursor));
                            cursor = prev.get(cursor).start;
                        } else {
                            cursor = null;
                        }
                    }
                    return route;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        init();
        Station run_start = stations.get("gilroy");
        ConcurrentLinkedQueue<Quadruple> work_queue = new ConcurrentLinkedQueue<>();
        work_queue.add(new Quadruple(0, new HashSet<>(Arrays.asList(new Station[]{run_start})), run_start, new ArrayList<>()));

        ArrayList<Worker> workers = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            workers.add(new Worker(work_queue));
            workers.get(i).start();
        }

        // Using a standalone main thread for efficient monitoring + everything syncs to main
        while (!work_queue.isEmpty()) {
            System.out.print("\u001b[2K" + "Queue Size: " + work_queue.size() + "\r");
        }

        System.out.println(out.time);
        System.out.println(out.visited.size());
        System.out.println(out.path);

    }
}