package university.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий студента.
 */
public class Student {
  private String name;
  private String group;
  private int course;
  private List<Grade> grades;

  /**
   * Конструктор класса Student.
   *
   * @param name   Имя студента.
   * @param group  Группа студента (например, "M-12" или "B-34").
   * @param course Курс студента.
   */
  public Student(String name, String group, int course) {
    this.name = name;
    this.group = group;
    this.course = course;
    this.grades = new ArrayList<>();
  }

  /**
   * Возвращает имя студента.
   *
   * @return Имя студента.
   */
  public String getName() {
    return name;
  }

  /**
   * Возвращает группу студента.
   *
   * @return Группа студента.
   */
  public String getGroup() {
    return group;
  }

  /**
   * Устанавливает новую группу студента.
   *
   * @param group Новая группа студента
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Возвращает курс студента.
   *
   * @return Курс студента.
   */
  public int getCourse() {
    return course;
  }

  /**
   * Устанавливает новый курс студента.
   *
   * @param course Новый курс студента.
   */
  public void setCourse(int course) {
    this.course = course;
  }

  /**
   * Возвращает список оценок студента.
   *
   * @return Список оценок студента.
   */
  public List<Grade> getGrades() {
    return grades;
  }

  /**
   * Добавляет оценку студенту.
   *
   * @param subject Название предмета.
   * @param grade   Оценка по предмету.
   */
  public void addGrade(String subject, int grade) {
    grades.add(new Grade(subject, grade));
  }

  /**
   * Вычисляет средний балл студента.
   *
   * @return Средний балл студента.
   */
  public double getAverageGrade() {
    if (grades.isEmpty()) {
      return 0;
    }
    double sum = 0;
    for (Grade grade : grades) {
      sum += grade.getGrade();
    }
    return sum / grades.size();
  }

  /**
   * Выводит в консоль информацию о студенте.
   */
  public void printStudentInfo() {
    System.out.println("Имя: " + name);
    System.out.println("Группа: " + group);
    System.out.println("Курс: " + course);
    System.out.println("Оценки:");
    for (Grade grade : grades) {
      System.out.printf("  - %s: %d%n", grade.getSubject(), grade.getGrade());
    }
    System.out.println();
  }
}
