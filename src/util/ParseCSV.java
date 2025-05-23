package util;

import javafx.util.Pair;
import rail.Service;
import rail.Station;

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

    public static void parse_nameMap(Path fname) {
        try {
            String file = Files.readString(fname);
            ArrayList<String> lines = new ArrayList(Arrays.asList(file.split("\n")));
            for (String line : lines) {
                String[] splitLine = line.split(":");
                if ( splitLine.length == 2) {
                    nameMap.put(splitLine[0], splitLine[1]);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void parse_stations(Path fname, ArrayList<String> lines, HashMap<String, Station> stations) {
        String[] header = lines.getFirst().split("/");
        Service s = Service.parse_service(fname.getFileName().toString().split("\\.")[0].toUpperCase(),
                                          header[0].toUpperCase());

        if (s.equals(Service.ERROR)) {
            return;
        }

        for (int i = 1; i < lines.size()-1; i++) {
            String cur[] = lines.get(i).split(",");
            String nxt[] = lines.get(i+1).split(",");
            if (nxt.length != cur.length) {
                System.err.println("CSV Length Mismatch!");
                System.out.println(cur[0] + " " + nxt.length + " " + cur.length);
            }

            String nxxt = nameMap.getOrDefault(nxt[0], nxt[0]);
            String cuur = nameMap.getOrDefault(cur[0], cur[0]);

            Station next = stations.getOrDefault(nxxt, new Station(nxxt));
            Station curr = stations.getOrDefault(cuur, new Station(cuur));

            for (int j = 2; j < cur.length; j++) {
                curr.addEntry(next, s, cur[j], nxt[j]);
            }
            if (!s.equals(Service.ERROR)) {
                stations.put(nxxt, next);
                stations.put(cuur, curr);
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
                            return new Pair<>(filePath, Files.readString(filePath, StandardCharsets.UTF_8));
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
