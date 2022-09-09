package ru.skqwk.kicksharingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.kicksharingservice.model.Subscription;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.model.UserAccount;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  Optional<Subscription> findByUserAndTariff(UserAccount user, Tariff tariff);
}
