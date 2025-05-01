package machado.placementfacilitator.DTOs;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String username;
    private String password;
    private String accountType;
}
