package machado.placementfacilitator.controllers;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.services.AccountServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class PlacementController {
    AccountServices accountServices;

    public PlacementController(AccountServices accountServices) {
        this.accountServices = accountServices;
    }

    @GetMapping("Accounts")
    public List<Account> getAccounts() {
        return accountServices.findAllAccounts();
    }

    @GetMapping("Admins")
    public List<Profile> getAdmins() {
        return accountServices.findAllAdmins();
    }

    @GetMapping("Students")
    public List<Profile> getStudents() {
        return accountServices.findAllStudents();
    }

    @GetMapping("Employers")
    public List<Profile> getEmployers() {
        return accountServices.findAllEmployers();
    }

}