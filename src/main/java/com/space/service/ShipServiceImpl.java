package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private final ShipRepository shipRepository;

    private final ShipValidatorCreate shipValidatorCreate;

    private final ShipValidatorUpdate shipValidatorUpdate;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository, ShipValidatorCreate shipValidatorCreate, ShipValidatorUpdate shipValidatorUpdate) {
        this.shipValidatorCreate = shipValidatorCreate;
        this.shipRepository = shipRepository;
        this.shipValidatorUpdate = shipValidatorUpdate;
    }

    @Override
    public ResponseEntity<Ship> createShip(Ship ship) {
        if (ship.getUsed() == null) ship.setUsed(false);
        DataBinder dataBinder = new DataBinder(ship);
        dataBinder.addValidators(shipValidatorCreate);
        dataBinder.validate();

        if (!dataBinder.getBindingResult().hasErrors()) {
            ship.setRating(calculateRating(ship));
            return new ResponseEntity<>(shipRepository.save(ship), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Ship> getShipById(Long id) {
        if (id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipRepository.findById(id).isPresent()
                ? shipRepository.findById(id).get()
                : null;

        return ship != null
                ? new ResponseEntity<>(ship, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Ship> getShips(Map<String, String> param) {
        int page = 0;
        int size = 3;
        String shipOrder = "id";

        if (param.isEmpty()) {
            return shipRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, shipOrder))).getContent();
        } else {

            Specification<Ship> spec = getSpecification(param);

            if (param.get("pageSize") != null)
                size = Integer.parseInt(param.get("pageSize"));
            if (param.get("pageNumber") != null)
                page = Integer.parseInt(param.get("pageNumber"));
            if (param.get("order") != null)
                shipOrder = ShipOrder.valueOf(param.get("order")).getFieldName();

            return spec != null
                    ? shipRepository.findAll(spec, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, shipOrder))).getContent()
                    : shipRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, shipOrder))).getContent();
        }
    }

    @Override
    public ResponseEntity<?> deleteShip(Long id) {
        ResponseEntity<?> entity = getShipById(id);
        if (entity.getStatusCode() == HttpStatus.OK) {
            shipRepository.delete((Ship)entity.getBody());
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return entity;
    }

    @Override
    public Integer getCount(Map<String, String> param) {
        if (param.isEmpty()) {
            return shipRepository.findAll().size();
        } else {
            Specification<Ship> spec = getSpecification(param);
            return spec != null
                    ? shipRepository.findAll(spec).size()
                    : shipRepository.findAll().size();
        }
    }

    @Override
    public ResponseEntity<Ship> updateShip(Long id, Ship ship) {
        if (id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        DataBinder dataBinder = new DataBinder(ship);
        dataBinder.addValidators(shipValidatorUpdate);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Ship> optionalShipFromDAO = shipRepository.findById(id);
        if (!optionalShipFromDAO.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Ship shipFromDAO = optionalShipFromDAO.get();

        if (ship.getName() != null && !ship.getName().isEmpty()) shipFromDAO.setName(ship.getName());
        if (ship.getPlanet() != null && !ship.getPlanet().isEmpty()) shipFromDAO.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null) shipFromDAO.setShipType(ship.getShipType());
        if (ship.getProdDate() != null) shipFromDAO.setProdDate(ship.getProdDate());
        if (ship.getUsed() != null) shipFromDAO.setUsed(ship.getUsed());
        if (ship.getSpeed() != null) shipFromDAO.setSpeed(ship.getSpeed());
        if (ship.getCrewSize() != null) shipFromDAO.setCrewSize(ship.getCrewSize());
        shipFromDAO.setRating(calculateRating(shipFromDAO));
        return new ResponseEntity<>(shipRepository.save(shipFromDAO), HttpStatus.OK);
    }

    private Double calculateRating(Ship ship) {
        Double speed = ship.getSpeed();
        double k = !ship.getUsed() ? 1.0 : 0.5;
        int currentYear = new Date(33103198801000L).getYear();
        int prodDate = ship.getProdDate().getYear();
        double r = (80 * speed * k) / (currentYear - prodDate + 1);
        return (Math.round(r * 100)) / 100.0;
    }

    private Specification<Ship> getSpecification(Map<String, String> param) {
        Specification<Ship> spec = null;
        for (Map.Entry<String, String> entry : param.entrySet()) {
            String paramName = entry.getValue();
            try {
                switch (entry.getKey()) {
                    case "name":
                        spec = spec == null
                                ? ShipSpecification.getShipByName(paramName)
                                : spec.and(ShipSpecification.getShipByName(paramName));
                        break;
                    case "planet":
                        spec = spec == null
                                ? ShipSpecification.getShipByPlanet(paramName)
                                : spec.and(ShipSpecification.getShipByPlanet(paramName));
                        break;
                    case "shipType":
                        ShipType shipType = ShipType.valueOf(paramName);
                        spec = spec == null
                                ? ShipSpecification.getShipType(shipType)
                                : spec.and(ShipSpecification.getShipType(ShipType.valueOf(paramName)));
                        break;
                    case "after":
                        spec = spec == null
                                ? ShipSpecification.getShipAfterDate(Long.parseLong(paramName))
                                : spec.and(ShipSpecification.getShipAfterDate(Long.parseLong(paramName)));
                        break;
                    case "before":
                        spec = spec == null
                                ? ShipSpecification.getShipBeforeDate(Long.parseLong(paramName))
                                : spec.and(ShipSpecification.getShipBeforeDate(Long.parseLong(paramName)));
                        break;
                    case "isUsed":
                        spec = spec == null
                                ? ShipSpecification.getShipIsUsed(Boolean.parseBoolean(paramName))
                                : spec.and(ShipSpecification.getShipIsUsed(Boolean.parseBoolean(paramName)));
                        break;
                    case "minSpeed":
                        spec = spec == null
                                ? ShipSpecification.getMinSpeed(Double.parseDouble(paramName))
                                : spec.and(ShipSpecification.getMinSpeed(Double.parseDouble(paramName)));
                        break;
                    case "maxSpeed":
                        spec = spec == null
                                ? ShipSpecification.getMaxSpeed(Double.parseDouble(paramName))
                                : spec.and(ShipSpecification.getMaxSpeed(Double.parseDouble(paramName)));
                        break;
                    case "minCrewSize":
                        spec = spec == null
                                ? ShipSpecification.getMinCrewSize(Integer.parseInt(paramName))
                                : spec.and(ShipSpecification.getMinCrewSize(Integer.parseInt(paramName)));
                        break;
                    case "maxCrewSize":
                        spec = spec == null
                                ? ShipSpecification.getMaxCrewSize(Integer.parseInt(paramName))
                                : spec.and(ShipSpecification.getMaxCrewSize(Integer.parseInt(paramName)));
                        break;
                    case "minRating":
                        spec = spec == null
                                ? ShipSpecification.getMinRating(Double.parseDouble(paramName))
                                : spec.and(ShipSpecification.getMinRating(Double.parseDouble(paramName)));
                        break;
                    case "maxRating":
                        spec = spec == null
                                ? ShipSpecification.getMaxRating(Double.parseDouble(paramName))
                                : spec.and(ShipSpecification.getMaxRating(Double.parseDouble(paramName)));
                        break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return spec;
    }
}
