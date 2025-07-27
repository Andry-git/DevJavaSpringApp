package org.ad.userapi.repository;

import java.util.List;
import org.ad.userapi.model.Country;
import org.ad.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByCountryNotOrderByAgeAsc(Country country);
}
