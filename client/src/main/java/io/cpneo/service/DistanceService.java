package io.cpneo.service;

import org.springframework.stereotype.Service;

@Service
public interface DistanceService {

    public double getDistance(double lat1, double lon1, double lat2, double lon2);
}
