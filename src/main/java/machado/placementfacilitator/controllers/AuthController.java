package machado.placementfacilitator.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public void login() {
        //TODO implement authentication logic
    }

    @PostMapping("/logout")
    public void logout() {
        //TODO implement logout logic
    }

    @PostMapping("/register")
    public void register() {
        //TODO implement new account logic
    }
}
