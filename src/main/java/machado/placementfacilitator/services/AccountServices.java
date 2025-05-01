package machado.placementfacilitator.services;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.repos.AccountRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServices {
    private final AccountRepo accountRepo;

    public AccountServices(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<Account> allAccounts() {
        List<Account> users = new ArrayList<>();

        accountRepo.findAll().forEach(users::add);

        return users;
    }

}
