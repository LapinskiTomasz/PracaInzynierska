package io.cpneo.service;


import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.StatisticsDTO;
import io.cpneo.service.dto.GenericMessageDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.service.dto.StationRegisterDTO;
import io.cpneo.repository.StationRepository;
import io.cpneo.station.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@Slf4j
@Service("StationService")
public class StationServiceImpl implements StationService{

    @Autowired
    StationRepository stationRepository;

    @Override
    public GenericMessageDTO register(StationRegisterDTO registerDTO) {

        if(stationRepository.existsByLogin(registerDTO.getLogin())) return new GenericMessageDTO("Login already exist", 409);
        if(!registerDTO.getCode().equals("SUPERCODE")) return new GenericMessageDTO("Bad register code",403);

        Station station = new Station();
        station.setName(registerDTO.getName());
        station.setLogin(registerDTO.getLogin());
        station.setPassword(registerDTO.getPassword());
        Address address = new Address();
        address.setCity(registerDTO.getCity());
        address.setHomeNumber(registerDTO.getHomeNumber());
        address.setStreet(registerDTO.getStreet());
        address.setZipcode(registerDTO.getZipcode());
        address.setStation(station);
        station.setAddress(address);
        stationRepository.save(station);

        return new GenericMessageDTO("Registered",200);
    }

    @Override
    public String login(StationCredentials credentials) {

        Station station =stationRepository.findByLogin(credentials.getLogin());

        if(station != null && station.getPassword().equals(credentials.getPassword())) {
            String token = DigestUtils.sha512Hex(UUID.randomUUID().toString());
            station.setToken(token);
            stationRepository.save(station);
            return token;
        }
        else
            return null;
    }

    @Override
    public boolean setFuels(Fuels fuels, String token) {

        Station station = stationRepository.findByToken(token);
        if(station == null) return false;
        station.setPB95Price(fuels.getPB95());
        station.setPB98Price(fuels.getPB98());
        station.setONPrice(fuels.getON());
        station.setLPGPrice(fuels.getLPG());
        stationRepository.save(station);
        return true;
    }

    @Override
    public StationDTO getStation(String token){

        Station station = stationRepository.findByToken(token);

        if(station == null) return null;

        return  entityToStationDTO(station);

    }


    public List<CommentDTO> entityToCommentDTO(List<Comment> comments){
        List<CommentDTO> commentList = new LinkedList<>();
        for(Comment c: comments){
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(c.getComment());
            commentDTO.setRating(c.getRating());
            commentDTO.setStationID(c.getId());
            commentDTO.setAuthor(c.getAuthor());
            commentList.add(commentDTO);
        }
        return commentList;
    }

    public StationDTO entityToStationDTO(Station s){

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

        OptionalDouble avg1Opt =s.getComments().stream().mapToDouble(a->a.getRating()).average();
        if(avg1Opt.isPresent())
            stationDTO.setRating(String.format("%1.1f",avg1Opt.getAsDouble()).replace(",","."));
        else
            stationDTO.setRating("0.0");

        stationDTO.setStatistics(getStatistic(stationDTO.getComments()));

        return stationDTO;
    }



    private StatisticsDTO getStatistic(List<CommentDTO> comments){

        int size = comments.size();
        if(size==0){
            StatisticsDTO statistics = new StatisticsDTO();

            statistics.setOneStats(0);
            statistics.setTwoStats(0);
            statistics.setThreeStats(0);
            statistics.setFourStats(0);
            statistics.setFiveStats(0);

            return statistics;

        }

        int fives = 0;
        int fours = 0;
        int threes = 0;
        int twos = 0;
        int ones = 0;

        Double percent;


        for( CommentDTO c: comments){

            switch (c.getRating()){
                case 1: ones++;
                    break;
                case 2: twos++;
                    break;
                case 3: threes++;
                    break;
                case 4: fours++;
                    break;
                case 5: fives++;
                    break;
            }

        }




        StatisticsDTO statistics = new StatisticsDTO();

        percent = ((double)ones/size)*100;
        statistics.setOneStats(percent.intValue());
        percent = ((double)twos/size)*100;
        statistics.setTwoStats(percent.intValue());
        percent = ((double)threes/size)*100;
        statistics.setThreeStats(percent.intValue());
        percent = ((double)fours/size)*100;
        statistics.setFourStats(percent.intValue());
        percent = ((double)fives/size)*100;
        statistics.setFiveStats(percent.intValue());


        return statistics;
    }

}
