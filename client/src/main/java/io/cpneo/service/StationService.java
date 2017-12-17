package io.cpneo.service;

import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.station.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StationService {

    public List<StationDTO> getStationListByPrice(String city, String fuel);

    public List<StationDTO> getStationListByRating(String city);

    public void addComment(CommentDTO commentDTO, String token);

    public List<CommentDTO> getComments(long id);


}
