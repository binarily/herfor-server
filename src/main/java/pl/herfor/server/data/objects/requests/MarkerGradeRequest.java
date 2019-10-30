package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.MarkerGrade;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.enums.Grade;

@Data
@AllArgsConstructor
public class MarkerGradeRequest {
    public String markerId;
    public Point location;
    public Grade grade;

    public MarkerGrade toMarkerGrade(MarkerData marker) {
        return new MarkerGrade(marker, grade);
    }
}
