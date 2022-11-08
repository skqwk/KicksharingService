package ru.skqwk.kicksharingservice.service.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.exception.BadInputParametersException;
import ru.skqwk.kicksharingservice.service.TariffValidator;
import ru.skqwk.kicksharingservice.service.util.Validator;

import java.util.Objects;

/** Сервис для валидации расчетного тарифа. */
@Slf4j
@Service
public class SettlementTariffValidator implements TariffValidator {

  /**
   * Производит валидацию расчетного тарифа.
   *
   * @param tariffDTO Расчетный тариф.
   */
  @Override
  public void validate(TariffDTO tariffDTO) {
    try {
      Objects.requireNonNull(tariffDTO.getSettlementCost());
      Objects.requireNonNull(tariffDTO.getSettlementFor());
      Objects.requireNonNull(tariffDTO.getActivationCost());
      Objects.requireNonNull(tariffDTO.getDiscount());

      Validator.requireInterval(tariffDTO.getDiscount(), 0.0, 1.0);
      Validator.requirePositive(tariffDTO.getSettlementCost());
      Validator.requirePositive(tariffDTO.getActivationCost());

    } catch (NullPointerException ex) {
      log.warn("Try create tariff without mandatory fields");
      throw new BadInputParametersException(
          "Subscription tariff must have fields tariffCost, durationInHours, settlementCost, settlementFor not null");
    } catch (IllegalArgumentException ex) {
      log.warn("Try create tariff with incorrect interval of values");
      throw new BadInputParametersException(
          "Subscription tariff must have fields activationCost, durationInHours, settlementCost only > 0, and discount in interval [0, 1]");
    }
  }

  @Override
  public TariffType getType() {
    return TariffType.SETTLEMENT_TARIFF;
  }
}
