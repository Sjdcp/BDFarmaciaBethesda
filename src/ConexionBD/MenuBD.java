package ConexionBD;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
/**
 *
 * @author scume
 */
public class MenuBD {
  
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            System.out.println("");
            System.out.println("********** Menú principal **********");
            System.out.println("");
            System.out.println("1.....Ingresar producto");
            System.out.println("2.....Mostrar productos");
            System.out.println("3.....Buscar producto");
            System.out.println("4.....Modificar producto");
            System.out.println("5.....Eliminar producto");
            System.out.println("6.....Salir del menú principal");
            System.out.print("Seleccione una opción del menú: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine(); 
            } catch (Exception e) {
                System.out.println("Error en la entrada. Por favor, ingrese un número válido.");
                sc.nextLine(); 
                opcion = -1;   
            }

            switch (opcion) {
                case 1:
                    ingresarProducto();
                    break;
                case 2:
                    mostrarProductos();
                    break;
                case 3:
                    buscarProducto();
                    break;
                case 4:
                    modificarProducto();
                    break;
                case 5:
                    eliminarProducto();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }
        } while (opcion != 6);

        sc.close(); 
    }

    public static void ingresarProducto() {
        try (Connection conn = BDConexion.getConnection()) {

            System.out.print("Ingrese código del producto: ");
            String codigoProducto = sc.nextLine();

            System.out.print("Ingrese nombre del producto: ");
            String nombreProducto = sc.nextLine();

            System.out.print("Ingrese precio unitario: ");
            double precioUnitario = sc.nextDouble();

            System.out.print("Ingrese cantidad del producto: ");
            int cantidadProducto = sc.nextInt();
            sc.nextLine();  

        
            String fechaVencimiento = null;
            while (true) {
                System.out.print("Ingrese fecha de vencimiento (YYYY-MM-DD): ");
                String inputFecha = sc.nextLine();
                if (validarFecha(inputFecha)) {
                    fechaVencimiento = inputFecha;
                    break; 
                } else {
                    System.out.println("Formato de fecha incorrecto. Por favor, ingrese una fecha válida (YYYY-MM-DD).");
                }
            }

            String sql = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigoProducto);
            ps.setString(2, nombreProducto);
            ps.setDouble(3, precioUnitario);
            ps.setInt(4, cantidadProducto);
            ps.setDate(5, Date.valueOf(fechaVencimiento));

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Producto ingresado exitosamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarProductos() {
        try (Connection conn = BDConexion.getConnection()) {
            String sql = "SELECT * FROM producto";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String codigoProducto = resultSet.getString("codigoProducto");
                String nombreProducto = resultSet.getString("nombreProducto");
                double precioUnitario = resultSet.getDouble("precioUnitario");
                int cantidadProducto = resultSet.getInt("cantidadProducto");
                Date fechaVencimiento = resultSet.getDate("fechaVencimiento");

                System.out.printf("%s, %s, %.2f, %d, %s\n", codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buscarProducto() {
        try (Connection conn = BDConexion.getConnection()) {

            System.out.print("Ingrese el código del producto a buscar: ");
            String codigoProducto = sc.nextLine();

            String sql = "SELECT * FROM producto WHERE codigoProducto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigoProducto);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String nombreProducto = resultSet.getString("nombreProducto");
                double precioUnitario = resultSet.getDouble("precioUnitario");
                int cantidadProducto = resultSet.getInt("cantidadProducto");
                Date fechaVencimiento = resultSet.getDate("fechaVencimiento");

                System.out.printf("Producto encontrado: %s, %.2f, %d, %s\n", nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento);
            } else {
                System.out.println("Producto no encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modificarProducto() {
        try (Connection conn = BDConexion.getConnection()) {

            System.out.print("Ingrese el código del producto a modificar: ");
            String codigoProducto = sc.nextLine();

            System.out.print("Ingrese nuevo precio unitario: ");
            double nuevoPrecio = sc.nextDouble();
            sc.nextLine();

            String sql = "UPDATE producto SET precioUnitario = ? WHERE codigoProducto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, nuevoPrecio);
            ps.setString(2, codigoProducto);

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Producto actualizado exitosamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarProducto() {
        try (Connection conn = BDConexion.getConnection()) {

            System.out.print("Ingrese el código del producto a eliminar: ");
            String codigoProducto = sc.nextLine();

            String sql = "DELETE FROM producto WHERE codigoProducto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigoProducto);

            int filasEliminadas = ps.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Producto eliminado exitosamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validarFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);  // Desactivar la validación permisiva
        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
