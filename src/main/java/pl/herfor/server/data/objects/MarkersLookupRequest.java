package pl.herfor.server.data.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkersLookupRequest {
    public Point northEast, southWest;
    public Date date;
}
