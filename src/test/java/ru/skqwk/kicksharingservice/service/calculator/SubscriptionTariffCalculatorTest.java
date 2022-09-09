package ru.skqwk.kicksharingservice.service.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Subscription;
import ru.skqwk.kicksharingservice.model.Tariff;
import ru.skqwk.kicksharingservice.model.UserAccount;
import ru.skqwk.kicksharingservice.repo.SubscriptionRepository;
import ru.skqwk.kicksharingservice.service.TariffCalculator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionTariffCalculatorTest {

    private TariffCalculator tariffCalculator;
    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);

    @BeforeEach
    public void setUp() {
        tariffCalculator = new SubscriptionTariffCalculator(subscriptionRepository);
    }

    // Для тарифов по подписке цена считается по формуле:
    //
    // Если у пользователя ЕСТЬ подписка и она НЕ истекла
    // (длительность_аренды * стоимость_аренды) * (1.0 - скидка)
    //
    // Иначе:
    // стоимость_подписки + (длительность_аренды * стоимость_аренды) * (1.0 - скидка)

    @Test
    void shouldAddTariffCostIfUserNotSubscriber() {
        // Arrange
        double settlementCost = 10.0;
        double tariffCost = 20.0;
        double discount = 0.1;
        int durationInHours = 2;

        UserAccount user = UserAccount.builder().build();

        Tariff tariff = Tariff.builder()
                .settlementFor(ChronoUnit.HOURS)
                .settlementCost(settlementCost)
                .discount(discount)
                .durationInHours(2)
                .tariffCost(tariffCost)
                .build();

        Rent rent = Rent.builder()
                .user(user)
                .tariff(tariff)
                .startedIn(Instant.now())
                .finishedIn(Instant.now().plus(durationInHours, ChronoUnit.HOURS))
                .build();

        when(subscriptionRepository.findByUserAndTariff(user, tariff))
                .thenReturn(Optional.empty());


        // Act
        double calculatedCost = tariffCalculator.calculate(rent);

        // Assert
        double expectedCost = tariffCost + (durationInHours * settlementCost) * (1 - discount);
        Assertions.assertEquals(expectedCost, calculatedCost);
    }

    @Test
    void shouldAddTariffCostIfSubscriptionIsExpired() {
        // Arrange
        double settlementCost = 10.0;
        double tariffCost = 20.0;
        double discount = 0.1;
        int durationInHours = 2;

        UserAccount user = UserAccount.builder().build();

        Tariff tariff = Tariff.builder()
                .settlementFor(ChronoUnit.HOURS)
                .settlementCost(settlementCost)
                .discount(discount)
                .durationInHours(2)
                .tariffCost(tariffCost)
                .build();

        Rent rent = Rent.builder()
                .user(user)
                .tariff(tariff)
                .startedIn(Instant.now())
                .finishedIn(Instant.now().plus(durationInHours, ChronoUnit.HOURS))
                .build();

        Subscription expiredSubscription = Subscription.builder()
                .expiredIn(Instant.now().minus(2L, ChronoUnit.HOURS))
                .build();

        when(subscriptionRepository.findByUserAndTariff(user, tariff))
                .thenReturn(Optional.of(expiredSubscription));


        // Act
        double calculatedCost = tariffCalculator.calculate(rent);

        // Assert
        double expectedCost = tariffCost + (durationInHours * settlementCost) * (1 - discount);
        Assertions.assertEquals(expectedCost, calculatedCost);
    }


    @Test
    void shouldNotAddTariffCostIfSubscriptionIsActual() {
        // Arrange
        double settlementCost = 10.0;
        double tariffCost = 20.0;
        double discount = 0.1;
        int durationInHours = 2;

        UserAccount user = UserAccount.builder().build();

        Tariff tariff = Tariff.builder()
                .settlementFor(ChronoUnit.HOURS)
                .settlementCost(settlementCost)
                .discount(discount)
                .tariffCost(tariffCost)
                .build();

        Rent rent = Rent.builder()
                .user(user)
                .tariff(tariff)
                .startedIn(Instant.now())
                .finishedIn(Instant.now().plus(durationInHours, ChronoUnit.HOURS))
                .build();

        Subscription actualSubscription = Subscription.builder()
                .expiredIn(Instant.now().plus(2L, ChronoUnit.HOURS))
                .build();

        when(subscriptionRepository.findByUserAndTariff(user, tariff))
                .thenReturn(Optional.of(actualSubscription));


        // Act
        double calculatedCost = tariffCalculator.calculate(rent);

        // Assert
        double expectedCost = (durationInHours * settlementCost) * (1 - discount);
        Assertions.assertEquals(expectedCost, calculatedCost);
    }
}