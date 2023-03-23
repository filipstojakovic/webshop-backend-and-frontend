package org.etf.webshopbackend.converters;

import org.etf.webshopbackend.utils.DateTimeUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  @Override
  public LocalDateTime convert(String source) {
    return DateTimeUtil.convertIsoToDate(source);
  }
}
