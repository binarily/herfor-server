package pl.herfor.server.data.objects;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
}
