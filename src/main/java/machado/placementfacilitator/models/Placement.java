package machado.placementfacilitator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "placement")
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long placementId;

    @Column(name = "position_name", nullable = false)
    private String positionName;

    @Column(name = "position_description")
    private String positionDescription;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Profile.TechnicalSkill> requiredSkills;

    @Column(name = "positions_available")
    private int positionsAvailable;

    @Column(name = "visible")
    boolean visible;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "potential_candidates",
            joinColumns = @JoinColumn(name = "placement_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> potentialCandidates = new ArrayList<>();

}