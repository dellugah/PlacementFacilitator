package machado.placementfacilitator.repos;

import machado.placementfacilitator.models.Placement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementRepo extends JpaRepository<Placement, Long> {
}
