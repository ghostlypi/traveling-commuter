package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ParseCSV {
    public static void main(String[] args) {
        try {
            ArrayList<ArrayList<LocalTime>> times = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            String file = Files.readString(Paths.get("src/resources/BART_RED.csv"));
            String[] lines = file.split("\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            for (String line : lines) {
                String[] vals = line.split(",");
                names.add(vals[0]);
                ArrayList<LocalTime> arr = new ArrayList();
                for (int i = 1; i < arr.size(); i++)
                    if (!vals[i].isEmpty())
                        arr.add(LocalTime.parse(vals[i], formatter));
                times.add(arr);
            }

            for (int i = 1; i < times.size(); i++) {
                for (int j = 0; j < times.get(i-1).size(); j++)
                    System.out.print(times.get(i).get(j) + ",");
                System.out.println("\b");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
