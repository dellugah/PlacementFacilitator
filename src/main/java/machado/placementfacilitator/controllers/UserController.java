package machado.placementfacilitator.controllers;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.services.AccountServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final AccountServices accountService;

    public UserController(AccountServices accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ResponseEntity<Account> authenticatedAccount() {
        Account currentUser = null;
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            currentUser = (Account) authentication.getPrincipal();
            System.out.println("current user: " + currentUser);
        }catch (Exception e){
            System.out.println("could not find current user");
        }

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<Account>> allAccounts() {
        List<Account> users = accountService.allAccounts();
        return ResponseEntity.ok(users);
    }
}
