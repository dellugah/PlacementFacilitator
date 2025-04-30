package machado.placementfacilitator.repos;
import machado.placementfacilitator.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface AccountRepo extends JpaRepository<Account, Long> {}
