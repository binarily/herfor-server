package pl.herfor.server.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.herfor.server.data.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Data
@NoArgsConstructor
@Entity(name = "reportUser")
@Inheritance
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Expose
    private String id;
    @JsonIgnore
    private Integer reportDurationsCount = 0;
    @JsonIgnore
    private Long reportDurations = 0L;

    public Double calculateReliability() {
        if (reportDurationsCount == 0) {
            return 1.0;
        }
        return (double) reportDurations / (reportDurationsCount * Constants.REGULAR_EXPIRY_DURATION);
    }
}
