package org.ad.userapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ad.userapi.model.User;
import org.ad.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }
}
