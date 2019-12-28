package pl.herfor.server.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import pl.herfor.server.data.Constants;
import pl.herfor.server.data.objects.enums.Accident;
import pl.herfor.server.data.objects.enums.Severity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.OffsetDateTime;

@Data
@Embeddable
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class ReportProperties {
    @Expose
    private OffsetDateTime creationDate = OffsetDateTime.now();
    @Expose
    private OffsetDateTime modificationDate = OffsetDateTime.now();
    @JsonIgnore
    private OffsetDateTime expiryDate = OffsetDateTime.now().plusMinutes(Constants.REGULAR_EXPIRY_DURATION);
    @Enumerated(EnumType.STRING)
    @Expose
    private Accident accident;
    @Enumerated(EnumType.STRING)
    @Expose
    private Severity severity;

    public ReportProperties(Accident accident, Severity severity) {
        this.accident = accident;
        this.severity = severity;
    }
}
