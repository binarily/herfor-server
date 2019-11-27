package pl.herfor.server.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public User user;

    @Embedded
    @Expose
    private ReportProperties properties;

    public Report(Double latitude, Double longitude, ReportProperties properties, User user) {
        this.location = new Point(latitude, longitude);
        this.properties = properties;
        this.user = user;
    }

    public Report(Point location, ReportProperties properties, User user) {
        this.location = location;
        this.properties = properties;
        this.user = user;
    }
}
