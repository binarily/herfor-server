package pl.herfor.server.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.herfor.server.data.objects.enums.Accident;
import pl.herfor.server.data.objects.enums.Severity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.OffsetDateTime;

@Data
@Embeddable
@NoArgsConstructor
public class MarkerProperties {
    @Expose
    private OffsetDateTime creationDate = OffsetDateTime.now();
    @Expose
    private OffsetDateTime modificationDate = OffsetDateTime.now();
    @JsonIgnore
    private OffsetDateTime expiryDate = OffsetDateTime.now().plusSeconds(60 * 10);
    @Enumerated(EnumType.STRING)
    @Expose
    private Accident accident;
    @Enumerated(EnumType.STRING)
    @Expose
    private Severity severity;

    public MarkerProperties(Accident accident, Severity severity) {
        this.accident = accident;
        this.severity = severity;
    }
}
