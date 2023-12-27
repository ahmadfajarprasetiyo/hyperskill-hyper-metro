package metro;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Line {
    private final String name;
    final private Station firstDepot;
    final private Station lastDepot;
    final private Map<String, Station> mapStationName;

    Line(String name) {
        this.name = name;
        this.firstDepot = new Station("depot", name, Integer.MAX_VALUE);
        this.lastDepot = new Station("depot", name, Integer.MAX_VALUE);
        this.mapStationName = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    private void addStation(Station station, List<String> next, List<String> prev) {
        for (String nextStationName : next) {
            Station neighbor = this.getSafeStationInLine(nextStationName);

            station.addNext(neighbor);
            neighbor.addPrev(station);
        }

        for (String prevStationName : prev) {
            Station neighbor = this.getSafeStationInLine(prevStationName);

            neighbor.addNext(station);
            station.addPrev(neighbor);

        }
    }

    public Station addStation(String stationName, int time, List<String> next, List<String> prev) {
        Station station = this.getSafeStationInLine(stationName);
        station.setTime(time);


        this.addStation(station, next, prev);

        return station;
    }

    public void removeStation(String stationName) {
        Station station = this.getStationInLine(stationName);

        if (station != null) {
            station.remove();
        }
    }

    public void printLine() {

        Station station = null;
        Station firstStation = null;
        Station lastStation = null;

        for (String key : this.mapStationName.keySet()) {
            Station stationTemp = this.mapStationName.get(key);
            if (stationTemp.isFirstStation()) {
                firstStation = stationTemp;
            }

            if (stationTemp.isLastStation()) {
                lastStation = stationTemp;
            }
        }


        if (lastStation != null) {
            this.firstDepot.addNext(firstStation);
            lastStation.addNext(this.lastDepot);

            station = this.firstDepot;
        } else {
            System.out.println("depot");
        }



        while (station != null) {
            station.printStation();
            station = station.getOneNext();

        }

        if (lastStation != null) {
            firstDepot.removeNext(firstStation);
            lastStation.removeNext(this.lastDepot);
        }
    }

    public Station getStationInLine(String stationName) {
        return this.mapStationName.get(stationName);
    }

    private Station getSafeStationInLine(String stationName) {

        Station station = this.getStationInLine(stationName);
        if (station == null) {
            station = new Station(stationName, this.name, Integer.MAX_VALUE);
            this.mapStationName.put(stationName, station);
        }

        return station;
    }


}
