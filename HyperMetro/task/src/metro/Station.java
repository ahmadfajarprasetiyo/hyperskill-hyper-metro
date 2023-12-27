package metro;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private List<Station> prev;
    private List<Station> next;

    private int time;
    private final String name;
    private final String lineName;

    final private List<Station> transferStations;

    Station(String name, String lineName, int time) {
        this.lineName = lineName;
        this.name = name;
        this.time = time;
        this.transferStations = new ArrayList<>();
        this.prev = new ArrayList<>();
        this.next = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getLineName() {
        return lineName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public void addNext(Station next) {
        if (this.next.contains(next)) {
            return;
        }
        this.next.add(next);
    }

    public void addPrev(Station prev) {
        if (this.prev.contains(prev)) {
            return;
        }
        this.prev.add(prev);
    }

    public void removeNext(Station next) {
        this.next.remove(next);
    }

    public void removePrev(Station prev) {
        this.prev.remove(prev);
    }

    private List<Station> getNext() {
        return this.next;
    }

    private List<Station> getPrev() {
        return this.prev;
    }

    public Station getOneNext() {
        if (this.next.isEmpty()) {
            return null;
        }
        return this.next.get(this.next.size() - 1);
    }

    public List<Station> getNeighbor() {
        List<Station> res = new ArrayList<>();

        res.addAll(this.transferStations);
        res.addAll(this.getNext());
        res.addAll(this.getPrev());


        return res;
    }

    public void remove() {
        for (Station station : this.prev) {
            station.removeNext(this);
        }

        for (Station station : this.next) {
            station.removePrev(this);
        }
        this.prev = null;
        this.next = null;
    }

    private boolean isConnectedStation(Station newStation) {
        for (Station station : this.transferStations) {
            if (station == newStation) {
                return true;
            }
        }

        return false;
    }

    public void connectStation(Station station) {
        if (this.isConnectedStation(station)) {
            return;
        }

        this.transferStations.add(station);
        station.connectStation(this);
    }

    public void printStation() {
        System.out.print(this.getName());

        for (Station station : this.transferStations) {
            System.out.printf(" - %s (%s line)", station.getName(), station.getLineName());
        }

        System.out.println();
    }

    public boolean isFirstStation() {
        return this.prev.isEmpty();
    }

    public boolean isLastStation() {
        return this.next.isEmpty();
    }

    public boolean isContainsNext(Station station) {
        return this.getNext().contains(station);
    }

}
