package com.example.experiment.geographic;

import com.example.experiment.geographic.domain.Coordinate;
import com.example.experiment.geographic.domain.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoordinateController {

    private final CoordinateRepository coordinateRepository;

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody CoordinateDto coordinateDto) {
        Coordinate coordinate = Coordinate.of(coordinateDto.getLongitude(), coordinateDto.getLatitude());
        Coordinate saved = coordinateRepository.save(coordinate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Void> find(@PathVariable Long id) {
        Coordinate coordinate = coordinateRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        System.out.println("coordinate.getPoint().getX() = " + coordinate.getPoint().getX());
        System.out.println("coordinate.getPoint().getY() = " + coordinate.getPoint().getY());
        return ResponseEntity.ok().build();
    }
}
