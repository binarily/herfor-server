package pl.herfor.server.data.objects;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Inheritance
public class Report {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Expose
    private String id;
    @Embedded
    @Expose
    private Point location;
    @Expose
    @ManyToOne
    public User user;

    @Embedded
    @Expose
    private ReportProperties properties;

    public Report(Double latitude, Double longitude, ReportProperties properties) {
        this.location = new Point(latitude, longitude);
        this.properties = properties;
    }

    public Report(Point location, ReportProperties properties) {
        this.location = location;
        this.properties = properties;
    }
}