import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Doctor {

        private Connection connection;
    
    
        public Doctor(Connection connection){
            this.connection = connection;
    
        }
    
        public void viewDoctor(){
            String query = "select * from doctors";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Dcotors: ");
                System.out.println("+------------+----------------------+---------------------+");
                System.out.println("| doctor Id |  Name                 |  Specialization     |");
                System.out.println("+------------+----------------------+---------------------+");
    
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String specialization =  resultSet.getString("specialization");
                    System.out.printf("|%-12s|%-20s|%-18s|\n", id, name, specialization);
                    System.out.println("+------------+-------------------------+----------------------+");
    
                }
               
            }
            catch(SQLException e){
                e.printStackTrace();
            }
    }
            public boolean getDoctorById(int id){
                String query = "SELECT * FROM doctors WHERE id = ?";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next()){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
    
                catch (SQLException e){
                    e.printStackTrace();
                }
                return false;
            }
    }
    
    


