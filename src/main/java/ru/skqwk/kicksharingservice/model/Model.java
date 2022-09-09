package ru.skqwk.kicksharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Сущность для представления моделей самокатов.
 *
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model")
public class Model {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_sequence")
  @SequenceGenerator(name = "model_sequence", sequenceName = "model_sequence", allocationSize = 1)
  private Long id;

  private String name;
  private String manufacturer;
}
