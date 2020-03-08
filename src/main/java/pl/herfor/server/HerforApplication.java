package pl.herfor.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class HerforApplication {

    public static final String credentialsFileLocation = "CHANGE CREDENTIALS FILE LOCATION HERE";
    public static final String databaseUrl = "https://obs-czerniakk.firebaseio.com";

    public static void main(String[] args) {
        try {
            //USE WHEN AUTHENTICATING WITH A GENERATED FILE
            //FileInputStream serviceAccount =
            //        new FileInputStream(credentialsFileLocation);
            //GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            //USE WHEN AUTHENTICATING WITH GOOGLE CLOUD API
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(databaseUrl)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SpringApplication.run(HerforApplication.class, args);
    }

}
