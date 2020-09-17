package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ShipSpecification {

    public static Specification<Ship> getShipByName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%"+name+"%");
    }

    public static Specification<Ship> getShipByPlanet(String planetName) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("planet"), "%"+planetName+"%"));
    }

    public static Specification<Ship> getShipAfterDate(Long afterDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(afterDate)));
    }

    public static Specification<Ship> getShipBeforeDate(Long before) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before)));
    }

    public static Specification<Ship> getShipType(ShipType shipType) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shipType"), shipType));
    }

    public static Specification<Ship> getShipIsUsed(boolean isUsed) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isUsed"), isUsed));
    }

    public static Specification<Ship> getMinSpeed(Double minSpeed) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed));
    }

    public static Specification<Ship> getMinRating(Double minRating) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
    }

    public static Specification<Ship> getMaxSpeed(Double maxSpeed) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed));
    }

    public static Specification<Ship> getMaxRating(Double maxRating) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
    }

    public static Specification<Ship> getMinCrewSize(Integer minCrewSize) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
    }

    public static Specification<Ship> getMaxCrewSize(Integer maxCrewSize) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize));
    }
}
