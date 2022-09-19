package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.skqwk.kicksharingservice.enumeration.ScooterStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Duration;

/** Сущность для представления самоката. */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scooter")
public class Scooter {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scooter_sequence")
  @SequenceGenerator(
      name = "scooter_sequence",
      sequenceName = "scooter_sequence",
      allocationSize = 1)
  private Long id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Model model;

  private Duration timeInUse;

  private ScooterStatus status = ScooterStatus.NOT_ATTACHED;

  @Override
  public int hashCode() {
    return Math.toIntExact(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Scooter scooter = (Scooter) o;

    return id.equals(scooter.id);
  }
}
