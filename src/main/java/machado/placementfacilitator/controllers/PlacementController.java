package machado.placementfacilitator.controllers;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.services.accountServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlacementController {
    accountServices accountServices;

    public PlacementController(accountServices accountServices) {
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