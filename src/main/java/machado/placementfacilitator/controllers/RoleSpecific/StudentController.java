package machado.placementfacilitator.controllers.RoleSpecific;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/student")
public class StudentController {

    StudentService studentService;
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PostMapping("/accept-placement")
    public ResponseEntity<Profile> acceptPlacement(@RequestBody Long placementId){
        try{
            Profile profile = studentService.acceptPlacement(getAccountProfile(), placementId);
            log.info("Student {} accepted placement {}",profile.getFirstName(), placementId);
            return ResponseEntity.ok(profile);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reject-placement")
    public ResponseEntity<Profile> rejectPlacement(@RequestBody Long placementId){
        try{
            Profile profile = studentService.rejectPlacement(getAccountProfile(), placementId);
            return ResponseEntity.ok(profile);
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
