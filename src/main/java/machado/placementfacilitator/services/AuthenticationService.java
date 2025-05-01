package machado.placementfacilitator.services;

import machado.placementfacilitator.DTOs.LoginUserDto;
import machado.placementfacilitator.DTOs.RegisterUserDto;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            AccountRepo accountRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
    }


    //Initializes a new Account
    public Account signup(RegisterUserDto input) {
        Account account = new Account();
        Profile profile = new Profile();

        account.setUsername(input.getUsername());
        account.setPassword(passwordEncoder.encode(input.getPassword()));
        account.setAccount_type(Account.account_type.valueOf(input.getAccountType()));

        account.setProfile(profile);

        return accountRepo.save(account);
    }

    public Account authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return accountRepo.findByUsername(input.getUsername())
                .orElseThrow();
    }
}