package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public ResponseEntity<List<Ship>> getShips(@RequestParam Map<String, String> allParam) {
        return new ResponseEntity<>(shipService.getShips(allParam), HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getCount(@RequestParam Map<String, String> allParam) {
        return new ResponseEntity<>(shipService.getCount(allParam), HttpStatus.OK);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    public ResponseEntity<?> createShip(@RequestBody Ship ship) {
        return shipService.createShip(ship);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShipById(@PathVariable(name = "id")  Long id) {
        return shipService.getShipById(id);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    public ResponseEntity<Ship> updateShip(@PathVariable Long id, @RequestBody Ship ship) {
        return shipService.updateShip(id, ship);

    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShip(@PathVariable(name = "id") Long id) {
        return shipService.deleteShip(id);
    }
}
