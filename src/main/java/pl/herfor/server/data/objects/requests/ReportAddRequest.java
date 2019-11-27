package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportProperties;
import pl.herfor.server.data.objects.User;

@Data
@AllArgsConstructor
public class ReportAddRequest {
    private String userId;
    private Point location;
    private ReportProperties properties;

    public Report toMarker(User user) {
        return new Report(location, properties, user);
    }
}
