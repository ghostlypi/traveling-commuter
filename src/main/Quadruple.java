package main;

public class Quadruple {
    public int time;
    public java.util.HashSet<Station> visited;
    public Station cursor;
    public java.util.ArrayList<Journey> path;

    public Quadruple(int time, java.util.HashSet<Station> visited, Station cursor, java.util.ArrayList<Journey> path) {
        this.time = time;
        this.visited = visited;
        this.cursor = cursor;
        this.path = path;
    }
}
