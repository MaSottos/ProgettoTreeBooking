package it.corsobackendtree.treebooking.controllers;

import it.corsobackendtree.treebooking.views.EventView;
import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TreeBookingController {
    @PostMapping("/user")
    UserView signUpUser(@RequestBody UserView userToSignUp){

        //cod 201
        return null;
    }


    @GetMapping("/login")
    UserView logIn(@RequestParam(name = "username") String username,@RequestParam(name = "password") String password){

        //cod 200
        return null;
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
