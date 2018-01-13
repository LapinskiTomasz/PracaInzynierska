package io.cpneo.repository;

import io.cpneo.station.Station;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface StationRepository extends CrudRepository<Station,Long> {

    public boolean existsByLogin(String login);

    public Station findByToken(String token);

    public Station findByLogin(String login);

    @Query("SELECT s FROM Station s WHERE s.address.city = :city")
    public ArrayList<Station> findAllByCity(@Param("city") String city);

//    @Query("Select s from Station s WHERE s.id in (Select a.id from (SELECT b.station_id as 'id', ( 6371 * acos( cos( radians(:latt) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:lngg) ) + sin( radians(:latt) ) * sin( radians( lat ) ) ) ) AS distance FROM address b HAVING distance < :distt) a)")
//    public ArrayList<Station> findAllByCoordinates(@Param("latt") double latt, @Param("lngg") double lngg, @Param("dist") int dist );


}
