package machado.placementfacilitator.services;

import jakarta.transaction.Transactional;
import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class StudentService {

    ProfileRepo profileRepo;
    PlacementRepo placementRepo;
    StudentService(ProfileRepo profileRepo,
                   PlacementRepo placementRepo){
        this.placementRepo = placementRepo;
        this.profileRepo = profileRepo;
    }

    public Profile acceptPlacement(Profile profile, Long placementId){
        try{
            log.info("finding placement with id: {}", placementId);
            Placement placement = placementRepo.findById(placementId).orElseThrow(()
                    -> new IllegalArgumentException("Placement not found"));

            log.info("inserting placement into profile: {}", placementId);
            profile.setAcceptedPlacement(placement);

            log.info("removing placement from pending offers: {}", placementId);
            profile.getPendingOffers().clear();

            log.info("setting profile visibility to false");
            profile.setVisible(false);

            log.info("saving profile: {}", profile.getProfileId());
            return profileRepo.save(profile);

        } catch (Exception ex){
            System.out.println(" >>> Exception Occured");
            throw new IllegalArgumentException(ex);
        }
    }

    public Profile rejectPlacement(Profile profile, Long placementId){
        try{
            log.info("finding placement with id: {}", placementId);
            Placement placement = placementRepo.findById(placementId).orElseThrow(()
                    -> new IllegalArgumentException("Placement not found"));
            log.info("removing placement from pending offers: {}", placementId);
            profile.getPendingOffers().remove(placement);
            log.info("saving profile: {}", profile.getProfileId());
            profileRepo.save(profile);
            return profile;
        }
        catch (Exception ex){
            System.out.println(" >>> Exception Occured");
            throw new IllegalArgumentException(ex);
        }
    }
}
