package metro;

import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        final String ACTION_EXIT = "/exit";
        final String ACTION_APPEND = "/append";
        final String ACTION_ADD = "/add";
        final String ACTION_ADD_HEAD = "/add-head";
        final String ACTION_REMOVE = "/remove";
        final String ACTION_OUTPUT = "/output";

        List<Line> lines = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");

        String filePath = "";
        if (args.length > 0) {
            filePath = args[0];
        }

        try {
            lines = readJson(filePath);
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        } catch (Exception e) {
            System.out.println("Incorrect file");
        }

        if (!lines.isEmpty()) {
            String action;
            do {
                action = ACTION_EXIT;
                List<String> params = new ArrayList<>();
                Matcher regexMatcher = regex.matcher(scanner.nextLine());
                while (regexMatcher.find()) {
                    String param = regexMatcher.group();
                    param = param.replaceAll("\"", "");
                    params.add(param);
                }

                if(!params.isEmpty()) {
                    action = params.get(0);
                }

                Line line = null;
                switch (action){
                    case ACTION_APPEND, ACTION_ADD, ACTION_ADD_HEAD, ACTION_REMOVE -> {
                        if (params.size() != 3) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        line = getLine(lines, params.get(1));
                    }
                    case ACTION_OUTPUT -> {
                        if (params.size() != 2) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        line = getLine(lines, params.get(1));
                    }
                }

                switch (action){
                    case ACTION_APPEND, ACTION_ADD -> {
                        if (line != null) {
                            line.addTailStation(params.get(2));
                        }
                    }
                    case ACTION_ADD_HEAD -> {
                        if (line != null) {
                            line.addHeadStation(params.get(2));
                        }
                    }
                    case ACTION_REMOVE -> {
                        if (line != null) {
                            line.removeStation(params.get(2));
                        }
                    }
                    case ACTION_OUTPUT -> {
                        if (line != null) {
                            line.printLine();
                        }
                    }
                    case ACTION_EXIT -> System.out.print("");
                    default -> System.out.println("Invalid command");
                }

            } while (!action.equals(ACTION_EXIT));
        }

    }

    public static Line getLine(List<Line> lines, String lineName) {
        for (Line line : lines) {
            if (lineName.equals(line.getName())) {
                return line;
            }
        }

        return null;
    }
    public static ArrayList<Line> readJson(String fileName) throws IOException, NumberFormatException {
        ArrayList<Line> lines = new ArrayList<>();
        JsonReader jsonReader = new JsonReader(new FileReader(fileName));

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String nameLine = jsonReader.nextName();
            Line line = new Line(nameLine);

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String orderString = jsonReader.nextName();
                String nameStation = jsonReader.nextString();
                int order = Integer.parseInt(orderString);

                Station station = new Station(nameStation, order);
                line.addStation(station);
            }
            lines.add(line);
            jsonReader.endObject();
        }

        return lines;

    }
}



