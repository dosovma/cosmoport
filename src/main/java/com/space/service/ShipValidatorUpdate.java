package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ShipValidatorUpdate implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Ship.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ship ship = (Ship) target;

        if (ship.getId() != null) {
            Long shipId = ship.getId();
            if (shipId <= 0)
                errors.rejectValue("name", "value.negative");
        }

        if (ship.getName() != null) {
            String shipName = ship.getName();
            if (shipName.isEmpty() || shipName.length() > 50)
                errors.rejectValue("name", "value.negative");
        }

        if (ship.getPlanet() != null) {
            String planetName = ship.getPlanet();
            if (planetName == null || planetName.isEmpty() || planetName.length() > 50)
                errors.rejectValue("planet", "value.negative");
        }

        if (ship.getProdDate() != null) {
            long date = ship.getProdDate().getTime();
            if (date < 26192160000000L || date > 33137337600000L)
                errors.rejectValue("prodDate", "value.negative");
        }

        if (ship.getSpeed() != null) {
            double speed = ship.getSpeed();
            if (speed < 0.01 || speed > 0.99)
                errors.rejectValue("speed", "value.negative");
        }

        if (ship.getCrewSize() != null) {
            Integer crewSize = ship.getCrewSize();
            if (crewSize < 1 || crewSize > 9999)
                errors.rejectValue("crewSize", "value.negative");
        }
    }
}
