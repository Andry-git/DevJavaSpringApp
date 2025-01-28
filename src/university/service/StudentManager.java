package university.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import university.model.Student;

/**
 * Класс для управления списком студентов.
 */
public class StudentManager {
  private List<Student> students;

  /**
   * Конструктор класса StudentManager.
   * Инициализирует список студентов.
   */
  public StudentManager() {
    this.students = new ArrayList<>();
  }

  /**
   * Добавляет нового студента в список.
   *
   * @param name   Имя студента.
   * @param group  Группа студента (например, "M-12" или "B-34").
   * @param course Курс студента.
   * @param grades Двумерный массив строк с оценками (например, {{"Математика", "5"}, {"Физика", "4"}}).
   */
  public void addStudent(String name, String group, int course, String[][] grades) {
    Student student = new Student(name, group, course);
    for (String[] grade : grades) {
      if (grade.length == 2) {
        String subject = grade[0];
        try {
          int score = Integer.parseInt(grade[1]);
          student.addGrade(subject, score);
        } catch (NumberFormatException e) {
          System.err.println("Ошибка: Неверный формат оценки для предмета " + subject +
              ". Оценка должна быть числом. Студент не будет добавлен");
          return;
        }
      } else {
        System.err.println("Ошибка: Неверный формат оценки для студента " + name +
            ". Оценка должна иметь вид {предмет, оценка}. Студент не будет добавлен");
        return;
      }
    }
    students.add(student);
  }

  /**
   * Удаляет студентов с низкой успеваемостью и переводит остальных на следующий курс.
   * При этом, если студент заканчивает программу (магистр 2 курс, бакалавр 4 курс) - удаляет студента
   */
  public void removeLowPerformingStudents() {
    Iterator<Student> iterator = students.iterator();
    while (iterator.hasNext()) {
      Student student = iterator.next();
      if ((int) student.getAverageGrade() <= 3) {
        iterator.remove();
      } else {
        updateStudentCourseAndGroup(student, iterator);
      }
    }
  }

  /**
   * Обновляет курс студента и группу, учитывая ограничения для магистров и бакалавров.
   * Если студент заканчивает обучение - удаляет его из списка.
   *
   * @param student  Студент, которого нужно обновить
   * @param iterator Итератор для удаления студента из списка, если он закончил обучение.
   */
  private void updateStudentCourseAndGroup(Student student, Iterator<Student> iterator) {
    int currentCourse = student.getCourse();
    String group = student.getGroup();
    char programType = group.charAt(0); // M - магистратура, B - бакалавриат
    int nextCourse = currentCourse + 1;
    String updatedGroup;

    if (programType == 'M' || programType == 'М') {
      if (nextCourse > 2) {
        iterator.remove();
        return; // Магистры заканчивают на 2 курсе
      }
      updatedGroup = group.substring(0, 2) + nextCourse + group.charAt(3);
      student.setGroup(updatedGroup);
    } else if (programType == 'B' || programType == 'В') {
      if (nextCourse > 4) {
        iterator.remove();
        return; // Бакалавры заканчивают на 4 курсе
      }
      updatedGroup = group.substring(0, 2) + nextCourse + group.charAt(3);
      student.setGroup(updatedGroup);
    }
    student.setCourse(nextCourse);
  }

  /**
   * Выводит в консоль информацию о всех студентах.
   */
  public void printAllStudents() {
    for (Student student : students) {
      student.printStudentInfo();
    }
  }

  /**
   * Выводит в консоль имена студентов, обучающихся на определенном курсе.
   *
   * @param course Курс, по которому нужно вывести список студентов.
   */
  public void printStudentsByCourse(int course) {
    for (Student student : students) {
      if (student.getCourse() == course) {
        System.out.println(student.getName());
      }
    }
  }
}
