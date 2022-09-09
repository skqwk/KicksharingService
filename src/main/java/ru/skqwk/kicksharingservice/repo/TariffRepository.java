package ru.skqwk.kicksharingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.kicksharingservice.model.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {}
