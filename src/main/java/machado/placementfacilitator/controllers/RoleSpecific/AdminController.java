package machado.placementfacilitator.controllers.RoleSpecific;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public String test(){
        return "admin";
    }
    //TODO Admin functionalities
}
