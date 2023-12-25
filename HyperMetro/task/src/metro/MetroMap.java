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

    private Map<Station, DijkstraStation> searchFastestRoute(Station srcStation,Station desStation) {

        Set<Station> visitedStation = new HashSet<>();
        Map<Station, DijkstraStation> mapTimeElapse = new HashMap<>();
        PriorityQueue<DijkstraStation> priorityQueue = new PriorityQueue<>(new DijkstraStationComparator());

        DijkstraStation srcDijkstraStation = new DijkstraStation(srcStation, 0);
        priorityQueue.offer(srcDijkstraStation);
        mapTimeElapse.put(srcStation, srcDijkstraStation);
        visitedStation.add(srcStation);

        while (!priorityQueue.isEmpty()) {
            DijkstraStation processDijkstraStation = priorityQueue.poll();
            if (processDijkstraStation.getStation() == desStation) {
                break;
            }

            List<Station> allNeighbor = processDijkstraStation.getStation().getNeighbor();

            for (Station neighbor : allNeighbor) {
                int timeElapse = getTimeElapse(neighbor, processDijkstraStation);

                DijkstraStation neighborDijkstraStation = mapTimeElapse.computeIfAbsent(neighbor, n -> new DijkstraStation(n, timeElapse));

                if (neighborDijkstraStation.getTimeElapse() > timeElapse) {
                    neighborDijkstraStation.setTimeElapse(timeElapse);
                }

                if (!visitedStation.contains(neighbor)) {
                    priorityQueue.offer(neighborDijkstraStation);
                    visitedStation.add(neighbor);
                }


            }

        }



        return mapTimeElapse;

    }

    public void printFastestRoute(String srcLineName, String srcStationName, String desLineName, String desStationName) {
        Station srcStation = this.getSafeLine(srcLineName).getStationInLine(srcStationName);
        Station desStation = this.getSafeLine(desLineName).getStationInLine(desStationName);

        Map<Station, DijkstraStation> mapTimeElapse = this.searchFastestRoute(srcStation, desStation);

        Stack<String> stackRoute = new Stack<>();
        stackRoute.push(desStationName);

        DijkstraStation desDijkstraStation = mapTimeElapse.get(desStation);
        int timeElapse = desDijkstraStation.getTimeElapse();

        Station processStation = desStation;

        while (processStation !=  srcStation) {

            List<Station> allNeighbor = processStation.getNeighbor();

            for (Station neighbor : allNeighbor) {
                DijkstraStation neighborDijkstraStation = mapTimeElapse.get(neighbor);

                if (neighborDijkstraStation == null ) {
                    continue;
                }

                int timeElapseTemp = getTimeElapse(processStation, neighborDijkstraStation);
                if (timeElapseTemp != timeElapse) {
                    continue;
                }


                if (!neighbor.getLineName().equals(processStation.getLineName())) {
                    stackRoute.push(String.join("","Transition to line ",processStation.getLineName()));
                }

                stackRoute.push(neighbor.getName());
                processStation = neighbor;
                timeElapse = neighborDijkstraStation.getTimeElapse();
                break;
            }
        }



        while (!stackRoute.isEmpty()) {
            System.out.println(stackRoute.pop());
        }

        System.out.printf("Total: %d minutes in the way\n", desDijkstraStation.getTimeElapse());
    }

    private static int getTimeElapse(Station neighbor, DijkstraStation processDijkstraStation) {
        final int TIME_TO_TRANSFER_LINE = 5;
        int timeElapse = processDijkstraStation.getTimeElapse();
        if (!processDijkstraStation.getStation().getLineName().equals(neighbor.getLineName())) {
            timeElapse = timeElapse + TIME_TO_TRANSFER_LINE;
        } else if (processDijkstraStation.getStation().getNext() == neighbor){
            timeElapse = timeElapse + processDijkstraStation.getStation().getTime();
        } else {
            timeElapse = timeElapse + neighbor.getTime();
        }
        return timeElapse;
    }

    static class DijkstraStationComparator implements Comparator<DijkstraStation>{

        public int compare(DijkstraStation s1, DijkstraStation s2) {
            return Integer.compare(s1.getTimeElapse(), s2.getTimeElapse());
        }
    }

    static class DijkstraStation{
        private Station station;
        private int timeElapse;

        DijkstraStation(Station station, int timeElapse) {
            this.station = station;
            this.timeElapse = timeElapse;
        }

        public int getTimeElapse() {
            return timeElapse;
        }

        public Station getStation() {
            return station;
        }

        public void setTimeElapse(int timeElapse) {
            this.timeElapse = timeElapse;
        }
    }
}
