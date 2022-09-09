package ru.skqwk.kicksharingservice.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skqwk.kicksharingservice.dto.IRentPoint;
import ru.skqwk.kicksharingservice.dto.RentPointClosestDTO;
import ru.skqwk.kicksharingservice.model.RentPoint;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase
class RentPointRepositoryTest {

  @Autowired private RentPointRepository repo;

  @Test
  void shouldFindClosestRentPoints() {
    // Arrange
    List<RentPoint> all =
        Stream.of(
                RentPoint.builder().latitude(121.0).longitude(121.0).build(),
                RentPoint.builder().latitude(120.0).longitude(120.0).build(),
                RentPoint.builder().latitude(122.0).longitude(122.0).build(),
                RentPoint.builder().latitude(119.0).longitude(119.0).build(),
                RentPoint.builder().latitude(118.0).longitude(118.0).build(),
                RentPoint.builder().latitude(123.0).longitude(123.0).build(),
                RentPoint.builder().latitude(117.0).longitude(117.0).build(),
                RentPoint.builder().latitude(116.0).longitude(116.0).build(),
                RentPoint.builder().latitude(115.0).longitude(115.0).build(),
                RentPoint.builder().latitude(114.0).longitude(114.0).build(),
                RentPoint.builder().latitude(113.0).longitude(113.0).build())
            .collect(Collectors.toList());
    Collections.shuffle(all);
    repo.saveAll(all);

    // Act
    List<RentPointClosestDTO> closestRentPoints =
        repo.find10ClosestRentPointsOrderedByDistance(123.0, 123.0).stream()
            .map(this::convertToClosestDTO)
            .collect(Collectors.toList());

    closestRentPoints.forEach(p -> System.out.println(p.getDistanceInKm()));

    // Assert
    Assertions.assertEquals(123.0, closestRentPoints.get(0).getLatitude());
    Assertions.assertEquals(10, closestRentPoints.size());
  }

  private RentPointClosestDTO convertToClosestDTO(IRentPoint rentPoint) {
    return RentPointClosestDTO.builder()
        .id(rentPoint.getId())
        .latitude(rentPoint.getLatitude())
        .distanceInKm(rentPoint.getDistanceInKm())
        .longitude(rentPoint.getLongitude())
        .build();
  }
}
