package org.ad.userapi.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ad.userapi.model.User;
import org.ad.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user-api/v1")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @PostMapping("/users")
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @GetMapping("/additional-info")
  public List<User> getUsersNotFromCountrySortedByAge(@RequestParam String country) {
    return userService.getUsersNotFromCountrySortedByAge(country);
  }
}
