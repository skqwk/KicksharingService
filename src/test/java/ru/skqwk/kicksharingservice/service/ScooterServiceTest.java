package ru.skqwk.kicksharingservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skqwk.kicksharingservice.dto.NewScooterDTO;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;
import ru.skqwk.kicksharingservice.exception.ResourceNotFoundException;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.repo.ScooterRepository;
import ru.skqwk.kicksharingservice.service.impl.ScooterServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScooterServiceTest {

  private final ScooterRepository scooterRepository = mock(ScooterRepository.class);
  private final ModelService modelService = mock(ModelService.class);
  private ScooterService scooterService;

  @BeforeEach
  void setUp() {
    scooterService = new ScooterServiceImpl(scooterRepository, modelService);
  }

  @Test
  void shouldCallRepoWhenAddNewScooter() {
    // Arrange
    NewScooterDTO newScooter = new NewScooterDTO();

    // Act
    scooterService.addNewScooter(newScooter);

    // Assert
    verify(scooterRepository).save(any(Scooter.class));
  }

  @Test
  void shouldCallRepoWhenUpdateScooter() {
    // Arrange
    NewScooterDTO updated = new NewScooterDTO();
    Scooter returned = Scooter.builder().build();
    long id = 1L;
    when(scooterRepository.findById(id)).thenReturn(Optional.of(returned));

    // Act
    scooterService.updateScooter(id, updated);

    // Assert
    verify(scooterRepository).findById(id);
    verify(scooterRepository).save(any(Scooter.class));
  }

  @Test
  void shouldCallRepoWhenDeleteScooter() {
    // Arrange
    long id = 1L;
    Scooter returned = Scooter.builder().status(ScooterStatus.NOT_ATTACHED).build();
    when(scooterRepository.findById(id)).thenReturn(Optional.of(returned));

    // Act
    scooterService.deleteScooter(id);

    // Assert
    verify(scooterRepository).deleteById(id);
  }

  @Test
  void shouldDoesntThrowExceptionWhenScooterExists() {
    // Arrange
    long id = 1L;
    Scooter returned = Scooter.builder().build();
    when(scooterRepository.findById(id)).thenReturn(Optional.of(returned));

    // Act and Assert
    Assertions.assertDoesNotThrow(() -> scooterService.findScooter(id));
    verify(scooterRepository).findById(id);
  }

  @Test
  void shouldThrowExceptionWhenScooterNotExisted() {
    // Arrange
    long id = 1L;

    when(scooterRepository.findById(id)).thenReturn(Optional.empty());

    // Act and Assert
    Assertions.assertThrows(ResourceNotFoundException.class, () -> scooterService.findScooter(id));
    verify(scooterRepository).findById(id);
  }
}
