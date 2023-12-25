package metro;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
        final String ACTION_CONNECT = "/connect";
        final String ACTION_ROUTE = "/route";
        final String ACTION_FASTEST_ROUTE = "/fastest-route";

        MetroMap metroMap = new MetroMap();
        Scanner scanner = new Scanner(System.in);
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");

        String filePath = "test3.json";
        if (args.length > 0) {
            filePath = args[0];
        }

        try {
            metroMap = readJson(filePath);
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        } catch (Exception e) {
            System.out.println("Incorrect file");
        }

        if (!metroMap.isEmpty()) {
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
                        if (params.size() != 4 && params.size() != 3) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        line = metroMap.getSafeLine(params.get(1));
                    }
                    case ACTION_OUTPUT -> {
                        if (params.size() != 2) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        line = metroMap.getSafeLine(params.get(1));
                    }

                    case ACTION_CONNECT, ACTION_ROUTE, ACTION_FASTEST_ROUTE -> {
                        if (params.size() != 5) {
                            System.out.println("Invalid command");
                            continue;
                        }
                    }
                }

                switch (action){
                    case ACTION_APPEND, ACTION_ADD -> {
                        int time = Integer.MAX_VALUE;
                        if (params.size() == 4) {
                            time = Integer.parseInt(params.get(3));
                        }
                        if (line != null) {
                            line.addTailStation(params.get(2), time);
                        }
                    }
                    case ACTION_ADD_HEAD -> {
                        int time = Integer.MAX_VALUE;
                        if (params.size() == 4) {
                            time = Integer.parseInt(params.get(3));
                        }
                        if (line != null) {
                            line.addHeadStation(params.get(2), time);
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
                    case ACTION_CONNECT -> metroMap.buildConnection(params.get(1),params.get(2),params.get(3),params.get(4));
                    case ACTION_ROUTE -> metroMap.printRoute(params.get(1),params.get(2),params.get(3),params.get(4));
                    case ACTION_FASTEST_ROUTE -> metroMap.printFastestRoute(params.get(1),params.get(2),params.get(3),params.get(4));
                    case ACTION_EXIT -> System.out.print("");
                    default -> System.out.println("Invalid command");
                }

            } while (!action.equals(ACTION_EXIT));
        }

    }


    public static MetroMap readJson(String fileName) throws IOException, NumberFormatException {
        MetroMap metroMap = new MetroMap();
        JsonReader jsonReader = new JsonReader(new FileReader(fileName));
        Map<Station, List<String>> mapStationTransfer = new HashMap<>();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String lineName = jsonReader.nextName();
            Line line = metroMap.getSafeLine(lineName);

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String orderString = jsonReader.nextName();
                List<String> transfer = new ArrayList<>();
                int order = Integer.parseInt(orderString);
                String stationName = "";
                int timeElapse = 0;

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String nameAttribute = jsonReader.nextName();

                    if (nameAttribute.equals("time")) {
                        if (jsonReader.peek() == JsonToken.NUMBER) {
                            timeElapse = jsonReader.nextInt();
                        } else if (jsonReader.peek() == JsonToken.NULL) {
                            jsonReader.nextNull();
                        }

                    }

                    if (nameAttribute.equals("name")) {
                        stationName = jsonReader.nextString();
                    }

                    if (nameAttribute.equals("transfer")) {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            String nameStationTransfer = "";
                            String nameLineTransfer = "";

                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String nameAttributeArray = jsonReader.nextName();

                                if (nameAttributeArray.equals("line")) {
                                    nameLineTransfer = jsonReader.nextString();
                                }

                                if (nameAttributeArray.equals("station")) {
                                    nameStationTransfer = jsonReader.nextString();
                                }
                            }
                            jsonReader.endObject();

                            transfer.add(String.join("|", nameStationTransfer, nameLineTransfer));
                        }

                        jsonReader.endArray();
                    }
                }
                jsonReader.endObject();

                Station station = new Station(stationName, lineName, order, timeElapse);
                line.addStation(station);

                mapStationTransfer.put(station, transfer);
            }

            jsonReader.endObject();
        }
        jsonReader.endObject();

        metroMap.buildConnection(mapStationTransfer);

        return metroMap;

    }
}



