package ru.skqwk.kicksharingservice.service;

import ru.skqwk.kicksharingservice.dto.TariffDTO;
import ru.skqwk.kicksharingservice.enumeration.TariffType;

public interface TariffValidator {
    public void validate(TariffDTO tariffDTO);
    TariffType getType();
}
