package machado.placementfacilitator.repos;

import machado.placementfacilitator.models.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface PlacementRepo extends JpaRepository<Placement, Long> {
}
