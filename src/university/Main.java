package university;

import university.service.StudentManager;

/**
 * Главный класс.
 */
public class Main {

  public static final String ANSI_RESET = "\u001B[0m";
  ;
  public static final String ANSI_GREEN = "\u001B[32m";

  public static void main(String[] args) {
    //Колекция содержащая объекты класса Student.
    StudentManager students = new StudentManager();
    String[][] grades1 = {
        {"Математическая логика", "5"},
        {"Эконометрика", "4"},
        {"Теория чисел", "4"}
    };
    students.addStudent("Андрей", "M-22", 2, grades1);

    String[][] grades2 = {
        {"Программирование", "3"},
        {"Научная наука", "5"},
        {"Математика", "5"},
        {"Публикация", "4"}
    };
    students.addStudent("Иван", "B-24", 2, grades2);

    String[][] grades3 = {
        {"Физика", "4"},
        {"Химия", "5"},
        {"Биология", "3"}
    };
    students.addStudent("Мария", "B-10", 1, grades3);

    String[][] grades4 = {
        {"Литература", "5"},
        {"Иностранный язык", "4"},
        {"География", "5"},
        {"История", "3"}
    };
    students.addStudent("Сергей", "M-11", 1, grades4);

    String[][] grades5 = {
        {"Алгебра", "3"},
        {"Геометрия", "2"},
        {"Физкультура", "5"}
    };
    students.addStudent("Екатерина", "B-12", 1, grades5);

    String[][] grades6 = {
        {"Программирование", "4"},
        {"Системный анализ", "3"},
        {"Количественные методы в маркетинге", "5"},
        {"Управление проектами", "4"}
    };
    students.addStudent("Дмитрий", "B-41", 4, grades6);

    String[][] grades7 = {
        {"Экономика", "5"},
        {"Маркетинг", "4"},
        {"Финансовый учет", "5"},
        {"Статистика", "4"}
    };
    students.addStudent("Анна", "B-35", 3, grades7);

    String[][] grades8 = {
        {"Теория вероятностей", "2"},
        {"Математический анализ", "3"},
        {"Криптография", "4"}
    };
    students.addStudent("Олег", "B-31", 3, grades8);

    System.out.println("\nСписок студентов перед обработкой:");
    students.printAllStudents();

    System.out.println(ANSI_GREEN + "Студенты 3 курса без учёта обработки:" + ANSI_RESET);
    students.printStudentsByCourse(3);

    students.removeLowPerformingStudents();

    System.out.println("\nСписок студентов после обработки:");
    students.printAllStudents();

    System.out.println(ANSI_GREEN + "Студенты 3 курса с учётом обработки:" + ANSI_RESET);
    students.printStudentsByCourse(3);
  }
}
