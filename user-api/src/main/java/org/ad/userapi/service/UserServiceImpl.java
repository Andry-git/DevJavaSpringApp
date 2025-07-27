package org.ad.userapi.service;

import java.util.List;
import org.ad.userapi.model.Country;
import org.ad.userapi.model.User;
import org.ad.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public List<User> getUsersNotFromCountrySortedByAge(String country) {
    return userRepository.findByCountryNotOrderByAgeAsc(Country.valueOf(country));
  }
}
