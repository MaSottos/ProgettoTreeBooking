package it.corsobackendtree.treebooking.services;

import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    public HttpHeaders cookieGen(CookieAuthRepo cookieAuthRepo, UserDAO user, boolean reg){
        if (reg||user.getCookieAuthDAO() == null) {
            String cookieValue = UUID.randomUUID().toString();
            CookieAuthDAO cookie = new CookieAuthDAO(cookieValue, user);
            cookieAuthRepo.save(cookie);
            user.setCookieAuthDAO(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "auth=" + cookieValue + "; Max-Age=604800; Path=/; Secure; SameSite=None");
            return headers;
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "auth=" + user.getCookieAuthDAO() + "; Max-Age=604800; Path=/; Secure; SameSite=None");
            return headers;
        }

    }

}
