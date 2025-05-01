package machado.placementfacilitator.repos;
import machado.placementfacilitator.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
public interface ProfileRepo extends JpaRepository<Profile, Long> {
}
