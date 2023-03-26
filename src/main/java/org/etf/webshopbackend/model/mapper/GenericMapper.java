package org.etf.webshopbackend.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenericMapper<REQUEST, ENTITY, RESPONSE> {

  private final ModelMapper modelMapper;

  public ENTITY fromRequest(REQUEST request, Class<ENTITY> targetClass) {
    return modelMapper.map(request, targetClass);
  }

  public REQUEST toRequest(ENTITY entity, Class<REQUEST> targetClass) {
    return modelMapper.map(entity, targetClass);
  }

  public List<REQUEST> toDtos(List<ENTITY> entities, Class<REQUEST> targetClass) {
    return entities.stream()
        .map(item -> toRequest(item, targetClass))
        .collect(Collectors.toList());
  }

  public List<ENTITY> fromRequests(List<REQUEST> requests, Class<ENTITY> targetClass) {
    return requests.stream()
        .map(item -> fromRequest(item, targetClass))
        .collect(Collectors.toList());
  }

  public RESPONSE toResponse(ENTITY entity, Class<RESPONSE> targetClass) {
    return modelMapper.map(entity, targetClass);
  }

  public List<RESPONSE> toResponses(List<ENTITY> entities, Class<RESPONSE> targetClass) {
    return entities.stream()
        .map(item -> toResponse(item, targetClass))
        .collect(Collectors.toList());
  }
}
