package ru.skqwk.kicksharingservice.dto;

/** Сущность для маппинга точек аренды с помощью SQL-запроса. */
public interface IRentPoint {
  Long getId();

  Double getLatitude();

  Double getLongitude();

  Double getDistanceInKm();
}
