package io.cpneo;

import io.cpneo.interfaces.dto.StationDTO;
import io.cpneo.repository.StationRepository;
import io.cpneo.service.DistanceService;
import io.cpneo.service.StationService;
import io.cpneo.station.Station;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class ClientIT {

    @Autowired
    StationService stationService;

    @Autowired
    DistanceService distanceService;

    @Test
    public void station_test() throws IOException {
            //stationService.getStationsInCircle("Wojnicz",40);
//        List<StationDTO> list = stationService.getStationListByPrice("Wojnicz","PB95",40);
//        log.info(list.toString());
//        log.info(stationService.getStationListByRating("Wojnicz",40).toString());
        //log.info(distanceService.getDistance(Double.valueOf(49.8579027),20.8429267,50.0100187,20.9686456)+"");

    }


}
