package machado.placementfacilitator.controllers.RoleSpecific;

import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.AccountServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {
    private final AccountServices accountService;

    public EmployerController(AccountServices accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<Profile>> allStudents(@RequestBody String type) {
        try{
            List<Profile> users = accountService.findAccountByAccountType(type);
            return ResponseEntity.ok(users);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
