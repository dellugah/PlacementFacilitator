package machado.placementfacilitator.services;

import machado.placementfacilitator.DTOs.LoginUserDto;
import machado.placementfacilitator.DTOs.RegisterUserDto;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Placement;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.PlacementRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepo profileRepo;
    private final PlacementRepo placementRepo;

    public AuthenticationService(
            AccountRepo accountRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ProfileRepo profileRepo, PlacementRepo placementRepo) {
        this.authenticationManager = authenticationManager;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.profileRepo = profileRepo;
        this.placementRepo = placementRepo;
    }


    //Initializes a new Account
    public Account signup(RegisterUserDto input) {
        Profile profile = new Profile();
        profile = profileRepo.save(profile);
        Account account = new Account(profile);

        account.setUsername(input.getUsername());
        account.setPassword(passwordEncoder.encode(input.getPassword()));
        account.setAccount_type(Account.account_type.valueOf(input.getAccountType()));


        return accountRepo.save(account);
    }

    //Authenticate Account
    public Account authenticate(LoginUserDto input) {
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