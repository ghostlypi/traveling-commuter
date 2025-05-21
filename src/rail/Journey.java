package rail;

public class Journey {
    public Station start;
    public Station end;
    public int departure;
    public int arrival;
    public Service service;

    public Journey(Station start, Station end, int departure, int arrival, Service service) {
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
