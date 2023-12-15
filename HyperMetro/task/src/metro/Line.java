package metro;


public class Line {
    private final String name;
    private Station firstDepot;
    private Station lastDepot;
    private Station firstStation;
    private Station lastStation;

    Line(String name) {
        this.name = name;
        this.firstDepot = new Station("depot", name, Integer.MIN_VALUE);
        this.lastDepot = new Station("depot", name, Integer.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public void addStation(Station station) {
        if (firstStation == null) {
            this.firstStation = station;
            this.lastStation = station;
        } else if (firstStation.getOrder() > station.getOrder()) {
            firstStation.addHeadStation(station);
            firstStation = station;
        } else {
            Station iter = firstStation;
            while (iter.getOrder() < station.getOrder() && iter.getNext() != null) {
                iter = iter.getNext();
            }

            if (iter.getOrder() != station.getOrder()) {
                if (iter.getNext() == null) {
                    lastStation = station;
                }

                iter.addTailStation(station);
            }

        }
    }

    public void addHeadStation(String stationName) {
        Station station = new Station(stationName, this.name, Integer.MIN_VALUE);
        firstStation.addHeadStation(station);
        firstStation = station;
    }

    public void addTailStation(String stationName) {
        Station station = new Station(stationName, this.name, Integer.MAX_VALUE);
        lastStation.addTailStation(station);
        lastStation = station;
    }

    public void removeStation(String stationName) {
        Station station = firstStation;
        boolean isFinish = false;

        while (station != null && !isFinish) {
            if (station.getName().equals(stationName)) {
                if (station == firstStation) {
                    firstStation = station.getNext();
                } else if (station == lastStation) {
                    lastStation = station.getPrev();
                }

                station.remove();
                isFinish = true;
            }
        }
    }

    public void printLine() {

        Station station = null;

        if (this.firstDepot != null && this.lastStation != null) {
            this.firstDepot.setNext(this.firstStation);
            this.lastStation.setNext(this.lastDepot);

            station = this.firstDepot;
        } else {
            System.out.println("depot");
        }



        while (station != null) {
            station.printStation();
            station = station.getNext();

        }

        if (this.firstDepot != null && this.lastStation != null) {
            this.firstDepot.setNext(null);
            this.lastStation.setNext(null);
        }
    }

    public Station getStationInLine(String stationName) {
        Station station = this.firstStation;

        while (station != null) {
            if (station.getName().equals(stationName)) {
                return station;
            }

            station = station.getNext();
        }

        return null;
    }


}
