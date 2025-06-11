package machado.placementfacilitator.services;

import machado.placementfacilitator.DTOs.EditProfileDTO;
import machado.placementfacilitator.models.Account;
import machado.placementfacilitator.models.Profile;
import machado.placementfacilitator.repos.AccountRepo;
import machado.placementfacilitator.repos.ProfileRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServices {
    private final AccountRepo accountRepo;
    private final ProfileRepo profileRepo;

    public AccountServices(AccountRepo accountRepo, ProfileRepo profileRepo) {
        this.profileRepo = profileRepo;
        this.accountRepo = accountRepo;
    }

    public List<Profile> findAccountByAccountType(String type) {
        Account.AccountType accountType = Account.AccountType.valueOf(type);
        List<Profile> users = new ArrayList<>();
        Optional<List<Account>> accounts = accountRepo.findAllByAccountType(accountType);
        if (accounts.isEmpty()) {
            return users;
        }

        accounts.get().forEach(account -> {
            if (account.getProfile().isVisible()) {
                users.add(account.getProfile());
            }
        });

        return users;
    }

    public Profile editProfile(Profile profileToBeEdited, EditProfileDTO editedProfile){

        try{
            if(!Objects.equals(profileToBeEdited.getFirstName(), editedProfile.getFirstName())
                    && !editedProfile.getFirstName().isBlank()) {
                profileToBeEdited.setFirstName(editedProfile.getFirstName());
            }

            if(!Objects.equals(profileToBeEdited.getLastName(), editedProfile.getLastName())
                    && !editedProfile.getLastName().isBlank()){
                profileToBeEdited.setLastName(editedProfile.getLastName());
            }

            //TODO decide if email is editable
//            if(!Objects.equals(profileToBeEdited.getEmail(), editedProfile.getEmail())){
//                profileToBeEdited.setEmail(editedProfile.getEmail());
//                System.out.println("Email edited");
//            }
            if(!Objects.equals(profileToBeEdited.getCompanyName(), editedProfile.getCompanyName())
                    && !editedProfile.getCompanyName().isBlank()){
                profileToBeEdited.setCompanyName(editedProfile.getCompanyName());
            }

            if(!Objects.equals(profileToBeEdited.getLinkOne(), editedProfile.getLinkOne())
                    && !editedProfile.getLinkOne().isBlank()){
                profileToBeEdited.setLinkOne(editedProfile.getLinkOne());
            }

            if(!Objects.equals(profileToBeEdited.getLinkTwo(), editedProfile.getLinkTwo())
                    && !editedProfile.getLinkTwo().isBlank()){
                profileToBeEdited.setLinkTwo(editedProfile.getLinkTwo());
            }
            //TODO implement file logic
//            if(!Arrays.equals(profileToBeEdited.getProfilePhoto(), editedProfile.getProfilePhoto())){
//                profileToBeEdited.setProfilePhoto(editedProfile.getProfilePhoto());
//                System.out.println("Profile photo edited");
//            }
            if(!Objects.equals(profileToBeEdited.getBio(), editedProfile.getBio())
                    && !editedProfile.getBio().isBlank()){
                profileToBeEdited.setBio(editedProfile.getBio());
            }

            if(!Objects.equals(profileToBeEdited.getSkills(), editedProfile.getSkills())
                    && !editedProfile.getSkills().isEmpty()){
                profileToBeEdited.getSkills().clear();
                editedProfile.getSkills().forEach(skill -> {
                    profileToBeEdited.getSkills().add(skill);
                });
            }
            return profileRepo.save(profileToBeEdited);
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to edit profile");
        }
    }
}
