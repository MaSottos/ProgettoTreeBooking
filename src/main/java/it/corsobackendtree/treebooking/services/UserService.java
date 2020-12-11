package it.corsobackendtree.treebooking.services;

import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    public UserModel signUpUser(UserView user, SecurityService service){
        String passwordCriptata = service.computeHash(user.getPassword());

        return new UserModel(user.getUsername(),
                passwordCriptata,
                user.getName(),
                user.getSurname(),
                user.getGender(),
                user.getBirthDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
    }

    public boolean checkPassword(String password, String passwordCriptata, SecurityService service){
        String passToCheck = service.computeHash(password);
        return passToCheck.equals(passwordCriptata);

    }
    public void cookieGen(CookieAuthRepo cookieAuthRepo, UserDAO user, boolean reg, HttpServletResponse response){
        String cookieValue = "";
        if (reg||user.getCookieAuthDAO() == null) {
            cookieValue = UUID.randomUUID().toString();
            CookieAuthDAO cookie = new CookieAuthDAO(cookieValue, user);
            cookieAuthRepo.save(cookie);
            user.setCookieAuthDAO(cookie);
        }else{
            cookieValue = user.getCookieAuthDAO().getCookieauth();
        }
        Cookie auth = new Cookie("auth", cookieValue);
        auth.setMaxAge(7*24*60*60); // 7 giorni
        response.addCookie(auth);
    }

    public CookieAuthDAO isLogged(String auth, CookieAuthRepo cookieAuthRepo){
        cookieAuthRepo.findByCookieauth(auth);
        Optional<CookieAuthDAO> optCookieAuthDAO = cookieAuthRepo.findByCookieauth(auth);
        return optCookieAuthDAO.orElseGet(null);
    }
}
