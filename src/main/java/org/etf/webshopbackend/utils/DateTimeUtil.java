package org.etf.webshopbackend.utils;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

  // ISO dates use the ISO 8601 standard
  public static LocalDateTime convertIsoToDate(String isoDate) {
    if (isoDate == null) {
      return null;
    }
    DateTimeFormatter isoDateFormatter = DateTimeFormatter.ofPattern(StdDateFormat.DATE_FORMAT_STR_ISO8601);
    return LocalDateTime.from(isoDateFormatter.parse(isoDate));
  }
}
