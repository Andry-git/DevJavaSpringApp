package earth;

import earth.model.User;
import earth.service.UserService;
import java.util.Arrays;
import java.util.List;

/**
 * Главный класс.
 */
public class Main {
  public static void main(String[] args) {
    List<User> users = Arrays.asList(
        new User(1, "Иван", "Иванов", 25, "Россия"),
        new User(2, "Алиса", "Уайт", 30, "Канада"),
        new User(3, "Елена", "Брит", 22, "Великобритания"),
        new User(4, "Михаэль", "Камски", 35, "Канада"),
        new User(5, "Денис", "Сидоров", 10, "Россия"),
        new User(6, "Олег", "Панченко", 15, "Украина")
    );

    System.out.println("Пользователи, отсортированные по фамилии:");
    UserService.sortByLastName(users).forEach(System.out::println);

    System.out.println("\nПользователи, отсортированные по возрасту:");
    UserService.sortByAge(users).forEach(System.out::println);

    System.out.println("\nВсе пользователи старше 7 лет?\n" + UserService.allUsersOlderThan7(users));

    System.out.println("\nСредний возраст пользователей:\n" + UserService.averageAge(users));

    System.out.println(
        "\nКоличество уникальных стран проживания:\n" + UserService.countUniqueCountries(users));
  }
}