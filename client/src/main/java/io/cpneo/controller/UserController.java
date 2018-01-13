package io.cpneo.controller;


import io.cpneo.client.User;
import io.cpneo.client.UserCredentials;
import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.GenericMessageDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.interfaces.dto.StationsDTO;
import io.cpneo.repository.UserRepository;
import io.cpneo.service.StationService;
import io.cpneo.service.UserService;
import io.cpneo.station.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StationService stationService;

    @Autowired
    private HttpSession session;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public GenericMessageDTO registerUser(@RequestBody UserCredentials credentials){
        if(userService.register(credentials)) return new GenericMessageDTO("Registered",200);
        else return new GenericMessageDTO("User exist",400);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public GenericMessageDTO loginUser(@RequestBody UserCredentials credentials){
        String token =userService.login(credentials);
        if(!token.equals("")){
            session.setAttribute("login",credentials.getLogin());
            return new GenericMessageDTO(token,200);
        }
        else return new GenericMessageDTO("Bad credentials",400);
    }


    @RequestMapping(value = "/compare/price", method = RequestMethod.GET)
    public StationsDTO compareStations(@RequestParam(value="city") String city, @RequestParam(value="fuel") String fuel, @RequestParam(value ="distance") int distance) {
    return stationService.getStationListByPrice(city,fuel,distance);
    }

    @RequestMapping(value = "/compare/rating", method = RequestMethod.GET)
    public StationsDTO compareStations(@RequestParam(value="city") String city, @RequestParam(value ="distance") int distance) {
        return stationService.getStationListByRating(city,distance);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public List<CommentDTO> getStationComments(@RequestParam(value = "stationID") long id){
        return stationService.getComments(id);
    }

    @RequestMapping(value = "/login/comment", method = RequestMethod.POST)
    public GenericMessageDTO addComment(@RequestBody CommentDTO commentDTO, @RequestHeader("Session-Token") String token){
        if(!userService.loggedIn(token)) return new GenericMessageDTO("Not logged",401);
        stationService.addComment(commentDTO, token);
        return new GenericMessageDTO("Comment added", 200);
    }

}
