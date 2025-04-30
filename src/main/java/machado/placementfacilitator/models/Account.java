package machado.placementfacilitator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
public class Account {

    public enum account_type{
        ADMIN, STUDENT, EMPLOYER //DO NOT CHANGE ORDER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account_id;
    private String username;
    private char[] password;

    @Enumerated(EnumType.STRING)
    private account_type account_type;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
