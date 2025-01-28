package earth.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Класс, представляющий пользователя.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private int id;
  private String firstName;
  private String lastName;
  private int age;
  private String country;
}
