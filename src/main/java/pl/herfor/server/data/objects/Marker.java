package pl.herfor.server.data.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Inheritance
public class Marker {
    @ReadOnlyProperty
    private String type = "Marker";
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Embedded
    private Point location;
    @Embedded
    private MarkerProperties properties;

    public Marker(Double latitude, Double longitude, MarkerProperties properties) {
        this.location = new Point(latitude, longitude);
        this.properties = properties;
    }


}
