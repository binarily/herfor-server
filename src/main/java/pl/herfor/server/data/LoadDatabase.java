package pl.herfor.server.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportProperties;
import pl.herfor.server.data.objects.enums.Accident;
import pl.herfor.server.data.objects.enums.Severity;
import pl.herfor.server.data.repositories.GradeRepository;
import pl.herfor.server.data.repositories.ReportRepository;

@Configuration
@Slf4j
public
class LoadDatabase {

    @Bean
    @Autowired
    public CommandLineRunner initDatabase(ReportRepository repository, GradeRepository grades) {
        // TODO: remove to retain persistency
        grades.deleteAll();
        repository.deleteAll();
        ReportProperties properties = new ReportProperties(Accident.METRO, Severity.YELLOW);
        return args -> {
            log.info("Preloading " + repository.save(new Report(52.3501, 20.8600, properties)));
            log.info("Preloading " + repository.save(new Report(52.3801, 20.8900, properties)));
            log.info("Preloading " + repository.save(new Report(52.4001, 20.89100, properties)));
        };
    }
}