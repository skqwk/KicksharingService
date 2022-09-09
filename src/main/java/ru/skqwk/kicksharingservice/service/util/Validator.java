package ru.skqwk.kicksharingservice.service.util;

/** Util-класс для валидации входных данных. */
public class Validator {

  public static void requirePositive(Number number) throws IllegalArgumentException {
    if (number.doubleValue() <= 0) {
      throw new IllegalArgumentException("Value must be positive");
    }
  }

  public static void requireInterval(Number num, Number from, Number to) {
    if (num.doubleValue() < from.doubleValue() || num.doubleValue() > to.doubleValue()) {
      throw new IllegalArgumentException(
          String.format("Value %s must be in interval [%s, %s]", num, from, to));
    }
  }
}
