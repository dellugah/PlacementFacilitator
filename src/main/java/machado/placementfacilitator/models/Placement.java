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
    Long placement_id;

    private String position_name;
    private String position_description;
    private List<String> required_skills;
    private int positions_available;
    boolean visible;

    @ManyToMany
    @JoinTable(
            name = "placement_student",
            joinColumns = @JoinColumn(name = "placement_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Profile> profiles;
}
