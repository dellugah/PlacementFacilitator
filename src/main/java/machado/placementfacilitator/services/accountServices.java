package machado.placementfacilitator.services;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class accountServices {
    AccountRepo accountRepo;
    PlacementRepo placementRepo;
    ProfileRepo profileRepo;

    public accountServices(AccountRepo accountRepo, PlacementRepo placementRepo, ProfileRepo profileRepo) {
        this.accountRepo = accountRepo;
        this.placementRepo = placementRepo;
        this.profileRepo = profileRepo;
    }

    public List<Account> findAllAccounts() {
        return accountRepo.findAll();
    }

    public List<Profile> findAllAdmins() {
        List<Profile> admins = new ArrayList<>();
        accountRepo.findAll().forEach(account -> {
            if(account.getAccount_type() == Account.account_type.ADMIN) {
                admins.add(account.getProfile());
            }
        });
        return admins;
    }

    public List<Profile> findAllStudents(){
        List<Profile> students = new ArrayList<>();
        accountRepo.findAll().forEach(account -> {
            if(account.getAccount_type() == Account.account_type.STUDENT) {
                students.add(account.getProfile());
            }
        });
        return students;
    }

    public List<Profile> findAllEmployers(){
        List<Profile> employers = new ArrayList<>();
        accountRepo.findAll().forEach(account -> {
            if(account.getAccount_type() == Account.account_type.EMPLOYER) {
                employers.add(account.getProfile());
            }
        });
        return employers;
    }

}
