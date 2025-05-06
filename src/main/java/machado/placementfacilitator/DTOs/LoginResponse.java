package machado.placementfacilitator.DTOs;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private long expiresIn;

    private String homePage;
}
