package machado.placementfacilitator.controllers.RoleSpecific;

import machado.placementfacilitator.DTOs.EmployersDTOs.AddToPlacementDTO;
import machado.placementfacilitator.DTOs.EmployersDTOs.OfferPlacementDTO;
import machado.placementfacilitator.DTOs.EmployersDTOs.PlacementDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.AccountServices;
import machado.placementfacilitator.services.EmployerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    /// TODO: access if returning the profile is a good idea to update the current logged in user as needed
    @GetMapping("/students")
    public ResponseEntity<List<Profile>> allStudents() {
        try{
            List<Profile> users = employerService.getAllStudents();
            return ResponseEntity.ok(users);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/match-student")
    public ResponseEntity<Void> matchStudentWithPlacement(@RequestBody AddToPlacementDTO input){
        try{
            employerService.addStudentToPlacement(input.getStudentId(), input.getPlacementId());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-placement")
    public ResponseEntity<Void> createPlacement(@RequestBody PlacementDTO placementDTO){
        System.out.println(placementDTO);
        try{
            employerService.createPlacement(getAccountProfile(), placementDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit-placement")
    public ResponseEntity<Void> editPlacement(@RequestBody PlacementDTO placementDTO){
        try{
            employerService.editPlacement(placementDTO, getAccountProfile());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-placement")
    public ResponseEntity<Void> deletePlacement(@RequestBody PlacementDTO placement){
        if(placement != null){
            System.out.println(placement);
            employerService.deletePlacement(placement, getAccountProfile());
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/placement-offer")
    public ResponseEntity<Void> sendPlacementOffer(@RequestBody OfferPlacementDTO offer){
        try{
            employerService.sendPlacementOffer(offer.getStudentId(), offer.getPlacementId());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
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
