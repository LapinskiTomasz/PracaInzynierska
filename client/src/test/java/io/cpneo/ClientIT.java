package io.cpneo;

import io.cpneo.service.StationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ClientIT {

    @Autowired
    StationService stationService;

    @Test
    public void station_test(){
        System.out.println(stationService.getStationListByPrice("Wojnicz","PB95"));
        System.out.println(stationService.getStationListByRating("Wojnicz"));
    }


}
