package org.etf.webshopbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.NotFoundException;
import org.etf.webshopbackend.model.entity.ContactSupport;
import org.etf.webshopbackend.model.entity.User;
import org.etf.webshopbackend.model.mapper.GenericMapper;
import org.etf.webshopbackend.model.request.ContactSupportRequest;
import org.etf.webshopbackend.repository.ContactSupportRepository;
import org.etf.webshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ContactSupportService {

  private final ContactSupportRepository contactSupportRepository;
  private final UserRepository userRepository;
  private final GenericMapper<ContactSupportRequest, ContactSupport, ContactSupport> contactSupportMapper;

  public void sendMessage(ContactSupportRequest request, Long userId) {
    // TODO: repository
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(User.class, userId));
    ContactSupport contactSupport = contactSupportMapper.fromRequest(request, ContactSupport.class);
    contactSupport.setUser(user);

    contactSupportRepository.save(contactSupport);
  }
}
