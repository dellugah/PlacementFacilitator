package machado.placementfacilitator.services;

import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    ProfileRepo profileRepo;
    PlacementRepo placementRepo;
    StudentService(ProfileRepo profileRepo,
                   PlacementRepo placementRepo){
        this.profileRepo = profileRepo;
    }

    public Profile acceptPlacement(Profile profile, Long placementId){
        Placement placement = placementRepo.findById(placementId).orElseThrow(()
                -> new IllegalArgumentException("Placement not found"));
        profile.setAcceptedPlacement(placement);
        return profileRepo.save(profile);
    }
}
