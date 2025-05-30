package machado.placementfacilitator.services;

import machado.placementfacilitator.DTOs.LoginUserDTO;
import machado.placementfacilitator.DTOs.RegisterUserDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepo profileRepo;

    public AuthenticationService(
            AccountRepo accountRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ProfileRepo profileRepo) {
        this.authenticationManager = authenticationManager;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.profileRepo = profileRepo;
    }


    //Initializes a new Account
    public Account signup(RegisterUserDTO input) {
        //create an empty profile on the database
        Profile profile = new Profile();
        profile.setDomestic(input.isDomestic());
        profile = profileRepo.save(profile);
        //set account credentials with passed account info
        //TODO Expand account/profile info on first save
        Account account = new Account(profile);
        account.setAccountType(Account.AccountType.valueOf(input.getAccountType()));
        account.setPassword(passwordEncoder.encode(input.getPassword()));//Encrypt password
        account.setUsername(input.getUsername());
        profile.setVisible(true);
        if(input.getAccountType().equals("EMPLOYER")){
            profile.setCompanyName(input.getCompanyName());
        }else{
            profile.setFirstName(input.getFirstName());
            profile.setLastName(input.getLastName());
        }

        profile.setEmail(input.getEmail());



        //save an account onto the database
        profileRepo.save(profile);
        return accountRepo.save(account);
    }

    //Authenticate Account
    public ResponseEntity<Account> authenticate(LoginUserDTO input) {
        try{
            System.out.println("Authenticating");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
            System.out.println("Authenticated");
            return accountRepo.findByUsername(input.getUsername())//Return account if authenticated
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new Exception("User not found"));//Throw exception if not authenticated
        } catch (Exception e) {
            System.out.println("Could not authenticate");
            throw new IllegalArgumentException("Invalid username/password supplied");
        }
    }
}