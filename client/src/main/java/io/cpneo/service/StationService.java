package io.cpneo.service;

import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.interfaces.dto.StationsDTO;
import io.cpneo.station.Station;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public interface StationService {

    public StationsDTO getStationListByPrice(String address, String fuel, int distance);

    public StationsDTO getStationListByRating(String address, int distance);

    public void addComment(CommentDTO commentDTO, String token);

    public List<CommentDTO> getComments(long id);

    public StationsDTO getStationsInCircle(String address, int distance) throws IOException;


}
