package machado.placementfacilitator.repos;
import machado.placementfacilitator.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ProfileRepo extends JpaRepository<Profile, Long> {}
