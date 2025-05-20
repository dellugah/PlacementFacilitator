package machado.placementfacilitator.DTOs;

import jakarta.persistence.*;
import lombok.Data;
import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;

import java.util.List;

@Data
public class EditProfileDTO {
    Long profileId;
    private String bio;
    private String linkOne;
    private String linkTwo;
    private List<Profile.TechnicalSkill> skills;
    private List<Placement> placements;
    private String companyName;
    private String firstName;
    private String lastName;
    private byte[] file;
}
