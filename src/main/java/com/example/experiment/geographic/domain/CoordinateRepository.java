package com.example.experiment.geographic.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CoordinateRepository extends Repository<Coordinate, Long> {

    Coordinate save(Coordinate coordinate);

    Optional<Coordinate> findById(Long id);
}
