package machado.placementfacilitator.controllers;

import machado.placementfacilitator.DTOs.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import machado.placementfacilitator.DTOs.LoginUserDto;
import machado.placementfacilitator.DTOs.RegisterUserDto;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.services.AuthenticationService;
import machado.placementfacilitator.services.JwtService;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/test")
    public String test(){
        return "auth";
    }

    /**
     * Handles user registration (sign-up) requests.
     * Creates a new user account and generates a JWT token for immediate authentication.
     *
     * @param registerUserDto DTO containing user registration information (username, password, etc.)
     * @return ResponseEntity containing LoginResponse with JWT token and expiration time
     */
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterUserDto registerUserDto) {
        System.out.println(registerUserDto);
        if(registerUserDto != null && (registerUserDto.getPassword().length() >= 8 || registerUserDto.getUsername().length() >= 8)) {
            try{
                Account registeredAccount = authenticationService.signup(registerUserDto);
                String jwtToken = jwtService.generateToken(registeredAccount);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(jwtToken);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());
                switch (registerUserDto.getAccountType()){
                    case "STUDENT": loginResponse.setHomePage("student"); break;
                    case "EMPLOYER": loginResponse.setHomePage("employer"); break;
                    case "ADMIN": loginResponse.setHomePage("adminInterface"); break;
                    default: return ResponseEntity.badRequest().build();
                }
                return ResponseEntity.ok(loginResponse);
            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles user authentication (login) requests.
     * Validates user credentials and generates a JWT token upon successful authentication.
     *
     * @param loginUserDto DTO containing user login credentials (username and password)
     * @return ResponseEntity containing LoginResponse with JWT token and expiration time if authentication successful,
     * or BadRequest if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        System.out.println(loginUserDto);
        Account authenticatedUser = authenticationService.authenticate(loginUserDto);

        if (authenticatedUser == null) {
            return ResponseEntity.badRequest().build();
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        switch (authenticatedUser.getAccountType().toString()){
            case "STUDENT": loginResponse.setHomePage("/student"); break;
            case "EMPLOYER": loginResponse.setHomePage("/employer"); break;
            case "ADMIN": loginResponse.setHomePage("/admin-interface"); break;
            default: return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){
        return ResponseEntity.ok().build();
    }
}
