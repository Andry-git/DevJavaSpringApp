package university.model;

/**
 * Класс, представляющий оценку студента.
 */
public class Grade {
  private String subject;
  private int grade;

  /**
   * Конструктор класса Grade.
   *
   * @param subject Название предмета.
   * @param grade   Оценка по предмету.
   */
  public Grade(String subject, int grade) {
    this.subject = subject;
    this.grade = grade;
  }

  /**
   * Возвращает название предмета.
   *
   * @return Название предмета.
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Возвращает оценку.
   *
   * @return Оценка.
   */
  public int getGrade() {
    return grade;
  }
}
