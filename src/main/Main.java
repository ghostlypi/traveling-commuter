package main;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static HashMap<String, Station> stations = new HashMap<>();

    public static void init() {
        ParseCSV.parse_nameMap(Paths.get("resources/names.map"));
        stations = ParseCSV.parse_stations();
        System.out.println(stations.size());
    }

    public static void main(String[] args) {
        init();
    }
}