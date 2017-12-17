package io.cpneo.controller;

import io.cpneo.service.dto.GenericMessageDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.service.dto.StationRegisterDTO;
import io.cpneo.service.StationService;
import io.cpneo.station.Fuels;
import io.cpneo.station.StationCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
@CrossOrigin
@RestController
@RequestMapping("/sapi")
public class StationController {

    @Autowired
    StationService stationService;

    @Autowired
    HttpSession session;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public GenericMessageDTO registerStation(@RequestBody StationRegisterDTO stationRegisterDTO){
        return stationService.register(stationRegisterDTO);

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public GenericMessageDTO login(@RequestBody StationCredentials credentials){
        String token = stationService.login(credentials);
        if(token == null) return new GenericMessageDTO("Bad credentials, station not exist", 400);
        return new GenericMessageDTO(token,200);

    }

    @RequestMapping(value = "/station", method = RequestMethod.GET)
    public StationDTO getStation(@RequestHeader("Session-Token") String token){
        return stationService.getStation(token);
    }

    @RequestMapping(value = "/station/price", method = RequestMethod.POST)
    public GenericMessageDTO setPrice(@RequestBody Fuels fuels, @RequestHeader("Session-Token") String token){
        boolean isAdded=stationService.setFuels(fuels,token);
        if(isAdded)
            return new GenericMessageDTO("Added fuel prices",200);
        return new GenericMessageDTO("Bad sessionToken or fuels are null",400);
    }
}
