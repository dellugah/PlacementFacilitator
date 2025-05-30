package machado.placementfacilitator.controllers.RoleSpecific;

import machado.placementfacilitator.DTOs.EmployersDTOs.AddToPlacementDTO;
import machado.placementfacilitator.DTOs.EmployersDTOs.OfferPlacementDTO;
import machado.placementfacilitator.DTOs.EmployersDTOs.PlacementDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Placement;
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
    public ResponseEntity<Boolean> matchStudentWithPlacement(@RequestBody AddToPlacementDTO input){
        try{
            employerService.addStudentToPlacement(input.getStudentId(), input.getPlacementId());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/create-placement")
    public ResponseEntity<Placement> createPlacement(@RequestBody PlacementDTO placementDTO){
        System.out.println(placementDTO);
        try{
            Placement placement = employerService.createPlacement(getAccountProfile(), placementDTO);
            return ResponseEntity.ok(placement);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/edit-placement")
    public ResponseEntity<Placement> editPlacement(@RequestBody PlacementDTO placementDTO){
        try{
            Placement placement = employerService.editPlacement(placementDTO, getAccountProfile());
            return ResponseEntity.ok(placement);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/delete-placement")
    public ResponseEntity<Boolean> deletePlacement(@RequestBody PlacementDTO placement){
        if(placement != null){
            System.out.println(placement);
            employerService.deletePlacement(placement, getAccountProfile());
            return ResponseEntity.ok(true);
        } else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/offer-placement")
    public ResponseEntity<Boolean> sendPlacementOffer(@RequestBody OfferPlacementDTO offer){
        try{
            employerService.sendPlacementOffer(offer.getStudentId(), offer.getPlacementId());
            return ResponseEntity.ok(true);
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
