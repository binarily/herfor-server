package pl.herfor.server.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportProperties;
import pl.herfor.server.data.objects.User;
import pl.herfor.server.data.objects.enums.Accident;
import pl.herfor.server.data.objects.enums.Severity;
import pl.herfor.server.data.repositories.GradeRepository;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.repositories.UserRepository;

import static pl.herfor.server.data.Constants.DEV_SWITCH;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    @Autowired
    public CommandLineRunner initDatabase(ReportRepository repository, GradeRepository grades, UserRepository users) {
        if (!DEV_SWITCH) {
            return args -> log.info("Database init ignored (no dev switch).");
        }
        grades.deleteAll();
        repository.deleteAll();
        ReportProperties properties = new ReportProperties(Accident.METRO, Severity.YELLOW);
        User user = new User();
        user = users.save(user);
        User finalUser = user;
        return args -> {
            log.info("Preloading " + repository.save(new Report(52.3501, 20.8600, properties.withSeverity(Severity.RED), finalUser)));
            log.info("Preloading " + repository.save(new Report(52.3801, 20.8900, properties.withAccident(Accident.BIKE), finalUser)));
            log.info("Preloading " + repository.save(new Report(52.4001, 20.89100, properties.withSeverity(Severity.YELLOW), finalUser)));
        };
    }
}