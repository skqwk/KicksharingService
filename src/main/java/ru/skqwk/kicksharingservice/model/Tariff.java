package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.kicksharingservice.enumeration.TariffType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.temporal.ChronoUnit;

/** Сущность для представления тарифов аренды. */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tariff")
public class Tariff {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariff_sequence")
  @SequenceGenerator(name = "tariff_sequence", sequenceName = "tariff_sequence", allocationSize = 1)
  private Long id;

  private String name;
  private String description;
  private Double settlementCost;
  private ChronoUnit settlementFor;
  private Double discount;
  private Double activationCost;
  private Double tariffCost;
  private Integer durationInHours;
  private TariffType type;
}
