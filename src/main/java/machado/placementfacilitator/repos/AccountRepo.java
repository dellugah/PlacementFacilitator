package machado.placementfacilitator.repos;
import machado.placementfacilitator.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<List<Account>> findAllByAccountType(Account.AccountType type);
}
