package pl.herfor.server.data.objects;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Inheritance
@OnDelete(action = OnDeleteAction.CASCADE)
public class MarkerData {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Expose
    private String id;
    @Embedded
    @Expose
    private Point location;
    @Embedded
    @Expose
    private MarkerProperties properties;

    public MarkerData(Double latitude, Double longitude, MarkerProperties properties) {
        this.location = new Point(latitude, longitude);
        this.properties = properties;
    }

    public MarkerData(Point location, MarkerProperties properties) {
        this.location = location;
        this.properties = properties;
    }
}
