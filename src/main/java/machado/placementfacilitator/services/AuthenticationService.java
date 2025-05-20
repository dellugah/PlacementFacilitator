package machado.placementfacilitator.services;

import machado.placementfacilitator.DTOs.LoginUserDTO;
import machado.placementfacilitator.DTOs.RegisterUserDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.ProfileRepo;
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
    public Account authenticate(LoginUserDTO input) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
            System.out.println("Authenticated");
            return accountRepo.findByUsername(input.getUsername())
                    .orElseThrow();
        } catch (Exception e) {
            System.out.println("Could not authenticate");
            return null;
        }
    }
}