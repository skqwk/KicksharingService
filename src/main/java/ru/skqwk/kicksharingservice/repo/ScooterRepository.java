package ru.skqwk.kicksharingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.kicksharingservice.model.Model;
import ru.skqwk.kicksharingservice.model.Scooter;

import java.util.List;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {
    List<Scooter> findAllByModel(Model model);
}
