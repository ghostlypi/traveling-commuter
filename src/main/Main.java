package main;

import rail.Journey;
import rail.Station;
import util.ParseCSV;
import util.Quadruple;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static HashMap<String, Station> stations = new HashMap<>();

    public static void init() {
        ParseCSV.parse_nameMap(Paths.get("resources/names.map"));
        stations = ParseCSV.parse_stations();
        System.out.println("Total Stations: " + stations.size());
    }

    public static String int2Time(int time) {
        int s = time % 60;
        int m = (time / 60) % 60;
        int h = (time / 3600);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static ArrayList<Journey> djikstra(Station head, int time, HashSet<Station> visited) {
        if (head == null) {
            return null;
        }
        HashMap<Station, Integer> dist = new HashMap<>();
        HashMap<Station, Journey> prev = new HashMap<>();

        HashSet<Station> pq = new HashSet<>();

        for (String s : stations.keySet()) {
            dist.put(stations.get(s), Integer.MAX_VALUE);
            prev.put(stations.get(s), null);
            pq.add(stations.get(s));
        }
        dist.put(head, 0);

        while (!pq.isEmpty()) {
            Station u = null;
            int min = Integer.MAX_VALUE;
            for (Station v : pq) {
                if (dist.get(v) < min) {
                    min = dist.get(v);
                    u = v;
                }
            }

            if (u == null) {
                break;
            }

            pq.remove(u);
            for (Station v : u.timetable.keySet()) {
                Journey next_train = u.next_train(v, time + dist.get(u));
                int alt = Integer.MAX_VALUE;
                if (next_train != null)
                    alt = dist.get(u) + next_train.arrival - next_train.departure;
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

        Station start = stations.get("gilroy");

        HashSet<Station> visited = new HashSet<>();
        visited.add(start);

        Quadruple out = new Quadruple(0, new HashSet<>(), start, new ArrayList<>());

        LinkedBlockingQueue<Quadruple> queue = new LinkedBlockingQueue<>();
        queue.add(new Quadruple(0, visited, start, new ArrayList<>()));

        while (visited.size() < stations.size()) {
            //Run neighborhood search to cover the maximum number of vertices without any backtracking
            while (!queue.isEmpty()) {
                Quadruple packet = queue.poll();
                int time = packet.time;
                visited = packet.visited;
                Station cursor = packet.cursor;
                ArrayList<Journey> path = packet.path;

                for (Station s : cursor.timetable.keySet()) {
                    if (!visited.contains(s)) {
                        HashSet<Station> new_visited = (HashSet<Station>) visited.clone();
                        ArrayList<Journey> new_path = (ArrayList<Journey>) path.clone();
                        new_visited.add(s);
                        Journey jnextrain = cursor.next_train(s, time);
                        if (jnextrain != null) {
                            new_path.add(jnextrain);
                            queue.add(new Quadruple(new_path.getLast().arrival, new_visited, s, new_path));
                        }
                    } else {
                        if ((out.visited.size() < visited.size()) ||
                                (out.visited.size() == visited.size() && out.time > time)) {
                            out = packet;
                        }
                    }
                }
            }

            //Run Djikstra to find nearest unvisited node
            ArrayList<Journey> route = djikstra(out.cursor, out.time, out.visited);
            if (route == null) {
                //No more unvisited vertices exist
                break;
            }
            for (Journey j : route) {
                out.visited.add(j.start);
                out.visited.add(j.end);
                out.path.add(j);
            }
            out.time = out.path.getLast().arrival;
            out.cursor = out.path.getLast().end;
            queue.add(out);
            System.out.print("\u001b[2KStart Time: " + int2Time(out.path.getFirst().departure) +
                             "; End Time: " + int2Time(out.time) +
                             "; #stations: " + out.visited.size() + "\r");
        }

        System.out.println("");
        System.out.println(out.path);
        System.out.print("[");
        for (Station s : stations.values()) {
            if (!out.visited.contains(s))
                System.out.print(s + ",");
        }
        System.out.println("\b]");
    }
}