package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportProperties;

@Data
@AllArgsConstructor
public class ReportAddRequest {
    private Point location;
    private ReportProperties properties;

    public Report toMarker() {
        return new Report(location, properties);
    }
}
