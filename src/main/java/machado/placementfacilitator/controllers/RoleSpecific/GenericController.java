package machado.placementfacilitator.controllers.RoleSpecific;

import machado.placementfacilitator.DTOs.EditProfileDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.AccountServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/generic")
public class GenericController {

    private final AccountServices accountServices;

    GenericController(AccountServices accountServices){
        this.accountServices = accountServices;
    }

    @PostMapping("/edit")
    public ResponseEntity<Profile> editProfile(@RequestBody EditProfileDTO editedProfile){

        Profile profile = getAccountProfile();

        if(!Objects.equals(editedProfile.getProfileId(), profile.getProfileId())){
            return ResponseEntity.badRequest().build();
        }
        else{
            return ResponseEntity.ok(accountServices.editProfile(profile, editedProfile));
        }
    }


    private Profile getAccountProfile(){
        System.out.println(">>> authenticating");
        Account currentUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (Account) authentication.getPrincipal();
        System.out.println(">>> authenticated");
        return currentUser.getProfile();
    }

}
