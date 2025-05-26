package machado.placementfacilitator.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "profile")
public class Profile {

    public enum TechnicalSkill {
        // Programming Languages
        JAVA, PYTHON, JAVASCRIPT, TYPESCRIPT, CSHARP, CPP, RUBY, PHP, SWIFT, KOTLIN, GO, RUST,

        // Web Frontend
        HTML, CSS, REACT, ANGULAR, VUE, JQUERY, BOOTSTRAP, SASS, WEBPACK, REDUX, NEXTJS,

        // Backend
        SPRING, NODEJS, DJANGO, FLASK, EXPRESS, ASPNET, LARAVEL, HIBERNATE, JPA,

        // Databases
        MYSQL, POSTGRESQL, MONGODB, ORACLE, SQLSERVER, REDIS, ELASTICSEARCH, CASSANDRA,

        // Cloud Platforms
        AWS, AZURE, GCP, DOCKER, KUBERNETES, OPENSHIFT, TERRAFORM, CLOUDFORMATION,

        // DevOps
        GIT, JENKINS, GITLAB_CI, GITHUB_ACTIONS, ANSIBLE, PUPPET, PROMETHEUS, GRAFANA,

        // Testing
        JUNIT, SELENIUM, JEST, CYPRESS, MOCKITO, TESTNG, JASMINE, POSTMAN,

        // Mobile Development
        ANDROID, IOS, REACT_NATIVE, FLUTTER, XAMARIN,

        // AI/ML
        TENSORFLOW, PYTORCH, SCIKIT_LEARN, PANDAS, NUMPY, OPENCV,

        // Security
        OAUTH, JWT, CRYPTOGRAPHY, PENETRATION_TESTING, SECURITY_AUDITING,

        // Architecture
        MICROSERVICES, REST_API, GRAPHQL, SOAP, EVENT_DRIVEN, SERVERLESS,

        // Methodologies
        AGILE, SCRUM, KANBAN, TDD, BDD, DDD,

        // Big Data
        HADOOP, SPARK, KAFKA, HIVE, PIG, STORM,

        // Operating Systems
        LINUX, WINDOWS, MACOS, UNIX,

        // Networking
        TCP_IP, DNS, HTTP, HTTPS, SSL_TLS, LOAD_BALANCING,

        // Project Management Tools
        JIRA, CONFLUENCE, TRELLO, ASANA, MS_PROJECT,

        // IDE and Tools
        INTELLIJ, VSCODE, ECLIPSE, NETBEANS, SUBLIME, VIM
    }

    //Database Keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long profileId;

    //Profile Info
    @Column(unique = true, length = 255)
    private String bio;
    private String email;
    private String linkOne;
    private String linkTwo;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<TechnicalSkill> skills;
    private byte[] profilePhoto;

    //Employer Restricted
    @OneToMany(fetch = FetchType.EAGER)
    private List<Placement> placements;
    private String companyName;

    //Student Restricted;
    private String firstName;
    private String lastName;
    private boolean visible = true;
    private Boolean domestic;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "placement_offers",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "placement_id")
    )
    private List<Placement> pendingOffers;

    @OneToOne
    @JoinColumn(name = "acceptedPlacement_id")
    private Placement acceptedPlacement;

    //files
    private byte[] file;

}
