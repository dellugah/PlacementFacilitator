package machado.placementfacilitator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import machado.placementfacilitator.DTOs.EmployersDTOs.PlacementDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@Transactional
public class EmployerService {
    private final AccountRepo accountRepo;
    private final ProfileRepo profileRepo;
    private final PlacementRepo placementRepo;

    public EmployerService(AccountRepo accountRepo,
                           ProfileRepo profileRepo,
                           PlacementRepo placementRepo) {
        this.accountRepo = accountRepo;
        this.profileRepo = profileRepo;
        this.placementRepo = placementRepo;
    }

    public Profile addStudentToPlacement(Long studentId, Long placementId) {
        try {
            Profile student = profileRepo.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            Account account = accountRepo.findByProfile(student)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));

            if (account.getAccountType() != Account.AccountType.STUDENT) {
                throw new IllegalArgumentException("Profile is not a student account");
            }

            Placement placement = placementRepo.findById(placementId)
                    .orElseThrow(() -> new IllegalArgumentException("Placement not found"));

            if (placement.getPotentialCandidates().contains(student)) {
                throw new IllegalArgumentException("Student already on this placement list");
            }

            placement.getPotentialCandidates().add(student);
            placementRepo.save(placement);
            log.info("Added student {} to placement {}", studentId, placementId);
            return student;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to add Student to Placement");
        }

    }

    public Placement createPlacement(Profile profile, PlacementDTO placementDTO) {
        // Validate inputs
        if (profile == null || placementDTO == null) {
            throw new IllegalArgumentException("Profile and placement details are required");
        }

        // Check for duplicate before creating new placement
        boolean duplicateExists = profile.getPlacements().stream()
                .anyMatch(p -> p.getPositionName().equals(placementDTO.getPositionName()));

        if (duplicateExists) {
            throw new IllegalArgumentException(
                    String.format("Placement with name '%s' already exists", placementDTO.getPositionName())
            );
        }

        // Create and populate new placement
        Placement placement = new Placement();
        placement.setPositionName(placementDTO.getPositionName());
        placement.setRequiredSkills(placementDTO.getRequiredSkills());
        placement.setPositionDescription(placementDTO.getPositionDescription());
        placement.setPositionsAvailable(placementDTO.getPositionsAvailable());
        placement.setVisible(placementDTO.isVisible());

        // Add placement to profile and save
        profile.getPlacements().add(placement);
        placementRepo.save(placement);
        profileRepo.save(profile);

        log.info("Created new placement: {} for profile: {}", placementDTO.getPositionName(), profile.getProfileId());
        return placement;
    }

    public Placement editPlacement(PlacementDTO placement, Profile profile){
        try{
            AtomicReference<Placement> editedPlacement = new AtomicReference<>();
            profile.getPlacements().forEach(p -> {
                if (p.getPlacementId().equals(placement.getPlacementId())) {
                    p.setPositionName(placement.getPositionName());
                    p.setRequiredSkills(placement.getRequiredSkills());
                    p.setPositionDescription(placement.getPositionDescription());
                    p.setPositionsAvailable(placement.getPositionsAvailable());
                    p.setVisible(placement.isVisible());
                    placementRepo.save(p);
                    log.debug("Successfully edited placement {}", placement.getPositionName());
                }
            });
            return editedPlacement.get();

        } catch (Exception e){
            throw new IllegalArgumentException("Failed to edit placement");
        }
    }

    public void deletePlacement(PlacementDTO placement, Profile profile){
        profile.getPlacements().forEach(p ->{
            if(p.getPositionName().equals(placement.getPositionName())){
                placementRepo.delete(p);
                profileRepo.save(profile);
                log.debug("Successfully deleted placement {}", placement.getPositionName());
                return;
            }
            log.debug("Placement {} not found", placement.getPositionName());
        });
    }
}