package it.corsobackendtree.treebooking.controllers;

import it.corsobackendtree.treebooking.views.UserView;
import org.springframework.web.bind.annotation.*;

@RestController
public class TreeBookingController {
    @PostMapping("/user")
    UserView signUpUser(@RequestBody UserView userToSignUp){

        //cod 201
        return null;
    }

/*
    @GetMapping("/login")
    UserView logIn(@RequestParam )
*/

}
