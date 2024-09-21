import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// Класс User (базовый класс)
class User {
    String firstName;
    String lastName;
    String patronymic;

    public User(String firstName, String lastName, String patronymic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + patronymic;
    }

    public String getFirstName() {  // Добавлено для доступа к firstName
        return firstName;
    }
}

// Класс Student (наследуется от User)
class Student extends User {
    int studentId;

    public Student(String firstName, String lastName, String patronymic, int studentId) {
        super(firstName, lastName, patronymic);
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return super.toString() + ", ID: " + studentId;
    }
}

// Интерфейс UserView
interface UserView<T extends User> {
    void sendOnConsole(List<T> users);
}

// Класс StudentView
class StudentView implements UserView<Student> {

    @Override
    public void sendOnConsole(List<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }
    }
}

// Компаратор для пользователей (типизированный)
class UserComparator<T extends User> implements Comparator<T> {
    @Override
    public int compare(T user1, T user2) {
        return user1.getFirstName().compareTo(user2.getFirstName()); // Сортировка по имени
    }
}



// Интерфейс UserController
interface UserController<T extends User> {
    void create(List<T> users, T user);
}

// Класс StudentController (реализует UserController)
class StudentController implements UserController<Student> {
    private final UserView<Student> view; // Добавлено поле для view

    public StudentController(UserView<Student> view) { // Изменен конструктор
        this.view = view;
    }

    @Override
    public void create(List<Student> students, Student student) {
        students.add(student);
        // Вывод на консоль с помощью view (SOLID - Dependency Inversion Principle)
        view.sendOnConsole(students);
    }

    public void sortStudents(List<Student> students) {
        // Теперь используем типизированный компаратор
        Collections.sort(students, new UserComparator<>());
        view.sendOnConsole(students);
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        StudentView view = new StudentView(); // Создаем экземпляр StudentView
        StudentController controller = new StudentController(view); // Передаем view в конструктор

        controller.create(students, new Student("Иван", "Иванов", "Иванович", 1));
        controller.create(students, new Student("Петр", "Петров", "Петрович", 2));
        controller.create(students, new Student("Анна", "Сидорова", "Алексеевна", 3));

        System.out.println("Отсортированный список:");
        controller.sortStudents(students);



    }
}