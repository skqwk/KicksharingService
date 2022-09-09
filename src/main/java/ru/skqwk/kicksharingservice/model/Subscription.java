package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;

/**
 * Сущность для представления подписки.
 *
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription")
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_sequence")
  @SequenceGenerator(
      name = "subscription_sequence",
      sequenceName = "subscription_sequence",
      allocationSize = 1)
  private Long id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserAccount user;

  @OneToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Tariff tariff;

  private Instant expiredIn;
}
