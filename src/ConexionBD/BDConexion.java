package ConexionBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author scume
 */
public class BDConexion {

    public static Connection getConnection() {
        Connection connection = null;
        try {

            String url = "jdbc:mysql://localhost:3306/bdfarmaciabethesda";  
            String usuario = "root"; 
            String contraseña = "Cuborubik@7"; 
            
            connection = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }
    public static void main(String[] args) {

    }

}
