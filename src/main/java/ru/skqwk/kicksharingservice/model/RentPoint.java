package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/** Сущность для представления точки аренды. */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rent_point")
public class RentPoint {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_point_sequence")
  @SequenceGenerator(
      name = "rent_point_sequence",
      sequenceName = "rent_point_sequence",
      allocationSize = 1)
  private Long id;

  private Double longitude;
  private Double latitude;

  @OneToMany private Set<Scooter> scooters = new HashSet<>();
}
