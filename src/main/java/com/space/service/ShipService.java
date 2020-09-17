package com.space.service;

import com.space.model.Ship;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ShipService {

    ResponseEntity<Ship> createShip(Ship ship);

    ResponseEntity<Ship> getShipById(Long id);

    ResponseEntity<?> deleteShip(Long id);

    List<Ship> getShips(Map<String, String> param);

    Integer getCount(Map<String, String> param);

    ResponseEntity<Ship> updateShip(Long id, Ship ship);
}
