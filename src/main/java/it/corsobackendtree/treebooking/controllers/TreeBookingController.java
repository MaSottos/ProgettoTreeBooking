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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*")
public class TreeBookingController {
    @Autowired
    private CookieAuthRepo cookieAuthRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private BookingRepo bookingRepo;

    private Logger logger = LoggerFactory.getLogger(TreeBookingController.class);

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "CIAO!!!";
    }

    @PostMapping("/user")
    ResponseEntity<UserViewNoPsw> signUpUser(@RequestBody UserView userToSignUp,
                                             @Autowired UserService userService,
                                             @Autowired SecurityService securityService,
                                             HttpServletResponse response) {
        logger.info("SONO QUI");
        try{
            Optional<UserDAO> optUser = userRepo.findByUsername(userToSignUp.getUsername());
            if (optUser.isPresent()) return ResponseEntity.badRequest().body(null);
            UserModel model = userService.signUpUser(userToSignUp, securityService);
            UserDAO userDB = new UserDAO(model.getUsername(),
                    model.getPassword(),
                    model.getName(),
                    model.getSurname(),
                    model.getGender(),
                    model.getBirthDate(),
                    model.getSalt());
            userRepo.save(userDB);
            userService.cookieGen(cookieAuthRepo, userDB, true, response);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserViewNoPsw(userDB.getUsername(),
                    userDB.getName(), userDB.getSurname(), userDB.getBirthDate(),
                    userDB.getGender()));
        }catch (Exception e){
            logger.info("CLASS: "+e.getClass().getName());
            logger.info("MESSAGE: "+e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/login")
    ResponseEntity<UserViewNoPsw> logIn(@RequestParam(name = "username") String username,
                                        @RequestParam(name = "password") String password,
                                        @Autowired UserService userService,
                                        @Autowired SecurityService securityService,
                                        HttpServletResponse response) {
        Optional<UserDAO> optUtenteTrovato = userRepo.findByUsername(username);
        if (optUtenteTrovato.isPresent()) {
            UserDAO user = optUtenteTrovato.get();
            if (userService.checkPassword(password, user.getPassword(), securityService, user.getSalt())) {
                userService.cookieGen(cookieAuthRepo, user, false, response);
                return ResponseEntity.ok(new UserViewNoPsw(user.getUsername(),
                                user.getName(),
                                user.getSurname(),
                                user.getBirthDate(),
                                user.getGender()));
            } else {
                return ResponseEntity.badRequest().body(null);
            }

        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/events")
    ResponseEntity<List<EventView>> getActiveEvents(@CookieValue(value = "auth", defaultValue = "") String auth,
                                                    @Autowired UserService userService,
                                                    @Autowired EventService eventService) {
        /*Controllo autenticazione utente*/
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDAO user = cookieAuthDAO.getUser();
        List<EventDAO> listEventDAO = eventRepo.findByAvailableCapacityAndDatetime(LocalDateTime.now());
        List<EventView> response = listEventDAO.stream().filter(eventDAO -> {
            boolean match = eventDAO.getEventReservations().stream().anyMatch(b -> b.getUser().equals(user));
            return !match;
        }).map(eventDAO -> eventService.getEventView(eventDAO, user)).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join/{eventid}")
    ResponseEntity<EventView> joinEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                                        @PathVariable("eventid") UUID eventId,
                                        @Autowired UserService userService,
                                        @Autowired EventService eventService) {
        System.out.println(eventId);

        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if (optEvent.isPresent()) {
            EventDAO eventDAO = optEvent.get();
            eventDAO.addUserReservation(user);
            eventRepo.save(eventDAO);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.getEventView(eventDAO, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/unjoin/{eventid}")
    ResponseEntity<EventView> unjoinEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                                          @PathVariable("eventid") UUID eventId,
                                          @Autowired UserService userService,
                                          @Autowired EventService eventService) {
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if (optEvent.isPresent()) {
            EventDAO eventDAO = optEvent.get();
            if (eventDAO.getOwner().equals(user)) return ResponseEntity.badRequest().body(null);

            BookingId bookingId = new BookingId(user, eventDAO);
            Optional<BookingDAO> optBookingDAO = bookingRepo.findById(bookingId);

            if (optBookingDAO.isPresent()) {
                eventDAO.removeUserReservation(optBookingDAO.get());
                eventRepo.save(eventDAO);
                return ResponseEntity.status(HttpStatus.CREATED).body(eventService.getEventView(eventDAO, user));
            } else return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/event")
    ResponseEntity<EventView> createEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                                          @RequestBody EventView event,
                                          @Autowired UserService userService) {
        System.out.println("EVENTO:    " + event.toString());
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO owner = cookieAuthDAO.getUser();
        EventDAO eventDAO = new EventDAO(event.getName(), event.getDate(), event.getPlace(), event.getCapacity(), owner);
        eventDAO.addUserReservation(owner);
        eventRepo.save(eventDAO);
        //*******************
        return ResponseEntity.status(HttpStatus.CREATED).body( new EventView(
                eventDAO.getId(), event.getOwned(), event.getJoined(), event.getName(),
                event.getDate(), event.getPlace(), event.getCapacity()) );
    }

    @GetMapping("/event/{eventid}")
    ResponseEntity<EventView> getEventDetails(@CookieValue(value = "auth", defaultValue = "") String auth,
                                              @PathVariable("eventid") UUID eventId,
                                              @Autowired UserService userService,
                                              @Autowired EventService eventService) {
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO user = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if (optEvent.isPresent()) {
            EventDAO eventDAO = optEvent.get();
            return ResponseEntity.ok(eventService.getEventView(eventDAO, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/event/{eventid}")
    ResponseEntity<EventView> cancelEvent(@CookieValue(value = "auth", defaultValue = "") String auth,
                                          @PathVariable("eventid") UUID eventId,
                                          @Autowired UserService userService,
                                          @Autowired EventService eventService) {
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO owner = cookieAuthDAO.getUser();
        Optional<EventDAO> optEvent = eventRepo.findById(eventId);
        if (optEvent.isPresent()) {
            EventDAO eventDAO = optEvent.get();
            if (eventDAO.getOwner().equals(owner)) {
                eventRepo.deleteById(eventId);
                return ResponseEntity.ok(eventService.getEventView(eventDAO, owner));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("user/events")
    ResponseEntity<List<EventView>> getUserEvents(@CookieValue(value = "auth", defaultValue = "") String auth,
                                                  @Autowired UserService userService,
                                                  @Autowired EventService eventService) {
        CookieAuthDAO cookieAuthDAO = userService.isLogged(auth, cookieAuthRepo);
        if (cookieAuthDAO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDAO user = cookieAuthDAO.getUser();
        List<EventDAO> eventsDAO = eventRepo.findByDatetimeAfter(LocalDateTime.now());

        List<EventView> events = eventsDAO.stream().filter(e -> e.getJoined(user)).map(
                e -> eventService.getEventView(e, user))
                .collect(Collectors.toList());
        logger.info("EVENTS: "+events.toString());
        return ResponseEntity.ok(events);
    }
}
