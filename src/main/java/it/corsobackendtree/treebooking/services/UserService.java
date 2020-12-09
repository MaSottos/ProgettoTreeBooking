package it.corsobackendtree.treebooking.services;

import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserModel signUpUser(UserView user, SecurityService service){
        String passwordCriptata = service.computeHash(user.getPassword());

        UserModel modello = new UserModel(user.getUsername(),
                passwordCriptata,
                user.getName(),
                user.getSurname(),
                user.getGender(),
                user.getBirthDate());

        return modello;
    }

    public boolean checkPassword(String password, String passwordCriptata, SecurityService service){
        String passToCheck = service.computeHash(password);
        if (passToCheck.equals(passwordCriptata)){
            return true;
        }else return false;

    }

}
