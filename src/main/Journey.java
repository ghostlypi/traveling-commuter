package main;

import java.time.LocalTime;

public class Journey {
    public Station start;
    public Station end;
    public LocalTime departure;
    public LocalTime arrival;
    public Service service;

    public Journey(Station start, Station end, LocalTime departure, LocalTime arrival, Service service) {
        this.start = start;
        this.end = end;
        this.departure = departure;
        this.arrival = arrival;
        this.service = service;
    }

    public String toString() {
        return "(" + start + ", " + end + ", " + departure + ", " + arrival + ", " + service + ")";
    }
}
