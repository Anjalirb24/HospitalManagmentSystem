import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Myquery";
    // private coz noone outside can access this confidential data memebers
    // static  coz i dont have to create object in main method to access it
    // final coz the value should not change

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    Scanner scanner = new Scanner(System.in);
    try{
        Connection connection = DriverManager.getConnection(url, username, password);
        Patient patient = new Patient(connection, scanner);
        Doctor doctor = new Doctor(connection);
        while ( true) {
            System.out.println("HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("1.  Add patient");
            System.out.println("2.  View patient");
            System.out.println("3.  View Doctor");
            System.out.println("4.  Book Appointment");
            System.out.println("5.  Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                patient.addPatient();
                break;
                case 2:
                patient.viewPatients();
                break;
                case 3:
                doctor.viewDoctor();
                break;
                case 4:
                bookAppointment(patient, doctor, connection, scanner);
                break;
                case 5:
                    return;
                default :
                    System.out.println("Enter Valid Choice: ");
        }
    }
    }catch(SQLException e){
        e.printStackTrace();
    }
}

public static void bookAppointment(Patient patient,Doctor doctor, Connection connection, Scanner scanner){
    System.out.println("Enter Patient Id: ");
    int patientId = scanner.nextInt();
    System.out.println("Enter Doctor Id: ");
    int doctorId = scanner.nextInt();
    System.out.print("Enter Appointment Date(yyyy-mm-dd): " );
    String appointmentDate = scanner.next();
    if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
        if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
            String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ? ,?)";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                preparedStatement.setInt(1, patientId);
                preparedStatement.setInt(2, doctorId);
                preparedStatement.setString(3,appointmentDate);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0){
                    System.out.println("Appointment booked");
                }else{
                    System.out.println("Failed to book Appointment");
                }            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
        else{
            System.out.println("Doctor is not available on this date");
        }
    }else{
        System.out.println("Patient or Doctor not found");
    }
}
    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
            }else{
                return false;
            }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    
    return false;
}
}