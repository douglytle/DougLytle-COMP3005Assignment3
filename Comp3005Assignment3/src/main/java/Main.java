import java.sql.*;
import java.util.Scanner;

public class Main {
    public static String url = "jdbc:postgresql://localhost:5432/COMP3005A3";
    public static String user = "postgres";
    public static String password = "postgres";
    public static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the database\n");
            } else {
                System.out.println("Failed to connect to the database\n");
            }
            Scanner s = new Scanner(System.in);
            while(true) {
                System.out.println("\nEnter command:\ng: getAllStudents, a: addStudent, u: updateStudentEmail, d: deleteStudent, q: quit\n");
                String command = s.nextLine();
                switch (command) {
                    case "g":
                        getAllStudents();
                        break;
                    case "a":
                        System.out.println("Enter first name: ");
                        String firstName = s.nextLine();
                        System.out.println("Enter last name: ");
                        String lastName = s.nextLine();
                        System.out.println("Enter email: ");
                        String email = s.nextLine();
                        System.out.println("Enter enrollment date (YYYY-MM-DD): ");
                        String enrollmentDate = s.nextLine();
                        addStudent(firstName, lastName, email, enrollmentDate);
                        break;
                    case "u":
                        System.out.println("Enter student id: ");
                        int studentId = s.nextInt();
                        s.nextLine();
                        System.out.println("Enter new email: ");
                        String newEmail = s.nextLine();
                        updateStudentEmail(studentId, newEmail);
                        break;
                    case "d":
                        System.out.println("Enter student id: ");
                        int studentId2 = s.nextInt();
                        s.nextLine();
                        deleteStudent(studentId2);
                        break;
                    case "q":
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getAllStudents() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeQuery("SELECT * FROM students");
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            System.out.print(resultSet.getString("student_id") + " ");
            System.out.print(resultSet.getString("first_name") + " ");
            System.out.print(resultSet.getString("last_name") + " ");
            System.out.print(resultSet.getString("email") + " ");
            System.out.println(resultSet.getString("enrollment_date"));
        }
    }

    public static void addStudent(String first_name, String last_name, String email, String enrollment_date) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, Date.valueOf(enrollment_date));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully added student: " + first_name + " " + last_name + ", " + email + ", enrolled on: " + enrollment_date);
            } else {
                System.out.println("Could not add student");
            }
        }

    }

    public static void updateStudentEmail(int student_id, String new_email) throws SQLException {
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, new_email);
            preparedStatement.setInt(2, student_id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully updated email to " + new_email + " for student with id: " + student_id);
            } else {
                System.out.println("Could not update email for student with id: " + student_id);
            }
        }

    }

    public static void deleteStudent(int student_id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, student_id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully deleted student with id: " + student_id);
            } else {
                System.out.println("Could not delete student with id: " + student_id);
            }
        }

    }
}
