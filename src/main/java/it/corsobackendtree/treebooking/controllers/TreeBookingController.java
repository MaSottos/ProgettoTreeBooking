package it.corsobackendtree.treebooking.controllers;

import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.DAO.repositories.UserRepo;
import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.services.SecurityService;
import it.corsobackendtree.treebooking.services.UserService;
import it.corsobackendtree.treebooking.views.EventView;
import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TreeBookingController {
    @Autowired private CookieAuthRepo cookieAuthRepo;
    @Autowired private UserRepo userRepo;
    @PostMapping("/user")
    ResponseEntity<UserView> signUpUser(@RequestBody UserView userToSignUp,
                              @Autowired UserService userService,
                              @Autowired SecurityService securityService){
        //cod 201
        Optional<UserDAO> optUser = userRepo.findByUsername(userToSignUp.getUsername());
        if (optUser.isPresent()) return new ResponseEntity<>(null,
                                                            new HttpHeaders(), HttpStatus.BAD_REQUEST);
        UserModel model = userService.signUpUser(userToSignUp, securityService);
        UserDAO userDB = new UserDAO(model.getUsername(),
                model.getPassword(),
                model.getName(),
                model.getSurname(),
                model.getGender(),
                model.getBirthDate());
        //TODO: cookie
        userRepo.save(userDB);
        return new ResponseEntity<>(userToSignUp,userService.cookieGen(cookieAuthRepo,userDB,true), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    ResponseEntity<UserView> logIn(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password,
                                   @Autowired UserService userService,
                                   @Autowired SecurityService securityService){
        Optional<UserDAO> optUtenteTrovato = userRepo.findByUsername(username);
        if(optUtenteTrovato.isPresent()){
            UserDAO user = optUtenteTrovato.get();
            if(userService.checkPassword(password, user.getPassword(), securityService)){
                //cookie
                return new ResponseEntity<>(new UserView(user.getUsername(),"",user.getName(),
                                            user.getSurname(),
                                            user.getGender(),
                                            user.getBirthDate()),
                                            userService.cookieGen(cookieAuthRepo,user,false),
                                            HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

        }else{
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/events")
    List<EventView> getListEvents(){
        //cod200
        return null;
    }

    @PostMapping("/join/{eventid}")
    EventView joinEvent(@PathVariable("eventid") Long eventId){
        //cod201
        return null;
    }

    @PostMapping("/unjoin/{eventid}")
    EventView unjoinEvent(@PathVariable("eventid") Long eventId){
        //cod201
        return null;
    }

    @PostMapping("/event")
    EventView createEvent(@RequestBody EventView event){
        //cod201
        return null;
    }

    @GetMapping("/event/{eventid}")
    EventView getEventDetails(@PathVariable("eventid") Long eventId){
        //cod200
        return null;
    }

    @DeleteMapping("/event/{eventid}")
    EventView cancelEvent(@PathVariable("eventid") Long eventId){
        //cod200
        return null;
    }

    @GetMapping("user/events")
    List<EventView> getUserEvents(){
        //cod200
        return null;
    }


}
