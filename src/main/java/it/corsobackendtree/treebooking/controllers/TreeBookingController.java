package it.corsobackendtree.treebooking.controllers;

import it.corsobackendtree.treebooking.DAO.BookingId;
import it.corsobackendtree.treebooking.DAO.entities.BookingDAO;
import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.DAO.repositories.BookingRepo;
import it.corsobackendtree.treebooking.DAO.repositories.CookieAuthRepo;
import it.corsobackendtree.treebooking.DAO.repositories.EventRepo;
import it.corsobackendtree.treebooking.DAO.repositories.UserRepo;
import it.corsobackendtree.treebooking.models.UserModel;
import it.corsobackendtree.treebooking.services.EventService;
import it.corsobackendtree.treebooking.services.SecurityService;
import it.corsobackendtree.treebooking.services.UserService;
import it.corsobackendtree.treebooking.views.EventView;
import it.corsobackendtree.treebooking.views.UserView;
import it.corsobackendtree.treebooking.views.UserViewNoPsw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class TreeBookingController {
    @Autowired private CookieAuthRepo cookieAuthRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private EventRepo eventRepo;
    @Autowired private BookingRepo bookingRepo;

    @PostMapping("/user")
    ResponseEntity<UserViewNoPsw> signUpUser(@RequestBody UserView userToSignUp,
                                        @Autowired UserService userService,
                                        @Autowired SecurityService securityService,
                                        HttpServletResponse response){
        Optional<UserDAO> optUser = userRepo.findByUsername(userToSignUp.getUsername());
        if (optUser.isPresent()) return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        UserModel model = userService.signUpUser(userToSignUp, securityService);
        UserDAO userDB = new UserDAO(model.getUsername(),
                model.getPassword(),
                model.getName(),
                model.getSurname(),
                model.getGender(),
                model.getBirthDate());
        userRepo.save(userDB);
        userService.cookieGen(cookieAuthRepo,userDB,true, response);
        return new ResponseEntity<>( new UserViewNoPsw(userToSignUp.getUsername(),
                userToSignUp.getName(), userToSignUp.getSurname(), userToSignUp.getBirthDate(),
                userToSignUp.getGender()) , HttpStatus.CREATED);
    }

    @GetMapping("/login")
    ResponseEntity<UserViewNoPsw> logIn(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password,
                                   @Autowired UserService userService,
                                   @Autowired SecurityService securityService,
                                   HttpServletResponse response){
        Optional<UserDAO> optUtenteTrovato = userRepo.findByUsername(username);
        if(optUtenteTrovato.isPresent()){
            UserDAO user = optUtenteTrovato.get();
            if(userService.checkPassword(password, user.getPassword(), securityService)){
                userService.cookieGen(cookieAuthRepo,user,false,response);
                return new ResponseEntity<>(new UserViewNoPsw(user.getUsername(),
                                            user.getName(),
                                            user.getSurname(),
                                            user.getBirthDate(),
                                            user.getGender()),
                                            HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/events")
    ResponseEntity<List<EventView>> getListEvents(@CookieValue(value = "auth", defaultValue = "") String auth,
                                                  @Autowired UserService userService,
                                                  @Autowired EventService eventService){
        /*Controllo autenticazione utente*/
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

        UserDAO user = cookieAuthDAO.getUser();
        List<EventDAO> listEventDAO = eventRepo.findByCapacityGreaterThanZero(LocalDateTime.now());
        List<EventView> response = listEventDAO.stream().filter(eventDAO -> {
           boolean match = eventDAO.getEventReservations().stream().anyMatch(b->b.getUser().equals(user));
           return !match;
        }).map(eventDAO -> eventService.getEventView(eventDAO, user)).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/join/{eventid}")
    ResponseEntity<EventView> joinEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                        @PathVariable("eventid") UUID eventId,
                        @Autowired UserService userService,
                        @Autowired EventService eventService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if(optEvent.isPresent()){
            EventDAO eventDAO = optEvent.get();
            BookingDAO bookingDAO = new BookingDAO(user, eventDAO);
            bookingRepo.save(bookingDAO);
            return new ResponseEntity<>(eventService.getEventView(eventDAO, user),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/unjoin/{eventid}")
    ResponseEntity<EventView> unjoinEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                          @PathVariable("eventid") UUID eventId,
                          @Autowired UserService userService,
                          @Autowired EventService eventService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if(optEvent.isPresent()){
            EventDAO eventDAO = optEvent.get();
            if(eventDAO.getOwner().equals(user)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            BookingId bookingId = new BookingId(user, eventDAO);
            bookingRepo.deleteById(bookingId);
            return new ResponseEntity<>(eventService.getEventView(eventDAO, user),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/event")
    ResponseEntity<EventView> createEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                          @RequestBody EventView event,
                          @Autowired UserService userService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO owner = cookieAuthDAO.getUser();
        EventDAO eventDAO = new EventDAO(event.getName(), event.getDate(), event.getPlace(), event.getCapacity(), owner);
        eventDAO.addOwnerToReservations(owner);
        eventRepo.save(eventDAO);
        //*******************
        return new ResponseEntity<>( event, HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventid}")
    ResponseEntity<EventView> getEventDetails(@CookieValue(value = "auth", defaultValue = "") String auth,
                              @PathVariable("eventid") UUID eventId,
                              @Autowired UserService userService,
                              @Autowired EventService eventService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if(optEvent.isPresent()){
            EventDAO eventDAO = optEvent.get();
            return new ResponseEntity<>(eventService.getEventView(eventDAO,user),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/event/{eventid}")
    ResponseEntity<EventView> cancelEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                          @PathVariable("eventid") UUID eventId,
                          @Autowired UserService userService,
                          @Autowired EventService eventService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO owner = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if(optEvent.isPresent()){
            EventDAO eventDAO = optEvent.get();
            if(eventDAO.getOwner().equals(owner)){
                eventRepo.deleteById(eventId);
                return new ResponseEntity<>(eventService.getEventView(eventDAO,owner), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("user/events")
    ResponseEntity<List<EventView>> getUserEvents(@CookieValue(value = "auth", defaultValue = "") String auth,
                                  @Autowired UserService userService,
                                  @Autowired EventService eventService){
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth,cookieAuthRepo);
        if(cookieAuthDAO == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDAO user = cookieAuthDAO.getUser();
        List<EventDAO> eventsDAO = eventRepo.findByDatetimeAfter(LocalDateTime.now());

        List<EventView> events = eventsDAO.stream().map(
                e -> eventService.getEventView(e, user))
                .collect(Collectors.toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
