package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.model.Rent;

public interface TariffCalculator {
  Double calculate(Rent rent);

  TariffType getType();
}
