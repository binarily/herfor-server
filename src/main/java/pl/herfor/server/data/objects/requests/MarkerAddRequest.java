package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.MarkerProperties;
import pl.herfor.server.data.objects.Point;

@Data
@AllArgsConstructor
public class MarkerAddRequest {
    private Point location;
    private MarkerProperties properties;

    public MarkerData toMarker() {
        return new MarkerData(location, properties);
    }
}
