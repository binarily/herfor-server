package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportGrade;
import pl.herfor.server.data.objects.enums.Grade;

@Data
@AllArgsConstructor
public class ReportGradeRequest {
    public String reportId;
    public Point location;
    public Grade grade;

    public ReportGrade toMarkerGrade(Report marker) {
        return new ReportGrade(marker, grade);
    }
}
