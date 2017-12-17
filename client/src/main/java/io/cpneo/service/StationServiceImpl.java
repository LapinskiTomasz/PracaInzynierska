package io.cpneo.service;


import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.repository.StationRepository;
import io.cpneo.station.Address;
import io.cpneo.station.Comment;
import io.cpneo.station.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("StationService")
public class StationServiceImpl implements StationService {

    @Autowired
    StationRepository stationRepository;

    @Override
    public List<StationDTO> getStationListByPrice(String city, String fuel) {

        ArrayList<Station> stationList=stationRepository.findAllByCity(city);

        if(fuel.equals("PB95")){
            stationList.sort(Comparator.comparing(Station::getPB95Price));
        }
        if(fuel.equals("PB98")){
            stationList.sort(Comparator.comparing(Station::getPB98Price));
        }
        if(fuel.equals("ON")){
            stationList.sort(Comparator.comparing(Station::getONPrice));
        }
        if(fuel.equals("LPG")){
            stationList.sort(Comparator.comparing(Station::getLPGPrice));
        }

        return entityToStationDTO(stationList);
    }

    @Override
    public List<StationDTO> getStationListByRating(String city) {
        ArrayList<Station> stationList=stationRepository.findAllByCity(city);

        Comparator<Station> comparator = new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
               OptionalDouble avg1Opt =o1.getComments().stream().mapToDouble(a->a.getRating()).average();
               OptionalDouble avg2Opt =o2.getComments().stream().mapToDouble(a->a.getRating()).average();

               double avg1 = 0.0;
               double avg2 = 0.0;

               if(avg1Opt.isPresent()) avg1 = avg1Opt.getAsDouble();
               if(avg2Opt.isPresent()) avg2 = avg2Opt.getAsDouble();


                return Double.compare(avg2,avg1);
            }
        };

        stationList.sort(comparator);
        return entityToStationDTO(stationList);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        Station station = stationRepository.findOne(commentDTO.getStationID());
        Comment comment = new Comment();
        List<Comment> commentList;

        comment.setStation(station);
        comment.setRating(commentDTO.getRating());
        comment.setComment(commentDTO.getComment());

        if(station.getComments() == null)
            commentList = new LinkedList<>();
        else
            commentList = station.getComments();

        commentList.add(comment);
        station.setComments(commentList);

        stationRepository.save(station);

    }

    @Override
    public List<CommentDTO> getComments(long id) {
        Station station =stationRepository.findOne(id);
        return entityToCommentDTO(station.getComments());

    }

    public List<CommentDTO> entityToCommentDTO(List<Comment> comments){
        List<CommentDTO> commentList = new LinkedList<>();
        for(Comment c: comments){
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(c.getComment());
            commentDTO.setRating(c.getRating());
            commentDTO.setStationID(c.getId());
            commentList.add(commentDTO);
        }
        return commentList;
    }

    public List<StationDTO> entityToStationDTO(List<Station> stations){
        List<StationDTO> stationList = new LinkedList<>();
        for(Station s: stations){
            Address address = s.getAddress();
            StationDTO stationDTO = new StationDTO();
            stationDTO.setName(s.getName());
            stationDTO.setId(s.getId());
            stationDTO.setPB95Price(s.getPB95Price());
            stationDTO.setPB98Price(s.getPB98Price());
            stationDTO.setONPrice(s.getONPrice());
            stationDTO.setLPGPrice(s.getLPGPrice());
            stationDTO.setCity(address.getCity());
            stationDTO.setStreet(address.getStreet());
            stationDTO.setHomeNumber(address.getHomeNumber());
            stationDTO.setZipcode(address.getZipcode());
            stationDTO.setComments(entityToCommentDTO(s.getComments()));
            stationList.add(stationDTO);
        }
        return stationList;
    }


}
