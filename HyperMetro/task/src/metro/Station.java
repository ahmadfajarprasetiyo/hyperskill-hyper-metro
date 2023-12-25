package metro;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private Station prev = null;
    private Station next = null;

    private final int time;
    private final String name;
    private final String lineName;
    private final int order;

    private List<Station> transferStations;

    Station(String name, String lineName, int order, int time) {
        this.lineName = lineName;
        this.name = name;
        this.order = order;
        this.time = time;
        this.transferStations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getLineName() {
        return lineName;
    }

    public int getOrder() {
        return order;
    }

    public int getTime() {
        return time;
    }
    public void setNext(Station next) {
        this.next = next;
    }

    public void setPrev(Station prev) {
        this.prev = prev;
    }

    public Station getNext() {
        return next;
    }

    public Station getPrev() {
        return prev;
    }

    public void addTailStation(Station station) {
        station.setNext(this.next);
        this.next = station;
        station.setPrev(this);

        if (station.getNext() != null) {
            station.getNext().setPrev(station);
        }
    }

    public void addHeadStation(Station station) {
        this.prev = station;
        station.setNext(this);
    }

    public List<Station> getNeighbor() {
        List<Station> res = new ArrayList<>();

        if (this.getNext() != null) {
            res.add(this.getNext());
        }

        if (this.getPrev() != null) {
            res.add(this.getPrev());
        }

        res.addAll(this.transferStations);

        return res;
    }

    public void remove() {
        if (this.prev != null) {
            this.prev.setNext(this.next);
        }

        if (this.next != null) {
            this.next.setPrev(this.prev);
        }
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



}
