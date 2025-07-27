package org.ad.userapi.controller;

import org.ad.userapi.model.Country;
import org.ad.userapi.model.User;
import org.ad.userapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner initDatabase(UserRepository userRepository) {
    return args -> {
      if (userRepository.count() == 0) {
        userRepository.save(new User("Andrey", 33, Country.RUSSIA));
        userRepository.save(new User("Jack", 25, Country.USA));
        userRepository.save(new User("Hans", 40, Country.GERMANY));
        userRepository.save(new User("Pierre", 35, Country.FRANCE));
        userRepository.save(new User("Takashi", 28, Country.JAPAN));
        System.out.println("Test data initialized");
      }
    };
  }
}
