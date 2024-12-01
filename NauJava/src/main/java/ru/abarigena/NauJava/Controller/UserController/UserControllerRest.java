package ru.abarigena.NauJava.Controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllerRest {
    private final UserService userService;

    @Autowired
    public UserControllerRest(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser, @PathVariable Long id) {
        String existUserName = userService.findById(id).getUsername();
        User updated = userService.updateUser(existUserName, updatedUser.getFirstName(), updatedUser.getLastName(),
                updatedUser.getAge(), updatedUser.getPhoneNumber());
        return ResponseEntity.ok(updated);
    }

}
