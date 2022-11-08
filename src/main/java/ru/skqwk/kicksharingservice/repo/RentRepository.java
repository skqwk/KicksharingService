package ru.skqwk.kicksharingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Scooter;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.model.UserAccount;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
  List<Rent> findAllByUser(UserAccount user);

  List<Rent> findAllByScooter(Scooter scooter);

  Optional<Rent> findByUserAndStatus(UserAccount user, RentStatus status);

  Optional<Rent> findByUserAndId(UserAccount user, Long rentId);

  List<Rent> findAllByTariffAndFinishedAt(Tariff tariff, Instant finishedAt);
}
