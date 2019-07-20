package pl.herfor.server.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.herfor.server.data.objects.AccidentType;
import pl.herfor.server.data.objects.Marker;
import pl.herfor.server.data.objects.MarkerProperties;
import pl.herfor.server.data.objects.SeverityType;
import pl.herfor.server.data.repositories.MarkerRepository;

@Configuration
@Slf4j
public
class LoadDatabase {

    @Bean
    @Autowired
    public CommandLineRunner initDatabase(MarkerRepository repository) {
        // TODO: remove to retain persistency
        repository.deleteAll();
        MarkerProperties properties = new MarkerProperties(AccidentType.METRO, SeverityType.YELLOW);
        return args -> {
            log.info("Preloading " + repository.save(new Marker(52.3501, 20.8600, properties)));
            log.info("Preloading " + repository.save(new Marker(52.3801, 20.8900, properties)));
            log.info("Preloading " + repository.save(new Marker(52.4001, 20.89100, properties)));
        };
    }
}