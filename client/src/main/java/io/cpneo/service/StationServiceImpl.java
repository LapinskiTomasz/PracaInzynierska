package io.cpneo.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cpneo.client.User;
import io.cpneo.interfaces.dto.CommentDTO;
import io.cpneo.interfaces.dto.PositionDTO;
import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.interfaces.dto.StationsDTO;
import io.cpneo.repository.StationRepository;
import io.cpneo.repository.UserRepository;
import io.cpneo.station.Address;
import io.cpneo.station.Comment;
import io.cpneo.station.Station;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service("StationService")
public class StationServiceImpl implements StationService {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DistanceService distanceService;

    protected ObjectMapper mapper = new ObjectMapper();

    protected TypeReference<HashMap<String, Object>> typeRef
            = new TypeReference<HashMap<String, Object>>() {
    };

    protected Map<String, Object> jsonResponse;

    @Override
    public StationsDTO getStationListByPrice(String address, String fuel, int distance) {
        ArrayList<StationDTO> stationList = new ArrayList<>();
        StationsDTO stationsDTO = new StationsDTO();
        if(address.equals(""))
            stationList = (ArrayList)entityToStationDTO((ArrayList<Station>)stationRepository.findAll());
        else
            try {
                stationsDTO=getStationsInCircle(address,distance);
                stationList=stationsDTO.getStationsDTO();
            } catch (IOException e) {
                e.printStackTrace();
            }

        stationList = (ArrayList)removeZeroStation(stationList);

        if(fuel.equals("PB95")){
            stationList.sort(Comparator.comparing(StationDTO::getPB95Price));
        }
        if(fuel.equals("PB98")){
            stationList.sort(Comparator.comparing(StationDTO::getPB98Price));
        }
        if(fuel.equals("ON")){
            stationList.sort(Comparator.comparing(StationDTO::getONPrice));
        }
        if(fuel.equals("LPG")){
            stationList.sort(Comparator.comparing(StationDTO::getLPGPrice));
        }

        stationsDTO.setStationsDTO(stationList);

        return stationsDTO;
    }

    @Override
    public StationsDTO getStationListByRating(String address, int distance) {
        ArrayList<StationDTO> stationList = new ArrayList<>();
        StationsDTO stationsDTO = new StationsDTO();
        if(address.equals(""))
            stationList = (ArrayList)entityToStationDTO((ArrayList<Station>)stationRepository.findAll());
        else
            try {
                stationsDTO=getStationsInCircle(address,distance);
                stationList=stationsDTO.getStationsDTO();
            } catch (IOException e) {
                e.printStackTrace();
            }

        stationList = (ArrayList)removeZeroStation(stationList);

        Comparator<StationDTO> comparator = new Comparator<StationDTO>() {
            @Override
            public int compare(StationDTO o1, StationDTO o2) {
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
        stationsDTO.setStationsDTO(stationList);
        return stationsDTO;
    }

    @Override
    public void addComment(CommentDTO commentDTO, String token) {
        Station station = stationRepository.findOne(commentDTO.getStationID());
        User user = userRepository.findByToken(token);
        Comment comment = new Comment();
        List<Comment> commentList;


        comment.setStation(station);
        comment.setRating(commentDTO.getRating());
        comment.setComment(commentDTO.getComment());
        comment.setAuthor(user.getLogin());

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
            commentDTO.setAuthor(c.getAuthor());
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

            OptionalDouble avg1Opt =s.getComments().stream().mapToDouble(a->a.getRating()).average();

            if(avg1Opt.isPresent())
                stationDTO.setRating(String.format("%1.1f",avg1Opt.getAsDouble()).replace(",","."));
            else
                stationDTO.setRating("0.0");

            stationList.add(stationDTO);
        }
        return stationList;
    }

    public List<StationDTO> removeZeroStation(List<StationDTO> stationList){

        List<StationDTO> list = stationList;
        for(StationDTO s: list){
            if(s.getPB95Price() == 0 || s.getPB98Price() == 0 || s.getONPrice() == 0 || s.getLPGPrice() == 0){
                list.remove(s);
            }
        }

        return list;
    }

    public StationsDTO getStationsInCircle(String address, int distance) throws IOException {
        ArrayList<StationDTO> stationsInCircle = new ArrayList<>();
        String addr = address.replace(" ","+");
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address="+addr+"+PL&key=AIzaSyBjL50qBMCVGQrpGKf_WV2h6r04xAri6gQ";
        Connection.Response cordResponse = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();

        Map<String,String> coordMap = new HashMap<>();

//        log.info(cordResponse.body());

        jsonResponse = mapper.readValue(cordResponse.body(),typeRef);
        jsonResponse =((Map<String,Object>)(((List<Object>)jsonResponse.get("results")).get(0)));
//         log.info(jsonResponse.toString());
        jsonResponse = (Map<String,Object>)((Map<String,Object>)jsonResponse.get("geometry")).get("location");

        coordMap.put("lat",jsonResponse.get("lat").toString());
        coordMap.put("lng",jsonResponse.get("lng").toString());


        log.info(coordMap.toString());

        ArrayList<Station> stationList = (ArrayList<Station>)stationRepository.findAll();

        for(Station s: stationList){
            double dist = distanceService.getDistance(Double.valueOf(coordMap.get("lat")),Double.valueOf(coordMap.get("lng")),Double.valueOf(s.getAddress().getLat()),Double.valueOf(s.getAddress().getLng()));
            if(dist<distance){
                Address stationAddress = s.getAddress();
                StationDTO stationDTO = new StationDTO();
                stationDTO.setName(s.getName());
                stationDTO.setId(s.getId());
                stationDTO.setPB95Price(s.getPB95Price());
                stationDTO.setPB98Price(s.getPB98Price());
                stationDTO.setONPrice(s.getONPrice());
                stationDTO.setLPGPrice(s.getLPGPrice());
                stationDTO.setCity(stationAddress.getCity());
                stationDTO.setStreet(stationAddress.getStreet());
                stationDTO.setHomeNumber(stationAddress.getHomeNumber());
                stationDTO.setZipcode(stationAddress.getZipcode());
                stationDTO.setComments(entityToCommentDTO(s.getComments()));
                stationDTO.setDistance((int)dist);
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setDescription(s.getName()+", "+stationAddress.getCity()+" ul. "+stationAddress.getStreet()+" "+stationAddress.getHomeNumber());
                positionDTO.setLatitude(Double.valueOf(stationAddress.getLat()));
                positionDTO.setLongitude(Double.valueOf(stationAddress.getLng()));
                stationDTO.setPosition(positionDTO);

                OptionalDouble avg1Opt =s.getComments().stream().mapToDouble(a->a.getRating()).average();

                if(avg1Opt.isPresent())
                    stationDTO.setRating(String.format("%1.1f",avg1Opt.getAsDouble()).replace(",","."));
                else
                    stationDTO.setRating("0.0");

                stationsInCircle.add(stationDTO);
            }
        }

        StationsDTO stationsDTO = new StationsDTO();
        stationsDTO.setStationsDTO(stationsInCircle);
        PositionDTO userPositionDTO = new PositionDTO();
        userPositionDTO.setLatitude(Double.valueOf(coordMap.get("lat")));
        userPositionDTO.setLongitude(Double.valueOf(coordMap.get("lng")));
        userPositionDTO.setDescription("Twój adres");
        stationsDTO.setUserPosition(userPositionDTO);

        return stationsDTO;
    }




}
