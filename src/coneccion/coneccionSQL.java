package coneccion;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.SQLException;


public class coneccionSQL {
    public String db = "drogeria";
    public String url = "jdbc:mysql://localhost/"+db;
    public String user = "root";
    public String pass = "";
    
    public coneccionSQL (){
        
    }
    public Connection Conectar(){
 Connection link = null;
     try {
         Class.forName("com.mysql.jdbc.Driver");
         link = DriverManager.getConnection(this.url, this.user, this.pass);
     } catch (Exception e) {
     
 JOptionPane.showMessageDialog(null,"Error de Conexion:\n1) Conecte xammp" );
 }
 return link;
    }

    public Connection conexionMySql() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Connection conexionPostgreSql() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
