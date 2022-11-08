package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.skqwk.kicksharingservice.enumeration.RentStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;

/** Сущность для представления аренды. */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rent_history")
public class Rent {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_sequence")
  @SequenceGenerator(name = "rent_sequence", sequenceName = "rent_sequence", allocationSize = 1)
  private Long id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  private UserAccount user;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  private Scooter scooter;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  private Tariff tariff;

  private Instant startedAt;
  private Instant finishedAt;
  private Double cost;
  private RentStatus status;
}
