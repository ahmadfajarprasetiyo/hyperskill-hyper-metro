package metro;

public class Station {

    private Station prev = null;
    private Station next = null;
    private String name;
    private int order;

    Station(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
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

    public void remove() {
        if (this.prev != null) {
            this.prev.setNext(this.next);
        }

        if (this.next != null) {
            this.next.setPrev(this.prev);
        }
    }



}
