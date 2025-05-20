package machado.placementfacilitator.DTOs;

import lombok.Data;

@Data
public class RegisterUserDTO {

    private String username;

    private String password;

    //Student Restricted;
    private String firstName;
    private String lastName;

    //Employer Restricted
    private String companyName;

    private String email;

    private String accountType;






}
