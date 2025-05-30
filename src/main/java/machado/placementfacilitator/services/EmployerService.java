package machado.placementfacilitator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import machado.placementfacilitator.DTOs.EmployersDTOs.OfferPlacementDTO;
import machado.placementfacilitator.DTOs.EmployersDTOs.PlacementDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Service class handling employer-related operations including student management,
 * placement creation, editing, and offer management.
 */
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

    /*
     * Retrieves all student profiles from the system.
     * @return List of student profiles
     * @throws IllegalArgumentException if no students found or retrieval fails
     */
    public List<Profile> getAllStudents() {
        try {
            List<Account> accountlist = accountRepo.findAllByAccountType(Account.AccountType.STUDENT)
                    .orElse(null);
            if (accountlist == null) {
                throw new IllegalArgumentException("No students found");
            }
            return accountlist.stream()
                    .map(Account::getProfile)
                    .toList();

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve all students");
        }
    }

    /*
     * Adds a student to a placement's potential candidates list.
     * @param studentId The ID of the student to add
     * @param placementId The ID of the placement
     * @return The added student's profile
     * @throws IllegalArgumentException if student or placement not found
     */
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

    /*
     * Creates a new placement for an employer profile.
     * @param profile The employer's profile
     * @param placementDTO The placement details
     * @return The created placement
     * @throws IllegalArgumentException if inputs are invalid or duplicate exists
     */
    public Placement createPlacement(Profile profile, PlacementDTO placementDTO) {
        // Validate inputs
        log.info(">>> incerting placement: " + placementDTO.getPositionName() + " for profile: " + profile.getProfileId() + " <<<");
        if (profile == null || placementDTO == null) {
            log.error("Profile and placement details are required");
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

        log.info(">>> creating... <<<");
        // Create and populate new placement
        Placement placement = new Placement();
        placement.setPositionName(placementDTO.getPositionName());
        placement.setRequiredSkills(placementDTO.getRequiredSkills());
        placement.setPositionDescription(placementDTO.getPositionDescription());
        placement.setPositionsAvailable(placementDTO.getPositionsAvailable());
        placement.setVisible(placementDTO.isVisible());

        log.info(">>> created! <<<");
        // Add placement to profile and save
        log.info(">>> inserting... <<<");
        log.info(">>> Step 1<<<");
        placementRepo.save(placement);

        log.info(">>> Step 2<<<");
        profile.getPlacements().add(placement);

        log.info(">>> Step 3<<<");
        profileRepo.save(profile);
        log.info(">>> Inserted... <<<");
        log.info("Created new placement: {} for profile: {}", placementDTO.getPositionName(), profile.getProfileId());
        return placement;
    }


    /*
     * Edits an existing placement.
     * @param placement The updated placement details
     * @param profile The employer's profile
     * @return The edited placement
     * @throws IllegalArgumentException if edit operation fails
     */
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

    /*
     * Deletes a placement and its relationships.
     * @param placement The placement to delete
     * @param profile The employer's profile
     */
    public void deletePlacement(PlacementDTO placement, Profile profile){
        log.info(">>> deleting... <<<");
        profile.getPlacements().forEach(p ->{
            if(p.getPlacementId().equals(placement.getPlacementId())){
                System.out.println("Deleting placement: " + placement.getPositionName());
                placementRepo.deletePotentialCandidatesRelationships(p.getPlacementId());
                placementRepo.deleteProfilePlacementsRelationships(p.getPlacementId());
                placementRepo.deleteById(p.getPlacementId());
                log.debug("Successfully deleted placement {}", placement.getPositionName());
                return;
            }
            log.debug("Placement {} not found", placement.getPositionName());
        });
    }

    /*
     * Sends a placement offer to a student.
     * @param profileId The student's profile ID
     * @param placementId The placement ID
     * @throws IllegalStateException if account is not student or offer already exists
     * @throws RuntimeException if saving offer fails
     */
    public void sendPlacementOffer(Long profileId, Long placementId){
            Profile profile = profileRepo.findById(profileId).orElseThrow(()
                    -> new IllegalArgumentException("Profile not found"));
            Account account = accountRepo.findByProfile(profile).orElseThrow(()
                    -> new IllegalArgumentException("Account not found"));
            Placement placement = placementRepo.findById(placementId).orElseThrow(()
                    -> new IllegalArgumentException("Placement not found"));

        if (account.getAccountType() != Account.AccountType.STUDENT) {
            log.error("Account {} is not a student account", account.getAccountId());
            throw new IllegalStateException("Account is not a student account");
        }

        if (profile.getPendingOffers().contains(placement)) {
            log.error("Profile {} already has an offer for placement {}", profileId, placementId);
            throw new IllegalStateException("Profile already has an offer for this placement");
        }

        try {
            profile.getPendingOffers().add(placement);
            profileRepo.save(profile);
            log.info("Sent placement offer to profile {}", profileId);
        } catch (Exception e) {
            log.error("Failed to save placement offer", e);
            throw new RuntimeException("Failed to save placement offer", e);
        }

    }

    public void removeOffer(Long profileId, Long placementId){
        Profile profile = profileRepo.findById(profileId).orElseThrow(()
                -> new IllegalArgumentException("Profile not found"));
        Placement placement = placementRepo.findById(placementId).orElseThrow(()
                -> new IllegalArgumentException("Placement not found"));

        if(!profile.getPendingOffers().contains(placement)){
            throw new IllegalStateException("Profile does not have an offer for this placement");
        }
        try{
            profile.getPendingOffers().remove(placement);
            profileRepo.save(profile);
            log.info("Removed placement offer from profile {}", profileId);

        }catch(Exception e){
            log.error("Failed to remove placement offer", e);
            throw new RuntimeException("Failed to remove placement offer", e);
        }

    }
}