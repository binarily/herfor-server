package pl.herfor.server.data.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.herfor.server.data.objects.User;
import pl.herfor.server.data.repositories.UserRepository;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/users/register")
    public User registerUser() {
        return userRepository.save(new User());
    }
}
