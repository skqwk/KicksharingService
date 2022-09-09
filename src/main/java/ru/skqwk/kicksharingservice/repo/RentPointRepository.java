package ru.skqwk.kicksharingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skqwk.kicksharingservice.dto.IRentPoint;
import ru.skqwk.kicksharingservice.model.RentPoint;

import java.util.List;

@Repository
public interface RentPointRepository extends JpaRepository<RentPoint, Long> {

  /**
   * Находит ближайшие 10 точек по координатам пользователя. Для вычисления используется <a
   * href="https://www.matematicus.ru/cplusplus/vychislenie-rasstoyaniya-po-formule-gaversinusa-na-c">формула
   * Гаверсинуса</a> для нахождения расстояния между двумя точками на сферической поверхности:<br>
   * <br>
   * lat1, lat2 - широта в радианах<br>
   * lon1, lon2 - долгота в радианах<br>
   * R - радиус Земли (6371 км)<br>
   * d = 2 * R * arcsin(sqrt(sin^2((lat2 - lat1) / 2) + cos(lat1) * cos(lat2) * sin^2((lon2 - lon1)
   * / 2)))
   */
  @Query(
      value =
          "SELECT id, latitude, longitude, "
              + "(2 * 6371 * asin(sqrt(power(sin((radians(:latitude) - radians(latitude)) / 2), 2)"
              + " + cos(radians(:latitude)) * cos(radians(latitude))"
              + " * power(sin((radians(:longitude) - radians(longitude)) / 2), 2)))) as distanceInKm"
              + " FROM rent_point ORDER BY distanceInKm LIMIT 10",
      nativeQuery = true)
  List<IRentPoint> find10ClosestRentPointsOrderedByDistance(
      @Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
