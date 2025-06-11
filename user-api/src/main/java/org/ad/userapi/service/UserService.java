package org.ad.userapi.service;

import java.util.List;
import org.ad.userapi.model.User;

public interface UserService {
  List<User> getAllUsers();
  User createUser(User user);
}
