package metro;


public class Line {
    private final String name;
    private Station firstDepot;
    private Station lastDepot;
    private Station firstStation;
    private Station lastStation;

    Line(String name) {
        this.name = name;
        this.firstDepot = new Station("depot", Integer.MIN_VALUE);
        this.lastDepot = new Station("depot", Integer.MAX_VALUE);

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

            if (iter.getNext() == null) {
                lastStation = station;
            }

            iter.addTailStation(station);
        }
    }

    public void addHeadStation(String stationName) {
        Station station = new Station(stationName, Integer.MIN_VALUE);
        firstStation.addHeadStation(station);
        firstStation = station;
    }

    public void addTailStation(String stationName) {
        Station station = new Station(stationName, Integer.MAX_VALUE);
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

        Station station1 = null;
        Station station2 = null;
        Station station3 = null;

        if (lastStation != null) {
            this.firstDepot.setNext(this.firstStation);
            this.lastStation.setNext(this.lastDepot);

            station1 = this.firstDepot;
            station2 = station1.getNext();
            station3 = station2.getNext();
        } else {
            System.out.println("depot");
        }



        while (station3 != null) {
            System.out.printf("%s - %s - %s", station1.getName(), station2.getName(), station3.getName());
            System.out.println();

            station1 = station2;
            station2 = station3;
            station3 = station3.getNext();

        }

        if (lastStation != null) {
            this.firstDepot.setNext(null);
            this.lastStation.setNext(null);
        }
    }


}
