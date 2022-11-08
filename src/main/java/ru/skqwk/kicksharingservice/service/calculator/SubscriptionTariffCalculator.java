package ru.skqwk.kicksharingservice.service.calculator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Subscription;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.repo.SubscriptionRepository;
import ru.skqwk.kicksharingservice.service.TariffCalculator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/** Сервис-калькулятор для подсчета стоимости аренды по тарифу с подпиской. */
@Service
@AllArgsConstructor
public class SubscriptionTariffCalculator implements TariffCalculator {

  private final SubscriptionRepository subscriptionRepository;

  @Override
  public Double calculate(Rent rent) {
    Tariff subscriptionTariff = rent.getTariff();
    Optional<Subscription> subscriptionO =
        subscriptionRepository.findByUserAndTariff(rent.getUser(), subscriptionTariff);

    Duration absoluteDuration = Duration.between(rent.getStartedAt(), rent.getFinishedAt());
    Duration settlementForDuration = subscriptionTariff.getSettlementFor().getDuration();

    long relativeDuration = absoluteDuration.dividedBy(settlementForDuration);

    double discount = 1.0 - subscriptionTariff.getDiscount();
    double costWithoutTariffCost =
        relativeDuration * subscriptionTariff.getSettlementCost() * discount;

    if (subscriptionO.isEmpty() || isExpired(subscriptionO.get())) {

      int durationInHours = subscriptionTariff.getDurationInHours();
      Subscription newSubscription =
          Subscription.builder()
              .tariff(subscriptionTariff)
              .user(rent.getUser())
              .expiredIn(Instant.now().plus(durationInHours, ChronoUnit.HOURS))
              .build();

      subscriptionRepository.save(newSubscription);

      return subscriptionTariff.getTariffCost() + costWithoutTariffCost;
    } else {
      return costWithoutTariffCost;
    }
  }

  private boolean isExpired(Subscription subscription) {
    return subscription.getExpiredIn().isBefore(Instant.now());
  }

  @Override
  public TariffType getType() {
    return TariffType.SUBSCRIPTION_TARIFF;
  }
}
