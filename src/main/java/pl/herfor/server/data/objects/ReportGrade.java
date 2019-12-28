package pl.herfor.server.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.herfor.server.data.objects.enums.Grade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
public class ReportGrade {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @JsonIgnore
    @ManyToOne
    private Report marker;

    @ManyToOne
    private User user;

    private OffsetDateTime submissionDate = OffsetDateTime.now();

    private Grade grade;

    public ReportGrade(Report marker, Grade grade) {
        this.marker = marker;
        this.grade = grade;
    }

    @JsonProperty
    public String getReportId() {
        return marker.getId();
    }
}
