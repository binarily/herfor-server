package pl.herfor.server.data.objects;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Instant;
import java.util.Date;

@Data
@Embeddable
@NoArgsConstructor
public class MarkerProperties {
    private Date creationDate = Date.from(Instant.now());
    private Date modificationDate = Date.from(Instant.now());
    private AccidentType accidentType;
    private SeverityType severityType;

    public MarkerProperties(AccidentType accidentType, SeverityType severityType) {
        this.accidentType = accidentType;
        this.severityType = severityType;
    }

}
