package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.herfor.server.data.objects.User;

public interface UserRepository extends JpaRepository<User, String> {

}
