package io.cpneo.service;


import io.cpneo.service.dto.GenericMessageDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.service.dto.StationRegisterDTO;
import io.cpneo.station.Address;
import io.cpneo.station.Fuels;
import io.cpneo.station.StationCredentials;
import org.springframework.stereotype.Service;

@Service
public interface StationService {
    public GenericMessageDTO register(StationRegisterDTO registerDTO);

    public String login(StationCredentials credentials);

    public boolean setFuels(Fuels fuels, String token);

    public StationDTO getStation(String token);



}
