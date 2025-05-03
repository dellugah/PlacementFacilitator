package machado.placementfacilitator.services;

import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Profile> findAccountByAccountType(String type) {
        Account.AccountType accountType = Account.AccountType.valueOf(type);
        List<Profile> users = new ArrayList<>();
        Optional<List<Account>> accounts = accountRepo.findAllByAccountType(accountType);
        if (accounts.isEmpty()) {
            return users;
        }

        accounts.get().forEach(account -> {
            if (account.getProfile().isVisible()) {
                users.add(account.getProfile());
            }
        });

        return users;
    }

}
