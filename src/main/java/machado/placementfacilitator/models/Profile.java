package machado.placementfacilitator.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Long profile_id;

    //Profile Info
    @Column(unique = true, length = 255, nullable = true)
    private String bio;
    private String email;
    private String linkedIn_link;
    private String gitHub_link;

    @Enumerated(EnumType.STRING)
    private List<TechnicalSkill> skills;

    private Boolean domestic;
    private byte[] photo;

    //Employer Restricted
    @OneToMany
    @JoinColumn(name = "placement_id")
    private List<Placement> placements;
    private String company_name;

    //Student Restricted;
    private String first_name;
    private String last_name;

    //files
    private byte[] file; // RESUME OR COMPANY PRE-REQUISITES



}
