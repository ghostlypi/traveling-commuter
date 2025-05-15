package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Station> stations = ParseCSV.parse_stations();
        System.out.println(stations.keySet().size());

    }
}