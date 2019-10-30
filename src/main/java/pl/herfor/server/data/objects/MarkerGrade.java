package pl.herfor.server.data.objects;

import com.google.gson.annotations.Expose;
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
public class MarkerGrade {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Expose
    @ManyToOne
    public MarkerData marker;

    @Expose
    public OffsetDateTime submissionDate = OffsetDateTime.now();

    @Expose
    private Grade grade;

    public MarkerGrade(MarkerData marker, Grade grade) {
        this.marker = marker;
        this.grade = grade;
    }
}
