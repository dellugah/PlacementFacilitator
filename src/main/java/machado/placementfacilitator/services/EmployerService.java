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

@Service
@Slf4j
@Transactional
public class EmployerService {
    private final AccountRepo accountRepo;
    private final ProfileRepo profileRepo;
    private final PlacementRepo placementRepo;

    public EmployerService(AccountRepo accountRepo,
                           ProfileRepo profileRepo, PlacementRepo placementRepo) {
        this.accountRepo = accountRepo;
        this.profileRepo = profileRepo;
        this.placementRepo = placementRepo;
    }

    public boolean addStudentToPlacement(Long studentId, Long placementId) {
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
                log.debug("Student {} is already in this placement {}", studentId, placementId);
                return false;
            }

            placement.getPotentialCandidates().add(student);
            placementRepo.save(placement);

            log.debug("Successfully added student {} to placement {}", studentId, placementId);
            return true;

        } catch (Exception e) {
            log.error("Failed to add student {} to placement {}: {}",
                    studentId, placementId, e.getMessage());
            return false;
        }
    }

    public void createPlacement(Profile profile, PlacementDTO placementDTO) {
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

        log.info("Created new placement: {} for profile: {}",
                placementDTO.getPositionName(), profile.getProfileId());
    }

    public void editPlacement(PlacementDTO placement, Profile profile){
        profile.getPlacements().forEach(p ->{
            if(p.getPositionName().equals(placement.getPositionName())){
                p.setPositionName(placement.getPositionName());
                p.setRequiredSkills(placement.getRequiredSkills());
                p.setPositionDescription(placement.getPositionDescription());
                p.setPositionsAvailable(placement.getPositionsAvailable());
                p.setVisible(placement.isVisible());
                placementRepo.save(p);
                profileRepo.save(profile);
                log.debug("Successfully edited placement {}", placement.getPositionName());
                return;
            }
        });
        log.debug("Placement {} not found", placement.getPositionName());
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