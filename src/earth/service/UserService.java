package earth.service;

import earth.model.User;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для обработки списка пользователей.
 */
public class UserService {

  /**
   * Возвращает список пользователей, отсортированных по фамилии.
   * @param users Список пользователей.
   * @return Отсортированный список пользователей.
   */
  public static List<User> sortByLastName(List<User> users) {
    return users.stream()
        .sorted(Comparator.comparing(User::getLastName))
        .collect(Collectors.toList());
  }

  /**
   * Возвращает список пользователей, отсортированных по возрасту.
   * @param users Список пользователей.
   * @return Отсортированный список пользователей.
   */
  public static List<User> sortByAge(List<User> users) {
    return users.stream()
        .sorted(Comparator.comparing(User::getAge))
        .collect(Collectors.toList());
  }


  /**
   * Проверяет, что возраст всех пользователей больше 7 лет.
   * @param users Список пользователей.
   * @return true, если возраст всех пользователей больше 7, иначе false.
   */
  public static boolean allUsersOlderThan7(List<User> users) {
    return users.stream().allMatch(user -> user.getAge() > 7);
  }

  /**
   * Вычисляет средний возраст всех пользователей.
   * @param users Список пользователей.
   * @return Средний возраст пользователей.
   */
  public static double averageAge(List<User> users) {
    return users.stream()
        .mapToInt(User::getAge)
        .average()
        .orElse(0); // Возвращаем 0, если список пуст
  }

  /**
   * Вычисляет количество уникальных стран проживания среди пользователей.
   * @param users Список пользователей.
   * @return Количество уникальных стран.
   */
  public static int countUniqueCountries(List<User> users) {
    return (int) users.stream()
        .map(User::getCountry)
        .distinct()
        .count();
  }
}