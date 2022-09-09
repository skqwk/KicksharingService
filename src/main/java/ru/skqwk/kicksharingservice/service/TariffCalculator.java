package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.enumeration.TariffType;
import ru.skqwk.kicksharingservice.model.Rent;
import ru.skqwk.kicksharingservice.model.Tariff;

public interface TariffCalculator {
    Double calculate(Rent rent);
    TariffType getType();
}
