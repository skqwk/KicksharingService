package ru.skqwk.kicksharingservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skqwk.kicksharingservice.dto.RentPointDTO;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.RentPoint;
import ru.skqwk.kicksharingservice.repo.RentPointRepository;
import ru.skqwk.kicksharingservice.service.impl.RentPointServiceImpl;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RentPointServiceTest {

  private final RentPointRepository rentPointRepository = mock(RentPointRepository.class);
  private final ScooterService scooterService = mock(ScooterService.class);
  private RentPointService rentPointService;

  @BeforeEach
  void setUp() {
    rentPointService = new RentPointServiceImpl(rentPointRepository, scooterService);
  }

  @Test
  public void shouldCallFindClosestRentPointsInRepo() {
    // Arrange
    double lat = 10.0;
    double lng = 15.0;

    // Act
    rentPointService.findClosestRentPoints(lat, lng);

    // Assert
    verify(rentPointRepository).find10ClosestRentPointsOrderedByDistance(lat, lng);
  }

  @Test
  public void shouldCallDeleteByIdInRepo() {
    // Arrange
    long id = 1L;
    when(rentPointRepository.findById(1L))
        .thenReturn(Optional.of(RentPoint.builder().scooters(new HashSet<>()).build()));

    // Act
    rentPointService.deleteRentPoint(id);

    // Assert
    verify(rentPointRepository).deleteById(id);
  }

  @Test
  public void shouldCallFindByIdInRepo() {
    // Arrange
    long id = 1L;
    RentPoint returned = RentPoint.builder().build();
    when(rentPointRepository.findById(id)).thenReturn(Optional.of(returned));

    // Act
    rentPointService.findRentPoint(id);

    // Assert
    verify(rentPointRepository).findById(id);
  }

  @Test
  public void shouldThrowExceptionIfRentPointNotExisted() {
    // Arrange
    long id = 1L;
    when(rentPointRepository.findById(id)).thenReturn(Optional.empty());

    // Act and Assert
    Assertions.assertThrows(
        ResourceNotFoundException.class, () -> rentPointService.findRentPoint(id));
  }

  @Test
  public void shouldCheckExistingWhenUpdate() {
    // Arrange
    long id = 1L;
    RentPointDTO updated = RentPointDTO.builder().longitude(0.0).latitude(0.0).build();
    RentPoint returned = RentPoint.builder().build();
    when(rentPointRepository.findById(id)).thenReturn(Optional.of(returned));

    // Act
    rentPointService.updateRentPoint(id, updated);

    // Assert
    verify(rentPointRepository).findById(id);
    verify(rentPointRepository).save(any(RentPoint.class));
  }

  @Test
  public void shouldCallRepoWhenAddNewRentPoint() {
    // Arrange
    RentPointDTO newRentPoint = RentPointDTO.builder().latitude(0.0).longitude(0.0).build();

    // Act
    rentPointService.addNewRentPoint(newRentPoint);

    // Assert
    verify(rentPointRepository).save(any(RentPoint.class));
  }
}
