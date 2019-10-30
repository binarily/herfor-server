package pl.herfor.server.data.objects.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.herfor.server.data.objects.Point;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSearchRequest {
    public Point northEast, southWest;
    public OffsetDateTime date;
}
