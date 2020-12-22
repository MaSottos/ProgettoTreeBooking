package it.corsobackendtree.treebooking.services;

import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.DAO.repositories.UserRepo;
import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.views.UserView;
import it.corsobackendtree.treebooking.views.UserViewNoPsw;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    private static Random random = new Random();
    public UserDAO signUpUser(UserView user, SecurityService service, UserRepo userRepo,
                              CookieAuthRepo cookieAuthRepo, HttpServletResponse response){
        Optional<UserDAO> optUser = userRepo.findByUsername(user.getUsername());
        if (optUser.isPresent()) return null;

        Integer salt =random.nextInt(51);
        String passwordCriptata = service.computeHash(user.getPassword(),salt);

        UserModel userModel = new UserModel(user.getUsername(),
                passwordCriptata,
                user.getName(),
                user.getSurname(),
                user.getGender(),
                user.getBirthDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
                salt);
        UserDAO userDAO = new UserDAO(userModel.getUsername(),
                userModel.getPassword(),
                userModel.getName(),
                userModel.getSurname(),
                userModel.getGender(),
                userModel.getBirthDate(),
                userModel.getSalt());
        userRepo.save(userDAO);
        cookieGen(cookieAuthRepo, userDAO, true, response);

        return userDAO;
    }

    public UserDAO loginUser(UserRepo userRepo,
                             CookieAuthRepo cookieAuthRepo,
                             String username,
                             String password,
                             SecurityService securityService,
                             HttpServletResponse response){
        Optional<UserDAO> optUtenteTrovato = userRepo.findByUsername(username);
        if (optUtenteTrovato.isPresent()) {
            UserDAO user = optUtenteTrovato.get();
            if (checkPassword(password, user.getPassword(), securityService, user.getSalt())) {
                cookieGen(cookieAuthRepo, user, false, response);
                return user;
            } else {
                return null;//TODO:solleva eccezione
            }
        } else {
            return null;//TODO:solleva eccezione
        }
    }

    public boolean checkPassword(String password, String passwordCriptata, SecurityService service, Integer salt){
        String passToCheck = service.computeHash(password, salt);
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
        /*Cookie auth = new Cookie("auth", cookieValue);
        auth.setMaxAge(7*24*60*60); // 7 giorni
        response.addCookie(auth);*/
        response.addHeader(HttpHeaders.SET_COOKIE,
                ResponseCookie.from("auth", cookieValue).maxAge(7*24*60*60).secure(true).sameSite("None").build().toString());
    }

    public CookieAuthDAO isLogged(String auth, CookieAuthRepo cookieAuthRepo){
        Optional<CookieAuthDAO> optCookieAuthDAO = cookieAuthRepo.findByCookieauth(auth);
        return optCookieAuthDAO.orElseGet(null);
    }
}
