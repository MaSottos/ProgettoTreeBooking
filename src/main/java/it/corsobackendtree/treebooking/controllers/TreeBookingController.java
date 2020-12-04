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


}
