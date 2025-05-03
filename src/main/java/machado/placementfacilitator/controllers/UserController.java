package machado.placementfacilitator.controllers;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.AccountServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Profile> authenticatedAccount() {
        Account currentUser;
        Profile profile;
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            currentUser = (Account) authentication.getPrincipal();
            profile = currentUser.getProfile();
            return ResponseEntity.ok(profile);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //TODO Edit profile logic
    @PostMapping("/edit")
    public ResponseEntity<Boolean> editProfile(){
        Boolean success = true;
        if(success){
            return ResponseEntity.ok(success);
        }

        return ResponseEntity.badRequest().build();
    }
}
