package main;

import rail.Journey;
import rail.Station;
import util.Quadruple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import static main.Main.djikstra;
import static main.Main.stations;
import static main.Main.out;

public class Worker extends Thread {

    private ConcurrentLinkedQueue<Quadruple> work_queue;

    public Worker(ConcurrentLinkedQueue work_queue) {
        this.work_queue = work_queue;
    }

    public void run() {
        while (!work_queue.isEmpty()) {
            Quadruple packet = work_queue.poll();
            if (packet == null) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            int time = packet.time;
            HashSet<Station> visited = packet.visited;
            Station cursor = packet.cursor;
            ArrayList<Journey> path = packet.path;
            if (out.visited.size() > visited.size() && out.time > time) {
                continue;
            }
            boolean updated = false;
            for (Station next : cursor.timetable.keySet()) {
                if (!visited.contains(next)) {
                    HashSet<Station> new_visited = (HashSet<Station>) visited.clone();
                    new_visited.add(cursor);
                    ArrayList<Journey> new_path = (ArrayList<Journey>) path.clone();
                    Journey leg = cursor.next_train(next, time);
                    updated = true;
                    if (leg != null) {
                        new_path.add(leg);
                        work_queue.add(new Quadruple(new_path.getLast().arrival, new_visited, next, new_path));
                    }
                }
            }
            if (!updated) {
                if (visited.size() == stations.size() && out.time > path.getLast().arrival) {
                    synchronized (out) {
                        out = new Quadruple(path.getLast().arrival, visited, cursor, path);
                        System.out.println(out.time);
                        System.out.println(out.visited.size());
                        System.out.println(out.path);
                    }
                } else {
                    ArrayList<Journey> jump_path = djikstra(cursor, path.getLast().arrival, visited);
                    ArrayList<Journey> new_path = (ArrayList<Journey>) path.clone();
                    new_path.addAll(jump_path);
                    HashSet<Station> new_visited = (HashSet<Station>) visited.clone();
                    new_visited.add(new_path.getLast().end);
                    work_queue.add(new Quadruple(new_path.getLast().arrival, visited, new_path.getLast().end, new_path));
                    if (new_visited.size() > out.path.size()) {
                        synchronized (out) {
                            out = new Quadruple(new_path.getLast().arrival, new_visited, new_path.getLast().end, new_path);
                            System.out.println("");
                            System.out.println(out.time);
                            System.out.println(out.visited.size());
                            System.out.println(out.path);
                        }
                    }
                }
            }
        }
    }
}
