package main;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParseCSV {

    public static HashMap<String, String> nameMap = new HashMap<>();

    public static void parse_stations(Path fname, ArrayList<String> lines, HashMap<String, Station> stations) {
        String[] header = lines.get(0).split("/");
        Service s = Service.parse_service(fname.getFileName().toString().split("\\.")[0].toUpperCase(),
                                          header[0].toUpperCase());


        for (int i = 1; i < lines.size()-1; i++) {
            String cur[] = lines.get(i).split(",");
            String nxt[] = lines.get(i+1).split(",");
            if (nxt.length != cur.length) {
                System.out.println("CSV Length Mismatch!");
            }

            Station next = stations.getOrDefault(nxt[0], new Station(nxt[0]));
            Station curr = stations.getOrDefault(cur[0], new Station(cur[0]));

            for (int j = 2; j < cur.length; j++) {
                curr.addEntry(next, s, cur[j], nxt[j]);
            }
            if (!s.equals(Service.ERROR)) {
                stations.put(nxt[0], next);
                stations.put(cur[0], curr);
            }
        }
    }

    public static HashMap<String, Station> parse_stations() {
        Path dir = Paths.get("resources/lines");
        ArrayList<Pair<Path, String>> fileContents = new ArrayList<>();
        try (Stream<Path> stream = Files.list(dir)) {
            fileContents = stream
                    .filter(Files::isRegularFile) // Keep only regular files
                    .map(filePath -> {
                        try {
                            return new Pair<Path, String>(filePath, Files.readString(filePath, StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(content -> content != null) // Remove nulls if errors occurred and were handled by returning null
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, Station> stations = new HashMap<>();
        for (Pair<Path, String> file : fileContents) {
            ArrayList<String> lines = new ArrayList(Arrays.asList(file.getValue().split("\n")));
            while (lines.size() > 0) {
                int i = 1;
                while (i < lines.size() && lines.get(i).contains(",")) {
                    i++;
                }
                ArrayList<String> timetable = new ArrayList(lines.subList(0,i));
                lines.subList(0,i).clear();
                parse_stations(file.getKey(), timetable, stations);
            }
        }
        return stations;
    }

}
