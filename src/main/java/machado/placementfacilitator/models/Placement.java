package machado.placementfacilitator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "placement")
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long placementId;

    private String position_name;
    private String positionDescription;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Profile.TechnicalSkill> requiredSkills;
    private int positionsAvailable;
    boolean visible;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Profile> interestedStudents;
}