package metro;

import java.util.*;

public class MetroMap {

    private  List<Line> lines;

    MetroMap() {
        lines = new ArrayList<>();
    }

    public Line getSafeLine(String lineName) {
        Line line = this.getLine(lineName);
        if (line == null) {
            line = this.addLine(lineName);
        }
        return line;
    }

    private Line getLine(String lineName) {
        for (Line line : lines) {
            if (lineName.equals(line.getName())) {
                return line;
            }
        }

        return null;
    }

    public Line addLine(String lineName) {
        Line line = new Line(lineName);
        this.lines.add(line);
        return line;
    }
    public void buildConnection(Map<Station, List<String>> mapStationTransfer) {
        for (Station station : mapStationTransfer.keySet()) {
            List<String> values = mapStationTransfer.get(station);
            for (String value : values) {
                String[] lineAndStation = value.split("\\|");
                this.getSafeLine(lineAndStation[1]).getStationInLine(lineAndStation[0]).connectStation(station);
            }

        }
    }

    private Map<Station, Integer> searchRoute(Station srcStation,Station desStation) {
        Set<Station> visitedStation = new HashSet<>();
        Map<Station, Integer> mapDistance = new HashMap<>();
        Queue<Station> queue = new ArrayDeque<>();

        int distance = 0;

        visitedStation.add(srcStation);
        mapDistance.put(srcStation, distance);
        queue.add(srcStation);


        while (!queue.isEmpty()) {
            Station processStation = queue.poll();

            if (processStation == desStation) {
                break;
            }

            distance = mapDistance.get(processStation) + 1;

            List<Station> allNeighbor = processStation.getNeighbor();

            for (Station neighbor : allNeighbor) {
                if (visitedStation.contains(neighbor)) {
                    continue;
                }

                visitedStation.add(neighbor);
                queue.offer(neighbor);
                mapDistance.put(neighbor, distance);
            }
        }

        return mapDistance;
    }

    public void printRoute(String srcLineName, String srcStationName, String desLineName, String desStationName) {
        Station srcStation = this.getSafeLine(srcLineName).getStationInLine(srcStationName);
        Station desStation = this.getSafeLine(desLineName).getStationInLine(desStationName);

        Map<Station, Integer> mapDistance = this.searchRoute(srcStation, desStation);


        Stack<String> stackRoute = new Stack<>();
        stackRoute.push(desStationName);

        int distance = mapDistance.get(desStation);
        Station processStation = desStation;

        while (distance >= 0) {
            distance = distance - 1;

            List<Station> allNeighbor = processStation.getNeighbor();

            for (Station neighbor : allNeighbor) {
                Integer distanceNeighbor = mapDistance.get(neighbor);

                if (distanceNeighbor == null || distanceNeighbor != distance) {
                    continue;
                }


                if (!neighbor.getLineName().equals(processStation.getLineName())) {
                    stackRoute.push(String.join("","Transition to line ",processStation.getLineName()));
                }

                stackRoute.push(neighbor.getName());
                processStation = neighbor;
                break;
            }
        }



        while (!stackRoute.isEmpty()) {
            System.out.println(stackRoute.pop());
        }
    }

    public void buildConnection(String lineName1, String stationName1, String lineName2, String stationName2) {
        this.getSafeLine(lineName1).getStationInLine(stationName1).connectStation(this.getSafeLine(lineName2).getStationInLine(stationName2));
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }
}
