package pl.herfor.server.data.objects;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @Expose
    private double latitude;
    @Expose
    private double longitude;
}
