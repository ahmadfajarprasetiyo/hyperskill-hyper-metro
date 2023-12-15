package metro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void buildConnection(String lineName1, String stationName1, String lineName2, String stationName2) {
        this.getSafeLine(lineName1).getStationInLine(stationName1).connectStation(this.getSafeLine(lineName2).getStationInLine(stationName2));
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }
}
