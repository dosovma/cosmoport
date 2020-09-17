package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ShipValidatorCreate implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Ship.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ship ship = (Ship) target;

        String shipName = ship.getName();
        if (shipName == null || shipName.isEmpty() || shipName.length() > 50)
            errors.rejectValue("name", "value.negative");

        String planetName = ship.getPlanet();
        if (planetName == null || planetName.isEmpty() || planetName.length() > 50)
            errors.rejectValue("planet", "value.negative");

        if (ship.getProdDate() == null)
            errors.rejectValue("speed", "value.negative");
        else {
            long date = ship.getProdDate().getTime();
            if (date < 26192235600000L || date > 33103198799000L)
                errors.rejectValue("prodDate", "value.negative");
        }

        if (ship.getSpeed() == null)
            errors.rejectValue("speed", "value.negative");
        else {
            double speed = ship.getSpeed();
            if (speed < 0.01 || speed > 0.99)
                errors.rejectValue("speed", "value.negative");
        }

        if (ship.getCrewSize() == null)
            errors.rejectValue("speed", "value.negative");
        else {
            Integer crewSize = ship.getCrewSize();
            if (crewSize < 1 || crewSize > 9999)
                errors.rejectValue("crewSize", "value.negative");
        }
    }
}
