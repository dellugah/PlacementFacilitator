package machado.placementfacilitator.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public void getUser() {
        //TODO Retrieve current logged in user
    }

    @GetMapping
    public void getAllUsers() {
        //TODO Retrieve sll authenticated users
    }
}
