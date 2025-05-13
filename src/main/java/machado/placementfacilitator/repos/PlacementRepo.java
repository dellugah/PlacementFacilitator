package machado.placementfacilitator.repos;

import jakarta.transaction.Transactional;
import machado.placementfacilitator.models.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface PlacementRepo extends JpaRepository<Placement, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM potential_candidates WHERE placement_id = ?1", nativeQuery = true)
    void deletePotentialCandidatesRelationships(Long placementId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM profile_placements WHERE placements_placement_id = ?1", nativeQuery = true)
    void deleteProfilePlacementsRelationships(Long placementId);


}
