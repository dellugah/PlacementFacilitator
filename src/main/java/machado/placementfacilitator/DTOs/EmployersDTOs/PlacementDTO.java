package machado.placementfacilitator.DTOs.EmployersDTOs;

import lombok.Data;
import machado.placementfacilitator.models.Profile;

import java.util.List;

@Data
public class PlacementDTO {
    private Long placementId;
    private String positionName;
    private String positionDescription;
    private int positionsAvailable;
    private List<Profile.TechnicalSkill> requiredSkills;
    boolean visible;
}
