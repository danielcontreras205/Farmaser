// para generar un Jframe a la medida que uno quiera, es por medio de de las obciones de frame y dejarlo NULL
// despues espesificar las medidad del Jframe 
// ctrl + chif + c para codumentar
package farmaser;

import coneccion.coneccionSQL;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.mxrck.autocompleter.TextAutoCompleter;
import static farmaser.login.jPanel1;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.codec.digest.DigestUtils; // libreria para incriptar las contraseñas
        
import net.sf.jasperreports.view.JasperViewer;


public class Principal extends javax.swing.JFrame {

    coneccionSQL cc = new coneccionSQL();
    Connection cn = cc.Conectar();   // crear conexion con la base de datos

    DefaultTableModel model = new DefaultTableModel(); // crear modelo del jTable

    private variable.codigo metodo = new variable.codigo(); // parte de donde se llama el metodo creado para la busqueda para el combobox

    public Principal() {
        initComponents();

        TextAutoCompleter variable = new TextAutoCompleter(TexBuscarMedicamentoCodigo);
        this.setTitle("Farmaser");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/LOGO.png")).getImage());
        this.setBounds(150, 8, 1025, 690); // pocision y tamaño del Jframe
        this.ComboUbicacionStan.setModel(metodo.llena_combo()); // se agrega todo lo que se hace en el metodo llamado "llena_combo()"
        this.ComboLaboratorio.setModel(metodo.llenar_comboLaboratorio());
        Jtexcantidad.setEnabled(false);
        

        jPanel1.setBounds(150, 8, 1025, 690);
        
        BotonesPrincipales.setBounds(10, 8, 200, 480);
        jLabelBienvenido.setBounds(59, 500, 100, 10);
        TexUser.setBounds(59, 525, 100, 20);
        BotonSalir.setBounds(70, 550, 80, 53);
        BotonCambiarPassword.setBounds(40, 620, 150, 20);

        BotonesBuscar.setBounds(220, 8, 780, 120);
        BuscarMedicamento.setBounds(220, 127, 780, 520);
        BuscarLabora.setBounds(220, 127, 780, 520);
        BuscarEnStock.setBounds(220, 127, 780, 520);

        BotonesNuevo.setBounds(220, 8, 780, 120);
        AgregarUsuario.setBounds(220, 127, 780, 520);
        AgregarStan.setBounds(220, 127, 780, 520);
        AgregarMedicamento.setBounds(220, 127, 780, 520);
        AgregarLaboratorio.setBounds(220, 127, 780, 520);
        
        BotonesInformacion.setBounds(220, 8, 780, 120); // estipular posicion de la interfas
        ReporteFechas.setBounds(220, 127, 780, 520);
        ReporteFacturas.setBounds(220, 127, 780, 520);

        TexUser.setVisible(true); // metodo para visualizar
        BotonesNuevo.setVisible(false);
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);

        BotonesBuscar.setVisible(false);
        BuscarMedicamento.setVisible(false);
        BuscarLabora.setVisible(false);
        BuscarEnStock.setVisible(false);
        jTexTotal.setEnabled(false);

        BotonesInformacion.setVisible(false);
        ReporteFechas.setVisible(false);
        ReporteFacturas.setVisible(false);

        TexCodigoGavetas.setEnabled(false);
        TexCodigoLaboratorio_Medicamento.setEnabled(false);
        TexCodigoGaveta_Medicamento.setEnabled(false);

    }

    void CargarJtabletUser() { 
        model.setRowCount(0); // borara los datos que ya hay en el tablet para no acumolar
        TableColumn ColubnaCargo = jTable_Usuarios.getColumnModel().getColumn(4); // metodo para crear el jcombo box
        JComboBox comboBox = new JComboBox(); // Crea jcombo box
        comboBox.addItem("Administrador");
        comboBox.addItem("Empleado");
        ColubnaCargo.setCellEditor(new DefaultCellEditor(comboBox));// pone el jcombo box
        String[] columnas = {"Cedula", "Nombre", "1° Apellido", "2° Apellido", "Cargo"};
        String[] registros = new String[5];
        String sql = "SELECT a.cc, a.Nombre, a.Primer_Apellido, a.Segundo_Apellido, b.Nombre_Cargo \n"
                + "FROM  persona a, cargo b \n"
                + "WHERE b.id = a.id_Cargo";
        model = new DefaultTableModel(null, columnas);
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                registros[0] = es.getString("cc");
                registros[1] = es.getString("Nombre");
                registros[2] = es.getString("Primer_Apellido");
                registros[3] = es.getString("Segundo_Apellido");
                registros[4] = es.getString("Nombre_cargo");
                model = (DefaultTableModel) this.jTable_Usuarios.getModel();
                model.addRow(registros);
                jTable_Usuarios.setModel(model);
                model.fireTableDataChanged();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        jTable_Usuarios.setModel(model);
    }

    void limpiaCampos() {
        TexNombre.setText("");
        TexCedula.setText("");
        TexPrimerApellido.setText("");
        TexSegundoApellido.setText("");
        TexUsuario.setText("");
        TexPassword.setText("");
        TexPasswordRepetir.setText("");
        ComboBoxCargo.setSelectedIndex(0); // primer item 
        TexCorreo.setText("");       

    }

    void limpiaTabla() {
        DefaultTableModel temp;
        try {
            temp = (DefaultTableModel) TablaStock.getModel();
            int a = temp.getRowCount();
            for (int i = 0; i < a; i++) {
                temp.removeRow(0); //aquí estaba el error, antes pasaba la i como parametro.... soy un bacín  XD
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    void limpiaTablaLaboratorio() {
        DefaultTableModel temp;
        try {
            temp = (DefaultTableModel) jTablaCantidadLaboratorios.getModel();
            int a = temp.getRowCount();
            for (int i = 0; i < a; i++) {
                temp.removeRow(0); //aquí estaba el error, antes pasaba la i como parametro.... soy un bacín  XD
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
        
    void Actualizarpersona(String colubna, String DatoActualizar, String Cedula) {

        String sql = "UPDATE persona set " + colubna + " = '" + DatoActualizar + "' WHERE cc = '" + Cedula + "'";

        try {
            Statement st = cn.createStatement();
            int es = st.executeUpdate(sql); // executeUpdate eliminar actualizar y insertar

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    } // metodo para actualizar dato    

    void crear_Gaveta() {
        int z = Integer.parseInt(String.valueOf(NumeroGabeta.getSelectedItem())); // captura valor y lo combierte String

        int y = z - 1;

        for (int i = 0; i <= y; i++) { // creacion de el codigo de la gaveta
            int gaveta = i + 0;
            int e = Integer.parseInt(TexCodigoGavetas.getText());
            int E = e / 10;
            int G = E + i;
            String Key;
            String Gaveta;

            Key = String.valueOf(G);
            Gaveta = String.valueOf(gaveta);

            try { //guardar los codigos de las gavetas

                PreparedStatement pps = cn.prepareStatement("Insert INTO Gaveta (id, No_Gaveta, id_Stan)" + "VALUES(?,?,?)");
                pps.setString(1, Key);
                pps.setString(2, Gaveta);
                pps.setString(3, TexCodigoStand.getText());

                pps.executeUpdate();

            } catch (SQLException | HeadlessException ex) {

                JOptionPane.showMessageDialog(null, ex);
            }

        };
        JOptionPane.showMessageDialog(null, "Datos de Gaveta ingresados correctamente");
    }

    void llenar_jtexCodigoLaboratorio(String Dato) {
        String sql = "select a.id FROM laboratorio a WHERE a.Nombre like '" + Dato + "'";
        String Dato2 = "";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                Dato2 = es.getString("id");
                TexCodigoLaboratorio_Medicamento.setText(Dato2);

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    } // buscar y llenar con codigo de los lavoratorios

    void llenar_JtexCodigoGaveta(String CodigoStan, int CodigoGaveta) {
        String sql = "call CodigoGaveta('" + CodigoStan + "'," + CodigoGaveta + ")";
        String Dato2 = " ";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                Dato2 = es.getString("id");
                TexCodigoGaveta_Medicamento.setText(Dato2);

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    } // buscar y llenar con codigo de las Gavetas

    void Buscar_por_Comercial(String NombreComercial) {
        String[] columnas = {"Generico", "comercial", "cantidad", "Stan", "Gaveta"};
        String[] registros = new String[50];
        String sql = "SELECT a.NombreGenerico, a.NombreComercial, a.Cantidad, b.Letra_Stan, c.No_gaveta \n"
                + "from medicamentos a, stan b, gaveta c, gaveta_medicamento d\n"
                + "WHERE a.NombreComercial LIKE '%" + NombreComercial + "%' and  a.id = d.id_Medicamentos and c.id = d.id_Gaveta and c.id_Stan = b.id";

        model = new DefaultTableModel(null, columnas);

        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);

            while (es.next()) {
                registros[0] = es.getString("NombreGenerico");
                registros[1] = es.getString("NombreComercial");
                registros[2] = es.getString("Cantidad");
                registros[3] = es.getString("Letra_Stan");
                registros[4] = es.getString("No_gaveta");

                model.addRow(registros);
            }
            TablaBusquedaMedicamento.setModel(model);
        } catch (SQLException ex) {
            // JOptionPane.showMessageDialog(null, ex);//
            JOptionPane.showMessageDialog(null, ex);
        }
    } // metodo de busqueda por medio de Jtex

    void Buscar_por_Generico(String NombreGenerico) {

        String[] columnas = {"Generico", "comercial", "cantidad", "Stan", "Gaveta"};
        String[] registros = new String[50];
        String sql = "SELECT a.NombreGenerico, a.NombreComercial, a.Cantidad, b.Letra_Stan, c.No_gaveta \n"
                + "from medicamentos a, stan b, gaveta c, gaveta_medicamento d\n"
                + "WHERE a.NombreGenerico LIKE '%" + NombreGenerico + "%' and  a.id = d.id_Medicamentos and c.id = d.id_Gaveta and c.id_Stan = b.id";

        model = new DefaultTableModel(null, columnas);

        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);

            while (es.next()) {
                registros[0] = es.getString("NombreGenerico");
                registros[1] = es.getString("NombreComercial");
                registros[2] = es.getString("Cantidad");
                registros[3] = es.getString("Letra_Stan");
                registros[4] = es.getString("No_gaveta");

                model.addRow(registros);
            }
            TablaBusquedaMedicamento.setModel(model);
        } catch (SQLException ex) {
            // JOptionPane.showMessageDialog(null, ex);//
            JOptionPane.showMessageDialog(null, ex);
        }
    } // metodo de busqueda por medio de Jtex

    void BuscarLaboratorio(String NombreLabo) {
        String[] columnas = {"Codigo", "Nombre", "Telefono"};
        String[] registros = new String[50];
        String sql = "SELECT a.id, a.Nombre, b.No_telefono FROM laboratorio a, telefono b WHERE a.Nombre LIKE '%" + NombreLabo + "%' and a.id = b.id_Laboratorio";

        model = new DefaultTableModel(null, columnas);

        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);

            while (es.next()) {
                registros[0] = es.getString("id");
                registros[1] = es.getString("Nombre");
                registros[2] = es.getString("No_telefono");

                model.addRow(registros);
            }
            TablaBusquedaLaboratorio.setModel(model);
        } catch (SQLException ex) {
            // JOptionPane.showMessageDialog(null, ex);//
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    void Informacion_medicamento(String medicamento) {

        String sql = "SELECT a.NombreGenerico, a.NombreComercial, a.Funcion FROM medicamentos a WHERE a.NombreGenerico LIKE '%" + medicamento + "%'";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {

                String info = es.getString("Funcion");
                JOptionPane.showMessageDialog(null, info);

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    void ProductoStock(String Id) {
        String[] columnas = {"Codigo", "Generico", "comercial", "cantidad", "Precio_medicamento", "venta", "total"};
        String[] registros = new String[7];
        String sql = "SELECT a.id, a.NombreGenerico, a.NombreComercial, a.Cantidad, a.Precio_medicamento \n"
                + "from medicamentos a\n"
                + "WHERE a.id LIKE '" + Id + "%'";
        String v = "1";

        model = new DefaultTableModel(null, columnas);

        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);

            while (es.next()) {
                registros[0] = es.getString("id");
                registros[1] = es.getString("NombreGenerico");
                registros[2] = es.getString("NombreComercial");
                registros[3] = es.getString("Cantidad");
                registros[4] = es.getString("Precio_medicamento");
                registros[5] = v;
                registros[6] = es.getString("Precio_medicamento");

                model = (DefaultTableModel) this.TablaStock.getModel();
                model.addRow(registros);
            }
          
            
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);
        }
        TablaStock.setModel(model);
    }

    void TotalVenta() {
        int sumatoria1 = 0;
        int totalRow = TablaStock.getRowCount();
        totalRow -= 1;
        for (int i = 0; i <= (totalRow); i++) {
            int sumatoria = Integer.valueOf(String.valueOf(TablaStock.getValueAt(i, 6))); //en la parte de arriba indica el primer parametro la fila y el segundo la columna la cual estaras //manejando
            sumatoria1 += sumatoria;

            System.out.println("" + sumatoria1);
            jTexTotal.setText(String.valueOf(sumatoria1));

        }
    }
    
    void ActualizarStock(int valor, String codigo) {
        if (valor < 0) {
            JOptionPane.showMessageDialog(null, "NO se puede realizar venta, Error en el STOCk ");
        } else {
            String sql = "call actualizarStock('" + codigo + "'," + valor + ")";
            try {
                Statement st = cn.createStatement();
                ResultSet es = st.executeQuery(sql);
                JOptionPane.showMessageDialog(null, "Venta realizada");
            } catch (SQLException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    
    void RealizarVenta(String IdVenta, String FechaVenta, String Hora, String ValorVenta, String NikeName) {
        try {
            String sql = "SELECT a.cc FROM persona a WHERE a.user like '" + NikeName + "%'";
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            if (es.next()) {
                String Cedula = es.getString("cc");
                PreparedStatement pps = cn.prepareStatement("Insert INTO ventas (id, Fecha, Hora_Venta, cc_persona, Total_Venta)" + "VALUES(?,?,?,?,?)");
                pps.setString(1, IdVenta);
                pps.setString(2, FechaVenta);
                pps.setString(3, Hora);
                pps.setString(4, Cedula);
                pps.setString(5, ValorVenta);
                pps.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "1" + ex);
        }

    }  
    
    void GuardarVentaMedicamentos(String IdVentas, String Codigo, String Cantidad){
        try {
            PreparedStatement pps = cn.prepareStatement("Insert INTO venta_medicamento (id_Ventas,id_medicamentos,Cantidad)" + "VALUES(?,?,?)");
            pps.setString(1, IdVentas);
            pps.setString(2, Codigo);
            pps.setString(3, Cantidad);
            pps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "1" + ex);
        }

    }
    
    void ReporteFactura(String ID) {
        try {
            JasperReport reporte = null;
            String path = "src\\reportes\\factura1.jasper";
            Map Perametro1 = new HashMap();
            Perametro1.put("CodigoFactura", ID);
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint1 = JasperFillManager.fillReport(path, Perametro1, cn);
            JasperViewer view = new JasperViewer(jprint1, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "La Factura No Existe");
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void ReporteFacturasHistorial(String factura){
        try {
            JasperReport reporte = null;
            String path = "src\\reportes\\VentasEliminadas.jasper";
            Map Perametro1 = new HashMap();
            Perametro1.put("Codigo", factura);
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint1 = JasperFillManager.fillReport(path, Perametro1, cn);
            JasperViewer view = new JasperViewer(jprint1, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "La Factura No Existe");
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void llenar_Jtabla_medicamento(String valor){

    String[] columnas = {"Codigo", "Nombre Generico", "Nombre comercial", "Cantidad"};
        String[] registros = new String[50];
        String sql = "SELECT b.id, b.NombreGenerico, b.NombreComercial, b.Cantidad FROM laboratorio a, medicamentos b WHERE a.Nombre LIKE '" + valor + "%' and b.id_Laboratorio = a.id";

        model = new DefaultTableModel(null, columnas);
         
         

        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);

            while (es.next()) {
                registros[0] = es.getString("id");
                registros[1] = es.getString("NombreGenerico");
                registros[2] = es.getString("NombreComercial");
                registros[3] = es.getString("Cantidad");

                model.addRow(registros);
            }
            jTablaCantidadLaboratorios.setModel(model);
        } catch (SQLException ex) {
            // JOptionPane.showMessageDialog(null, ex);//
            JOptionPane.showMessageDialog(null, ex);
        }
   
}

    void llenar_TablaBackUp(String Cedula) {

        String sql = "call TablaBackup('" + Cedula + "%')";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        JOptionPane.showMessageDialog(null, "Los Datos del la presona " + Cedula + " se han guardado en la tabla BackUp ");

    }

    void validacionCaracteristicas(java.awt.event.KeyEvent evento) {
        if (evento.getKeyChar() >= 33 & evento.getKeyChar() <= 64
                || evento.getKeyChar() >= 91 && evento.getKeyChar() <= 96
                || evento.getKeyChar() >= 123 && evento.getKeyChar() <= 208
                || evento.getKeyChar() >= 210 && evento.getKeyChar() <= 240
                || evento.getKeyChar() >= 242 && evento.getKeyChar() <= 255) {
            evento.consume();
            JOptionPane.showMessageDialog(rootPane,  "No se Permiten Caracteres Especiales");
        }

        //System.out.println("Soy la letra: " + evento.getKeyChar() + "y mi numero ASCII corresponde a: " + evento.getKeyChar() + 0);
    } // revisar caracteres en lista https://ascii.cl/es/codigos-html.htm

    void validarcorreo(java.awt.event.KeyEvent evento) {
        if (evento.getKeyChar() >= 32 && evento.getKeyChar() <= 44
                || evento.getKeyChar() == 47
                || evento.getKeyChar() >= 58 && evento.getKeyChar() <= 63
                || evento.getKeyChar() >= 91 && evento.getKeyChar() <= 94
                || evento.getKeyChar() == 96
                || evento.getKeyChar() >= 123 && evento.getKeyChar() <= 255) {
            evento.consume();
            JOptionPane.showMessageDialog(this, "No se permite ese caracter en especifico");

        }
    } // funcion que permite revisar si tiene el punto y el @
    
    void validarcampos() {
        if (TexCedula.getText().isEmpty()) {
            jLabel39.setText("Campo Obligatorio");
        } else {
            jLabel39.setText("");
        }
        if (TexNombre.getText().isEmpty()) {
            jLabel40.setText("Campo Obligatorio");
        } else {
            jLabel40.setText("");
        }

        if (TexPrimerApellido.getText().isEmpty()) {
            jLabel41.setText("Campo Obligatorio");
        } else {
            jLabel41.setText("");
        }
        if (TexSegundoApellido.getText().isEmpty()) {
            jLabel42.setText("Campo Obligatorio");
        } else {
            jLabel42.setText("");
        }
                if (TexUsuario.getText().isEmpty()) {
            jLabel44.setText("Campo Obligatorio");
        } else {
            jLabel44.setText("");
        }
        if (TexPassword.getText().isEmpty()) {
            jLabel43.setText("Campo Obligatorio");
        } else {
            jLabel43.setText("");
        }
        
        if (TexCorreo.getText().isEmpty()) {
            jLabel45.setText("Campo Obligatorio");
        } else if (!TexCorreo.getText().contains("@") || !TexCorreo.getText().contains(".")) {
            jLabel45.setText("Correo No Valido");
        } else {
            jLabel45.setText("");
        }

    } // combierte los jlabel en condiciones para que los datos sean correctos
    
    void habilitarboton() {
        if (TexCedula.getText().isEmpty()
                || TexNombre.getText().isEmpty()
                || TexPrimerApellido.getText().isEmpty()
                || TexSegundoApellido.getText().isEmpty()
                || TexCorreo.getText().isEmpty()
                || TexPassword.getText().isEmpty()
                || TexPasswordRepetir.getText().isEmpty()
                || !jLabel45.getText().isEmpty()) {
            GuardarUsuarios.setEnabled(false);

        } else {
            GuardarUsuarios.setEnabled(true);

        }
    } // bloque el boton hasta que los datos esten llenos

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupos = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        BotonesPrincipales = new javax.swing.JPanel();
        BotonNuevo = new javax.swing.JButton();
        BotonBuscar = new javax.swing.JButton();
        BotonInformacion = new javax.swing.JButton();
        BotonesNuevo = new javax.swing.JPanel();
        NuevoUsuario = new javax.swing.JButton();
        NuevoStan = new javax.swing.JButton();
        NuevoMedicamento = new javax.swing.JButton();
        NuevoLaboratorio = new javax.swing.JButton();
        AgregarLaboratorio = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        TexNombreLaboratorio = new javax.swing.JTextField();
        TexCodigoLaboratorio = new javax.swing.JTextField();
        TexTelefonoLaboratorio = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTablaCantidadLaboratorios = new javax.swing.JTable();
        ComboBuscarLaboratorioEntradas = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        Jtexcantidad = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        JtexEntradasLaboratorio = new javax.swing.JTextField();
        BotonGuardarCantidad = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        AgregarStan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TexCodigoStand = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        TexLetraStan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NumeroGabeta = new javax.swing.JComboBox();
        GuardarStan = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        TexCodigoGavetas = new javax.swing.JTextField();
        AgregarUsuario = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        TexCedula = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TexNombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TexPrimerApellido = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        TexSegundoApellido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TexUsuario = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        GuardarUsuarios = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ComboBoxCargo = new javax.swing.JComboBox();
        TexCargo = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable_Usuarios = new javax.swing.JTable();
        jButton_Eliminar_Usuario = new javax.swing.JButton();
        boton_actualizar = new javax.swing.JButton();
        TexPassword = new javax.swing.JPasswordField();
        jLabel37 = new javax.swing.JLabel();
        TexPasswordRepetir = new javax.swing.JPasswordField();
        jLabel38 = new javax.swing.JLabel();
        TexCorreo = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        AgregarMedicamento = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        ComboUbicacionStan = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ComboUbicacionGaveta = new javax.swing.JComboBox();
        TexCodigoMedicamento = new javax.swing.JTextField();
        texNombreGenericoMedicamento = new javax.swing.JTextField();
        TexNombreComercialMedicamento = new javax.swing.JTextField();
        GuardarMedicamento = new javax.swing.JButton();
        JFECHA = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TexFuncion = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        ComboLaboratorio = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        TexCantidad = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        TexCodigoLaboratorio_Medicamento = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        TexCodigoGaveta_Medicamento = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        comboPrecentacion = new javax.swing.JComboBox();
        TexPrecio_medicamento = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        BotonesBuscar = new javax.swing.JPanel();
        BuscarStock = new javax.swing.JButton();
        buscarMedicamento = new javax.swing.JButton();
        BuscarLaboratorio = new javax.swing.JButton();
        BuscarEnStock = new javax.swing.JPanel();
        TexBuscarMedicamentoCodigo = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaStock = new javax.swing.JTable();
        botonStock = new javax.swing.JButton();
        JRealizarVenta = new javax.swing.JButton();
        jFechaVenta = new com.toedter.calendar.JDateChooser();
        jTexTotal = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        BuscarLabora = new javax.swing.JPanel();
        TexBuscarLaboratorio = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaBusquedaLaboratorio = new javax.swing.JTable();
        BuscarMedicamento = new javax.swing.JPanel();
        TexBuscarMedicamento = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaBusquedaMedicamento = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        BotonesInformacion = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        TexUser = new javax.swing.JLabel();
        jLabelBienvenido = new javax.swing.JLabel();
        BotonSalir = new javax.swing.JButton();
        codigo_venta = new javax.swing.JLabel();
        ReporteFechas = new javax.swing.JPanel();
        FechaFinReport = new com.toedter.calendar.JDateChooser();
        FechaInicioReport = new com.toedter.calendar.JDateChooser();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        ReporteFacturas = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        TexCodigoFactura = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        BotonCambiarPassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Farmaser");
        setBounds(new java.awt.Rectangle(150, 8, 1025, 690));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setLayout(null);

        BotonesPrincipales.setBackground(new java.awt.Color(0, 204, 204));
        BotonesPrincipales.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Principal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        BotonNuevo.setBackground(new java.awt.Color(0, 204, 0));
        BotonNuevo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        BotonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/edit-user-profile_icon-icons.com_71489.png"))); // NOI18N
        BotonNuevo.setText("Nuevo");
        BotonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonNuevoActionPerformed(evt);
            }
        });

        BotonBuscar.setBackground(new java.awt.Color(0, 204, 0));
        BotonBuscar.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        BotonBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/datos_personales.png"))); // NOI18N
        BotonBuscar.setText("<html><center>Buscar<p>Vender<html>");
        BotonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonBuscarActionPerformed(evt);
            }
        });

        BotonInformacion.setBackground(new java.awt.Color(0, 204, 0));
        BotonInformacion.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        BotonInformacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/datos_personales1.png"))); // NOI18N
        BotonInformacion.setText("Reportes");
        BotonInformacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonInformacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BotonesPrincipalesLayout = new javax.swing.GroupLayout(BotonesPrincipales);
        BotonesPrincipales.setLayout(BotonesPrincipalesLayout);
        BotonesPrincipalesLayout.setHorizontalGroup(
            BotonesPrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesPrincipalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BotonesPrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BotonInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BotonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(BotonNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        BotonesPrincipalesLayout.setVerticalGroup(
            BotonesPrincipalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesPrincipalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BotonNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(BotonBuscar)
                .addGap(82, 82, 82)
                .addComponent(BotonInformacion)
                .addGap(38, 38, 38))
        );

        jPanel1.add(BotonesPrincipales);
        BotonesPrincipales.setBounds(10, 11, 243, 521);

        BotonesNuevo.setBackground(new java.awt.Color(0, 204, 204));
        BotonesNuevo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nuevo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        NuevoUsuario.setBackground(new java.awt.Color(0, 204, 0));
        NuevoUsuario.setText("Nuevo Usuario");
        NuevoUsuario.setMaximumSize(new java.awt.Dimension(100, 23));
        NuevoUsuario.setMinimumSize(new java.awt.Dimension(100, 23));
        NuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoUsuarioActionPerformed(evt);
            }
        });

        NuevoStan.setBackground(new java.awt.Color(0, 204, 0));
        NuevoStan.setText("Nuevo Estand");
        NuevoStan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoStanActionPerformed(evt);
            }
        });

        NuevoMedicamento.setBackground(new java.awt.Color(0, 204, 0));
        NuevoMedicamento.setText("<html><center>Nuevo<p>Medicamento<html>");
        NuevoMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoMedicamentoActionPerformed(evt);
            }
        });

        NuevoLaboratorio.setBackground(new java.awt.Color(0, 204, 0));
        NuevoLaboratorio.setText("<html><center>Nuevo<p>Laboratorio<html>");
        NuevoLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoLaboratorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BotonesNuevoLayout = new javax.swing.GroupLayout(BotonesNuevo);
        BotonesNuevo.setLayout(BotonesNuevoLayout);
        BotonesNuevoLayout.setHorizontalGroup(
            BotonesNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesNuevoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(NuevoStan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(NuevoMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(NuevoLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        BotonesNuevoLayout.setVerticalGroup(
            BotonesNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BotonesNuevoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BotonesNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NuevoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(NuevoStan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NuevoMedicamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NuevoLaboratorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.add(BotonesNuevo);
        BotonesNuevo.setBounds(260, 10, 730, 120);

        AgregarLaboratorio.setBackground(new java.awt.Color(0, 204, 204));
        AgregarLaboratorio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Nuevo laboratorio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel22.setText("Codigo :");

        jLabel23.setText("Nombre Laboratorio :");

        jLabel24.setText("Telefono :");

        jButton1.setBackground(new java.awt.Color(0, 204, 0));
        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Entradas De Medicamentos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTablaCantidadLaboratorios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre Generico", "Nombre Comercial", "Cantidad"
            }
        ));
        jTablaCantidadLaboratorios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaCantidadLaboratoriosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTablaCantidadLaboratorios);

        ComboBuscarLaboratorioEntradas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ComboBuscarLaboratorioEntradas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboBuscarLaboratorioEntradasItemStateChanged(evt);
            }
        });

        jLabel34.setText("Buscar Laboratorio:");

        jLabel35.setText("Cantidad:");

        Jtexcantidad.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel36.setText("Entradas:");

        BotonGuardarCantidad.setBackground(new java.awt.Color(0, 240, 0));
        BotonGuardarCantidad.setText("Guardar");
        BotonGuardarCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonGuardarCantidadActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 240, 0));
        jButton8.setText("Eliminar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Jtexcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JtexEntradasLaboratorio))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(BotonGuardarCantidad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ComboBuscarLaboratorioEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 333, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBuscarLaboratorioEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(Jtexcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(JtexEntradasLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BotonGuardarCantidad)
                            .addComponent(jButton8))
                        .addGap(0, 47, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout AgregarLaboratorioLayout = new javax.swing.GroupLayout(AgregarLaboratorio);
        AgregarLaboratorio.setLayout(AgregarLaboratorioLayout);
        AgregarLaboratorioLayout.setHorizontalGroup(
            AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarLaboratorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AgregarLaboratorioLayout.createSequentialGroup()
                        .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TexNombreLaboratorio)
                            .addComponent(TexCodigoLaboratorio)
                            .addComponent(TexTelefonoLaboratorio, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        AgregarLaboratorioLayout.setVerticalGroup(
            AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarLaboratorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(TexCodigoLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(TexNombreLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(6, 6, 6)
                .addGroup(AgregarLaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(TexTelefonoLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(AgregarLaboratorio);
        AgregarLaboratorio.setBounds(780, 1700, 710, 370);

        AgregarStan.setBackground(new java.awt.Color(0, 204, 204));
        AgregarStan.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Nuevo Estand", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel1.setText("Codigo Stand: ");

        jLabel2.setText("Letra Estand:");

        TexLetraStan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexLetraStanKeyTyped(evt);
            }
        });

        jLabel3.setText("¿ cuantas gavetas tiene?:");

        NumeroGabeta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        GuardarStan.setBackground(new java.awt.Color(0, 204, 0));
        GuardarStan.setText("Guardar");
        GuardarStan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarStanActionPerformed(evt);
            }
        });

        jLabel4.setText("Codigo de Gavetas:");

        javax.swing.GroupLayout AgregarStanLayout = new javax.swing.GroupLayout(AgregarStan);
        AgregarStan.setLayout(AgregarStanLayout);
        AgregarStanLayout.setHorizontalGroup(
            AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarStanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AgregarStanLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(GuardarStan))
                    .addGroup(AgregarStanLayout.createSequentialGroup()
                        .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(AgregarStanLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NumeroGabeta, 0, 108, Short.MAX_VALUE))
                            .addGroup(AgregarStanLayout.createSequentialGroup()
                                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TexCodigoStand)
                                    .addComponent(TexLetraStan, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)))
                            .addGroup(AgregarStanLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TexCodigoGavetas)))
                        .addGap(0, 462, Short.MAX_VALUE)))
                .addContainerGap())
        );
        AgregarStanLayout.setVerticalGroup(
            AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarStanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TexCodigoStand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TexLetraStan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TexCodigoGavetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(AgregarStanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NumeroGabeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(GuardarStan)
                .addContainerGap())
        );

        jPanel1.add(AgregarStan);
        AgregarStan.setBounds(20, 800, 730, 330);

        AgregarUsuario.setBackground(new java.awt.Color(0, 204, 204));
        AgregarUsuario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Nuevo Usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel5.setText("C.C :");

        TexCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexCedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexCedulaKeyTyped(evt);
            }
        });

        jLabel6.setText("Nombre :");

        TexNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexNombreKeyTyped(evt);
            }
        });

        jLabel7.setText("Primer Apellido :");

        TexPrimerApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexPrimerApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexPrimerApellidoKeyTyped(evt);
            }
        });

        jLabel8.setText("Segundo Apellido: ");

        TexSegundoApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexSegundoApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexSegundoApellidoKeyTyped(evt);
            }
        });

        jLabel9.setText("Usuario:");

        TexUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexUsuarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexUsuarioKeyTyped(evt);
            }
        });

        jLabel10.setText("Password :");

        GuardarUsuarios.setBackground(new java.awt.Color(0, 204, 0));
        GuardarUsuarios.setText("Guardar");
        GuardarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarUsuariosActionPerformed(evt);
            }
        });

        jLabel11.setText("Codigo de Cargo: ");

        jLabel12.setText("Cargo:");

        ComboBoxCargo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "seleccione", "1", "2" }));
        ComboBoxCargo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboBoxCargoItemStateChanged(evt);
            }
        });

        TexCargo.setText("NO A SELECCIONADO CARGO");

        jTable_Usuarios.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable_Usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cedula", "Nombre", "1° Apellido", "2° Apellido", "Cargo"
            }
        ));
        jScrollPane5.setViewportView(jTable_Usuarios);

        jButton_Eliminar_Usuario.setBackground(new java.awt.Color(0, 204, 0));
        jButton_Eliminar_Usuario.setText("Eliminar");
        jButton_Eliminar_Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Eliminar_UsuarioActionPerformed(evt);
            }
        });

        boton_actualizar.setBackground(new java.awt.Color(0, 204, 0));
        boton_actualizar.setText("Modificar");
        boton_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_actualizarActionPerformed(evt);
            }
        });

        TexPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexPasswordKeyReleased(evt);
            }
        });

        jLabel37.setText("repetir contraseña:");

        TexPasswordRepetir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexPasswordRepetirKeyReleased(evt);
            }
        });

        jLabel38.setText("Correo: ");

        TexCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TexCorreoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexCorreoKeyTyped(evt);
            }
        });

        jLabel39.setForeground(new java.awt.Color(255, 0, 0));
        jLabel39.setText("*");

        jLabel40.setForeground(new java.awt.Color(255, 0, 0));
        jLabel40.setText("*");

        jLabel41.setForeground(new java.awt.Color(255, 0, 0));
        jLabel41.setText("*");

        jLabel42.setForeground(new java.awt.Color(255, 0, 0));
        jLabel42.setText("*");

        jLabel43.setForeground(new java.awt.Color(255, 0, 0));
        jLabel43.setText("*");

        jLabel44.setForeground(new java.awt.Color(255, 0, 0));
        jLabel44.setText("*");

        jLabel45.setForeground(new java.awt.Color(255, 0, 0));
        jLabel45.setText("*");

        javax.swing.GroupLayout AgregarUsuarioLayout = new javax.swing.GroupLayout(AgregarUsuario);
        AgregarUsuario.setLayout(AgregarUsuarioLayout);
        AgregarUsuarioLayout.setHorizontalGroup(
            AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel38))
                                .addGap(14, 14, 14)
                                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(TexPassword, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TexUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel37)
                                                .addGap(18, 18, 18)
                                                .addComponent(TexPasswordRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(TexCorreo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                                                .addComponent(TexSegundoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, AgregarUsuarioLayout.createSequentialGroup()
                                                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(TexPrimerApellido, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(TexCedula, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(TexNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGap(18, 18, 18)
                                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TexCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ComboBoxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_Eliminar_Usuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boton_actualizar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(GuardarUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        AgregarUsuarioLayout.setVerticalGroup(
            AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TexCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(ComboBoxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(TexNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(TexCargo)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TexPrimerApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TexSegundoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TexCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(TexUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(TexPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(TexPasswordRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AgregarUsuarioLayout.createSequentialGroup()
                        .addComponent(GuardarUsuarios)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Eliminar_Usuario)
                        .addGap(18, 18, 18)
                        .addComponent(boton_actualizar))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.add(AgregarUsuario);
        AgregarUsuario.setBounds(260, 130, 730, 380);

        AgregarMedicamento.setBackground(new java.awt.Color(0, 204, 204));
        AgregarMedicamento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Nuevo Medicamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel13.setText("Codigo :");

        jLabel14.setText("Nombre Generico : ");

        jLabel15.setText("Nombre Comercial :");

        jLabel16.setText("Fecha de Vencimiento :");

        jLabel17.setText("Ubicacion del medicamento :");

        ComboUbicacionStan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboUbicacionStanItemStateChanged(evt);
            }
        });

        jLabel18.setText("Ubicacion en Estand:");

        jLabel19.setText("Ubicacion en Gaveta :");

        ComboUbicacionGaveta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboUbicacionGavetaItemStateChanged(evt);
            }
        });

        GuardarMedicamento.setBackground(new java.awt.Color(0, 204, 0));
        GuardarMedicamento.setText("Guardar");
        GuardarMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarMedicamentoActionPerformed(evt);
            }
        });

        jLabel20.setText("funcion :");

        TexFuncion.setColumns(20);
        TexFuncion.setRows(5);
        jScrollPane1.setViewportView(TexFuncion);

        jLabel21.setText("Laboratorio :");

        ComboLaboratorio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboLaboratorioItemStateChanged(evt);
            }
        });

        jLabel25.setText("Cantidad :");

        jLabel26.setText("Codigo del Laboratorio : ");

        TexCodigoLaboratorio_Medicamento.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel27.setText("Codigo de Gaveta :");

        jLabel28.setText("Preecentacion :");

        comboPrecentacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione Precentacion", "Cremas", "Cápsulas", "Inhalaciones", "Jarabes", "Polvos", "Píldoras", "Soluciones", "Supositorios", "Tabletas" }));

        jLabel29.setText("Precio Medicamento:");

        javax.swing.GroupLayout AgregarMedicamentoLayout = new javax.swing.GroupLayout(AgregarMedicamento);
        AgregarMedicamento.setLayout(AgregarMedicamentoLayout);
        AgregarMedicamentoLayout.setHorizontalGroup(
            AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel27)
                            .addComponent(jLabel29))
                        .addGap(17, 17, 17)
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TexPrecio_medicamento)
                            .addComponent(ComboUbicacionStan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboUbicacionGaveta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TexCodigoMedicamento)
                            .addComponent(texNombreGenericoMedicamento)
                            .addComponent(JFECHA, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(TexNombreComercialMedicamento)
                            .addComponent(TexCodigoGaveta_Medicamento))
                        .addGap(41, 41, 41)
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel28))
                                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TexCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 10, Short.MAX_VALUE))
                                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ComboLaboratorio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(comboPrecentacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())))
                            .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TexCodigoLaboratorio_Medicamento)))
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AgregarMedicamentoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(GuardarMedicamento)
                        .addContainerGap())))
        );
        AgregarMedicamentoLayout.setVerticalGroup(
            AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(TexCodigoMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(texNombreGenericoMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(TexNombreComercialMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(JFECHA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(TexCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboUbicacionStan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel28)
                    .addComponent(comboPrecentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(ComboUbicacionGaveta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addGroup(AgregarMedicamentoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ComboLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(TexCodigoGaveta_Medicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(TexCodigoLaboratorio_Medicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AgregarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TexPrecio_medicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(GuardarMedicamento)
                .addGap(449, 449, 449))
        );

        jPanel1.add(AgregarMedicamento);
        AgregarMedicamento.setBounds(20, 1140, 730, 340);

        BotonesBuscar.setBackground(new java.awt.Color(0, 204, 204));
        BotonesBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        BuscarStock.setBackground(new java.awt.Color(0, 204, 0));
        BuscarStock.setText("<html><center>Realizar<p>Venta<html>");
        BuscarStock.setMaximumSize(new java.awt.Dimension(100, 23));
        BuscarStock.setMinimumSize(new java.awt.Dimension(100, 23));
        BuscarStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarStockActionPerformed(evt);
            }
        });

        buscarMedicamento.setBackground(new java.awt.Color(0, 204, 0));
        buscarMedicamento.setText("<html><center>Buscar<p>Medicamento y Stock<html>");
        buscarMedicamento.setMaximumSize(new java.awt.Dimension(100, 23));
        buscarMedicamento.setMinimumSize(new java.awt.Dimension(100, 23));
        buscarMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarMedicamentoActionPerformed(evt);
            }
        });

        BuscarLaboratorio.setBackground(new java.awt.Color(0, 204, 0));
        BuscarLaboratorio.setText("<html><center>Buscar<p>Laboratorio<html>");
        BuscarLaboratorio.setMaximumSize(new java.awt.Dimension(100, 23));
        BuscarLaboratorio.setMinimumSize(new java.awt.Dimension(100, 23));
        BuscarLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarLaboratorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BotonesBuscarLayout = new javax.swing.GroupLayout(BotonesBuscar);
        BotonesBuscar.setLayout(BotonesBuscarLayout);
        BotonesBuscarLayout.setHorizontalGroup(
            BotonesBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BotonesBuscarLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(buscarMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(BuscarLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(BuscarStock, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        BotonesBuscarLayout.setVerticalGroup(
            BotonesBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BotonesBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BuscarStock, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(buscarMedicamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BuscarLaboratorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.add(BotonesBuscar);
        BotonesBuscar.setBounds(20, 550, 728, 120);

        BuscarEnStock.setBackground(new java.awt.Color(0, 204, 204));
        BuscarEnStock.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Realizar Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        TexBuscarMedicamentoCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TexBuscarMedicamentoCodigoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TexBuscarMedicamentoCodigoKeyTyped(evt);
            }
        });

        TablaStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre Generico", "Nombre Comercial", "Cantidad", "precio", "Cantidad a Vender", "Total"
            }
        ));
        TablaStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TablaStockKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(TablaStock);

        botonStock.setBackground(new java.awt.Color(0, 204, 0));
        botonStock.setText("Buscar");
        botonStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonStockActionPerformed(evt);
            }
        });

        JRealizarVenta.setBackground(new java.awt.Color(0, 204, 0));
        JRealizarVenta.setText("Realizar Venta");
        JRealizarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JRealizarVentaActionPerformed(evt);
            }
        });

        jTexTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexTotal.setForeground(new java.awt.Color(255, 0, 0));
        jTexTotal.setText("0");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setText("Total:");

        javax.swing.GroupLayout BuscarEnStockLayout = new javax.swing.GroupLayout(BuscarEnStock);
        BuscarEnStock.setLayout(BuscarEnStockLayout);
        BuscarEnStockLayout.setHorizontalGroup(
            BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarEnStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addGroup(BuscarEnStockLayout.createSequentialGroup()
                        .addComponent(JRealizarVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BuscarEnStockLayout.createSequentialGroup()
                        .addComponent(TexBuscarMedicamentoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        BuscarEnStockLayout.setVerticalGroup(
            BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarEnStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TexBuscarMedicamentoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botonStock))
                    .addComponent(jFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JRealizarVenta)
                    .addGroup(BuscarEnStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTexTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30)))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jPanel1.add(BuscarEnStock);
        BuscarEnStock.setBounds(780, 1290, 730, 390);

        BuscarLabora.setBackground(new java.awt.Color(0, 204, 204));
        BuscarLabora.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Laboratorio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        TexBuscarLaboratorio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TexBuscarLaboratorioKeyPressed(evt);
            }
        });

        TablaBusquedaLaboratorio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Telefono"
            }
        ));
        jScrollPane3.setViewportView(TablaBusquedaLaboratorio);

        javax.swing.GroupLayout BuscarLaboraLayout = new javax.swing.GroupLayout(BuscarLabora);
        BuscarLabora.setLayout(BuscarLaboraLayout);
        BuscarLaboraLayout.setHorizontalGroup(
            BuscarLaboraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarLaboraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscarLaboraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BuscarLaboraLayout.createSequentialGroup()
                        .addComponent(TexBuscarLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE))
                .addContainerGap())
        );
        BuscarLaboraLayout.setVerticalGroup(
            BuscarLaboraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarLaboraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TexBuscarLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        jPanel1.add(BuscarLabora);
        BuscarLabora.setBounds(780, 890, 730, 390);

        BuscarMedicamento.setBackground(new java.awt.Color(0, 204, 204));
        BuscarMedicamento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Medicamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        TexBuscarMedicamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TexBuscarMedicamentoKeyPressed(evt);
            }
        });

        TablaBusquedaMedicamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comercial", "Generico", "cantidad", "Estand", "Gaveta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaBusquedaMedicamento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TablaBusquedaMedicamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaBusquedaMedicamentoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaBusquedaMedicamento);

        grupos.add(jRadioButton1);
        jRadioButton1.setText("Buscar por nombre comercial");

        grupos.add(jRadioButton2);
        jRadioButton2.setText("Buscar por nombre generico");

        javax.swing.GroupLayout BuscarMedicamentoLayout = new javax.swing.GroupLayout(BuscarMedicamento);
        BuscarMedicamento.setLayout(BuscarMedicamentoLayout);
        BuscarMedicamentoLayout.setHorizontalGroup(
            BuscarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarMedicamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BuscarMedicamentoLayout.createSequentialGroup()
                        .addComponent(TexBuscarMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton2)
                        .addGap(0, 177, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        BuscarMedicamentoLayout.setVerticalGroup(
            BuscarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuscarMedicamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BuscarMedicamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TexBuscarMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(BuscarMedicamento);
        BuscarMedicamento.setBounds(780, 550, 739, 330);

        BotonesInformacion.setBackground(new java.awt.Color(0, 204, 204));
        BotonesInformacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jButton2.setBackground(new java.awt.Color(0, 204, 0));
        jButton2.setText("<html><center>Reporte Historial<p>De Ventas<html>");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 204, 0));
        jButton3.setText("<html><center>Reporte<p>Por Fechas<html>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 204, 0));
        jButton4.setText("<html><center>Reporte<p>De Facturas<html>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BotonesInformacionLayout = new javax.swing.GroupLayout(BotonesInformacion);
        BotonesInformacion.setLayout(BotonesInformacionLayout);
        BotonesInformacionLayout.setHorizontalGroup(
            BotonesInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesInformacionLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        BotonesInformacionLayout.setVerticalGroup(
            BotonesInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotonesInformacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BotonesInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.add(BotonesInformacion);
        BotonesInformacion.setBounds(20, 670, 728, 120);

        TexUser.setFont(new java.awt.Font("Bodoni MT Condensed", 0, 20)); // NOI18N
        TexUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TexUser.setText("XXXXXX");
        jPanel1.add(TexUser);
        TexUser.setBounds(10, 1920, 160, 20);

        jLabelBienvenido.setFont(new java.awt.Font("Perpetua Titling MT", 1, 11)); // NOI18N
        jLabelBienvenido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelBienvenido.setText("Bienvenido");
        jPanel1.add(jLabelBienvenido);
        jLabelBienvenido.setBounds(30, 1940, 110, 14);

        BotonSalir.setBackground(new java.awt.Color(0, 204, 0));
        BotonSalir.setText("Salir");
        BotonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSalirActionPerformed(evt);
            }
        });
        jPanel1.add(BotonSalir);
        BotonSalir.setBounds(50, 1890, 73, 30);

        codigo_venta.setText("codigo_venta");
        jPanel1.add(codigo_venta);
        codigo_venta.setBounds(20, 1490, 100, 14);

        ReporteFechas.setBackground(new java.awt.Color(0, 204, 204));
        ReporteFechas.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte de Fechas"));

        FechaFinReport.setDateFormatString("yyyy/M/d");

        FechaInicioReport.setDateFormatString("yyyy/M/d");

        jLabel31.setText("Fecha Inicial:");

        jLabel32.setText("Fecha Final:");

        jButton5.setBackground(new java.awt.Color(0, 204, 0));
        jButton5.setText("Generar Reporte");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReporteFechasLayout = new javax.swing.GroupLayout(ReporteFechas);
        ReporteFechas.setLayout(ReporteFechasLayout);
        ReporteFechasLayout.setHorizontalGroup(
            ReporteFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReporteFechasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReporteFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(FechaInicioReport, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(FechaFinReport, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addContainerGap(459, Short.MAX_VALUE))
        );
        ReporteFechasLayout.setVerticalGroup(
            ReporteFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReporteFechasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FechaInicioReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FechaFinReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.add(ReporteFechas);
        ReporteFechas.setBounds(1000, 30, 630, 180);

        ReporteFacturas.setBackground(new java.awt.Color(0, 204, 204));
        ReporteFacturas.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte Facturas"));

        jLabel33.setText("Codigo Factura:");

        jButton6.setBackground(new java.awt.Color(0, 204, 0));
        jButton6.setText("Generar Reporte");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Buscar en BackUP");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReporteFacturasLayout = new javax.swing.GroupLayout(ReporteFacturas);
        ReporteFacturas.setLayout(ReporteFacturasLayout);
        ReporteFacturasLayout.setHorizontalGroup(
            ReporteFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReporteFacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReporteFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReporteFacturasLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TexCodigoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ReporteFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(407, Short.MAX_VALUE))
        );
        ReporteFacturasLayout.setVerticalGroup(
            ReporteFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReporteFacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReporteFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(TexCodigoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel1.add(ReporteFacturas);
        ReporteFacturas.setBounds(1000, 220, 630, 180);

        BotonCambiarPassword.setBackground(new java.awt.Color(0, 204, 0));
        BotonCambiarPassword.setText("Cambiar Contraseña");
        BotonCambiarPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonCambiarPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCambiarPasswordActionPerformed(evt);
            }
        });
        jPanel1.add(BotonCambiarPassword);
        BotonCambiarPassword.setBounds(50, 1960, 140, 20);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 1760, 2230));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NuevoMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoMedicamentoActionPerformed
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(true);
        AgregarLaboratorio.setVisible(false);
        TexCodigoGaveta_Medicamento.setText(" ");
        this.ComboUbicacionStan.setModel(metodo.llena_combo());// reinisiar el box
        this.ComboLaboratorio.setModel(metodo.llenar_comboLaboratorio());
        this.ComboUbicacionGaveta.setModel(metodo.limpiar_combobox());

    }//GEN-LAST:event_NuevoMedicamentoActionPerformed

    private void NuevoStanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoStanActionPerformed
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(true);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);
    }//GEN-LAST:event_NuevoStanActionPerformed

    private void NuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoUsuarioActionPerformed
        AgregarUsuario.setVisible(true);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);
        CargarJtabletUser();

    }//GEN-LAST:event_NuevoUsuarioActionPerformed

    private void NuevoLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoLaboratorioActionPerformed
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(true);
        limpiaTablaLaboratorio(); // vaciar la tabla
        this.ComboBuscarLaboratorioEntradas.setModel(metodo.llenar_comboLaboratorio()); // llenar conbobox base de datos con metodo
    }//GEN-LAST:event_NuevoLaboratorioActionPerformed

    private void BuscarStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarStockActionPerformed
        BuscarMedicamento.setVisible(false);
        BuscarLabora.setVisible(false);
        BuscarEnStock.setVisible(true);
        jTexTotal.setText("0");

        Calendar c2 = new GregorianCalendar(); // colocar automaticamente la fecha en el Jcalendar
        jFechaVenta.setCalendar(c2);
        limpiaTabla();
    }//GEN-LAST:event_BuscarStockActionPerformed

    private void buscarMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarMedicamentoActionPerformed
        BuscarMedicamento.setVisible(true);
        BuscarLabora.setVisible(false);
        BuscarEnStock.setVisible(false);
    }//GEN-LAST:event_buscarMedicamentoActionPerformed

    private void BuscarLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarLaboratorioActionPerformed
        BuscarMedicamento.setVisible(false);
        BuscarLabora.setVisible(true);
        BuscarEnStock.setVisible(false);
    }//GEN-LAST:event_BuscarLaboratorioActionPerformed

    private void GuardarStanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarStanActionPerformed
        try {
            PreparedStatement pps = cn.prepareStatement("Insert INTO Stan (id, Letra_Stan)" + "VALUES(?,?)");
            pps.setString(1, TexCodigoStand.getText());
            pps.setString(2, TexLetraStan.getText());

            pps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Datos de Stan ingresados correctamente");
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
        crear_Gaveta();
    }//GEN-LAST:event_GuardarStanActionPerformed

    private void TexLetraStanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexLetraStanKeyTyped
        TexCodigoGavetas.setText(TexCodigoStand.getText());
    }//GEN-LAST:event_TexLetraStanKeyTyped

    private void BotonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSalirActionPerformed
        String variable= ""; // para cumplir parametros
        login ob = new login(0,variable); // componentes normales y no de cambio de contraseña
        ob.setVisible(true);
        dispose();

    }//GEN-LAST:event_BotonSalirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            PreparedStatement pps = cn.prepareStatement("Insert INTO laboratorio(id, Nombre)" + "VALUES(?,?)");
            pps.setString(1, TexCodigoLaboratorio.getText());
            pps.setString(2, TexNombreLaboratorio.getText());
            PreparedStatement pps1 = cn.prepareStatement("Insert INTO telefono(No_telefono, id_Laboratorio)" + "VALUES(?,?)");
            pps1.setString(1, TexTelefonoLaboratorio.getText());
            pps1.setString(2, TexCodigoLaboratorio.getText());

            pps.executeUpdate();
            pps1.executeUpdate();
            JOptionPane.showMessageDialog(null, "Datos de Laboratorio ingresados correctamente");
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void ComboLaboratorioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboLaboratorioItemStateChanged
        TexCodigoLaboratorio_Medicamento.setText(" ");
        String Dato = (String) ComboLaboratorio.getSelectedItem();
        llenar_jtexCodigoLaboratorio(Dato);
    }//GEN-LAST:event_ComboLaboratorioItemStateChanged

    private void GuardarMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarMedicamentoActionPerformed
        String dia = Integer.toString(JFECHA.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mes = Integer.toString(JFECHA.getCalendar().get(Calendar.MONTH) + 1);
        String year = Integer.toString(JFECHA.getCalendar().get(Calendar.YEAR));
        String fecha = (dia + "-" + mes + "-" + year);
        try {
            PreparedStatement pps = cn.prepareStatement("Insert INTO medicamentos(id, NombreGenerico,NombreComercial, FechaDeVencimiento, Cantidad, Funcion, precentacion, id_Laboratorio, Precio_medicamento)" + "VALUES(?,?,?,?,?,?,?,?,?)");
            pps.setString(1, TexCodigoMedicamento.getText());
            pps.setString(2, texNombreGenericoMedicamento.getText());
            pps.setString(3, TexNombreComercialMedicamento.getText());
            pps.setString(4, fecha);
            pps.setString(5, TexCantidad.getText());
            pps.setString(6, TexFuncion.getText());
            pps.setString(7, (String) comboPrecentacion.getSelectedItem());
            pps.setString(8, TexCodigoLaboratorio_Medicamento.getText());
            pps.setString(9, TexPrecio_medicamento.getText());

            PreparedStatement pps1 = cn.prepareStatement("Insert INTO gaveta_medicamento(id_Gaveta, id_Medicamentos)" + "VALUES(?,?)");
            pps1.setString(1, TexCodigoGaveta_Medicamento.getText());
            pps1.setString(2, TexCodigoMedicamento.getText());

            pps.executeUpdate();
            pps1.executeUpdate();

            JOptionPane.showMessageDialog(null, "Datos de Medicamento ingresados correctamente");
        } catch (SQLException | HeadlessException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

    }//GEN-LAST:event_GuardarMedicamentoActionPerformed

    private void ComboUbicacionGavetaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboUbicacionGavetaItemStateChanged
        String uno = (String) ComboUbicacionStan.getSelectedItem();
        int dos = Integer.parseInt(String.valueOf(ComboUbicacionGaveta.getSelectedItem()));
        llenar_JtexCodigoGaveta(uno, dos);
    }//GEN-LAST:event_ComboUbicacionGavetaItemStateChanged

    private void ComboUbicacionStanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboUbicacionStanItemStateChanged
        TexCodigoGaveta_Medicamento.setText(" ");
        String valor = (String) ComboUbicacionStan.getSelectedItem();// captura valor 
        metodo.llena_comboGaveta(valor);// envia el valor al metodo
        this.ComboUbicacionGaveta.setModel(metodo.llena_comboGaveta(valor));// llena combobox de los datos del metodo
    }//GEN-LAST:event_ComboUbicacionStanItemStateChanged

    private void TexBuscarMedicamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexBuscarMedicamentoKeyPressed

        if (jRadioButton1.isSelected()) {

            Buscar_por_Comercial(TexBuscarMedicamento.getText());
        } else {
            if (jRadioButton2.isSelected()) {
                Buscar_por_Generico(TexBuscarMedicamento.getText());
            } else {

                JOptionPane.showMessageDialog(null, "no se ha seleccionado ningun campo");
            }
        }

    }//GEN-LAST:event_TexBuscarMedicamentoKeyPressed

    private void TablaBusquedaMedicamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaBusquedaMedicamentoMouseClicked
        int selecionFila = TablaBusquedaMedicamento.rowAtPoint(evt.getPoint());
        String valor = String.valueOf(TablaBusquedaMedicamento.getValueAt(selecionFila, 0));

        Informacion_medicamento(valor);
    }//GEN-LAST:event_TablaBusquedaMedicamentoMouseClicked

    private void TexBuscarLaboratorioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexBuscarLaboratorioKeyPressed
        BuscarLaboratorio(TexBuscarLaboratorio.getText());
    }//GEN-LAST:event_TexBuscarLaboratorioKeyPressed

    private void TexBuscarMedicamentoCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexBuscarMedicamentoCodigoKeyPressed
        TextAutoCompleter variable = new TextAutoCompleter(TexBuscarMedicamentoCodigo);

        String sql = "SELECT a.id FROM medicamentos a";
        String lista;
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                lista = es.getString("id");
                Object b = lista;
                variable.addItem(b);
            }

        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_TexBuscarMedicamentoCodigoKeyPressed

    private void TexBuscarMedicamentoCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexBuscarMedicamentoCodigoKeyTyped
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            botonStock.doClick();
        }
    }//GEN-LAST:event_TexBuscarMedicamentoCodigoKeyTyped

    private void botonStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonStockActionPerformed
        String valor = TexBuscarMedicamentoCodigo.getText();
        ProductoStock(valor);
        TotalVenta();
    }//GEN-LAST:event_botonStockActionPerformed

    private void JRealizarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JRealizarVentaActionPerformed
        int fila = TablaStock.getRowCount();//cantidad de filas
        int i;
        String valores = "";
        for (i = 0; i < fila; i++) {
            String codigo = (String) TablaStock.getValueAt(i, 0);
            int valor = Integer.valueOf((String) TablaStock.getValueAt(i, 3));
            int valor1 = Integer.valueOf((String) TablaStock.getValueAt(i, 5));
            int resta = valor - valor1;
            ActualizarStock(resta, codigo);// metodo
            valores += String.valueOf(valor - valor1);
            //Con esta condición solo ponemos comas hasta el penúltimo valor :)
            if (i < (fila - 1)) {
                valores += ", ";
            }

        }
        //--------------------- capturar fecha -----------------------------------------------
        String dia = Integer.toString(jFechaVenta.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mes = Integer.toString(jFechaVenta.getCalendar().get(Calendar.MONTH) + 1);
        String year = Integer.toString(jFechaVenta.getCalendar().get(Calendar.YEAR));
        String FechaVenta = String.valueOf(year + "-" + mes + "-" + dia);

        //--------------------- capturar Hora ------------------------------------------------
        Calendar calendario = new GregorianCalendar();
        int hours = calendario.get(Calendar.HOUR_OF_DAY);
        int minutes = calendario.get(Calendar.MINUTE);// capturar hora
        int seconds = calendario.get(Calendar.SECOND);//capturar segundos
        String Hora = String.valueOf((hours + ":" + minutes + ":" + seconds));

        //--------------------- crear codigo recibo ------------------------------------------
        String IdVenta = String.valueOf((dia + mes + hours + minutes + seconds));
        codigo_venta.setText(IdVenta);
        //--------------------- Capturar valor De La Venta  ---------------------------------
        String ValorVenta = jTexTotal.getText();
        String NikeName = String.valueOf(TexUser.getText());
        //--------------------- Capturar usuario  ---------------------------------
        RealizarVenta(IdVenta, FechaVenta, Hora, ValorVenta, NikeName);
        //------- Metodo Chiquito para llenar la tabla venta_medicamentos  ---------------------
        for (i = 0; i < fila; i++) {
            String IdVentas = String.valueOf((dia + mes + hours + minutes + seconds));
            String codigo = (String) TablaStock.getValueAt(i, 0);
            String Cantidad = (String) TablaStock.getValueAt(i, 5);
            GuardarVentaMedicamentos(IdVentas, codigo, Cantidad);

            if (i < (fila - 1)) {//Con esta condición solo ponemos comas hasta el penúltimo valor :)
                valores += ", ";
            }

        }

        int resp = JOptionPane.showConfirmDialog(null, "¿Desea Imprimir La FActura?", "FActura", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {

            ReporteFactura(IdVenta);

        }

        limpiaTabla(); // limpiar jtable
        jTexTotal.setText("0");


    }//GEN-LAST:event_JRealizarVentaActionPerformed

    private void TablaStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaStockKeyReleased
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {

            int cantidad = 0;
            int precio = 0;
            int fila = TablaStock.getSelectedRow(); // captura numero de fila

            if (TablaStock.getRowCount() > 0) {

                cantidad = Integer.parseInt(String.valueOf(TablaStock.getValueAt(fila, 5)));
                precio = Integer.parseInt(String.valueOf(TablaStock.getValueAt(fila, 4)));

                int Valor = cantidad * precio;
                TablaStock.setValueAt(Valor, fila, 6); // incertar valores en celdavalor, fila, colobna

            }
        }
        TotalVenta();

    }//GEN-LAST:event_TablaStockKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //reporte
        int Respuesta = JOptionPane.showConfirmDialog(null, "¿Desea Ver las ventas de Personas Eliminadas?", "Alerta!", JOptionPane.YES_NO_OPTION); // Pregunta de seguridad y se captura
        if (Respuesta == JOptionPane.YES_OPTION) {
            try {
                JasperReport reporte = null;
                String path = "src\\reportes\\HistorialVentasEliminadas.jasper";
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                JasperPrint jprint = JasperFillManager.fillReport(path, null, cn);
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                JasperReport reporte = null;
                String path = "src\\reportes\\HistorialVenta.jasper";
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                JasperPrint jprint = JasperFillManager.fillReport(path, null, cn);
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ReporteFechas.setVisible(true);
        ReporteFacturas.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void BotonInformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonInformacionActionPerformed
        BotonesNuevo.setVisible(false);
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);

        BotonesBuscar.setVisible(false);
        BuscarLabora.setVisible(false);
        BuscarMedicamento.setVisible(false);
        BuscarEnStock.setVisible(false);

        BotonesInformacion.setVisible(true);
        ReporteFechas.setVisible(false);
        ReporteFacturas.setVisible(false);
    }//GEN-LAST:event_BotonInformacionActionPerformed

    private void BotonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonBuscarActionPerformed
        BotonesNuevo.setVisible(false);
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);

        BotonesBuscar.setVisible(true);
        BuscarLabora.setVisible(false);
        BuscarMedicamento.setVisible(false);

        BotonesInformacion.setVisible(false);
        ReporteFechas.setVisible(false);
        ReporteFacturas.setVisible(false);
    }//GEN-LAST:event_BotonBuscarActionPerformed

    private void BotonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonNuevoActionPerformed
        BotonesNuevo.setVisible(true);
        AgregarUsuario.setVisible(false);
        AgregarStan.setVisible(false);
        AgregarMedicamento.setVisible(false);
        AgregarLaboratorio.setVisible(false);

        BotonesBuscar.setVisible(false);
        BuscarLabora.setVisible(false);
        BuscarMedicamento.setVisible(false);
        BuscarEnStock.setVisible(false);

        BotonesInformacion.setVisible(false);
        ReporteFechas.setVisible(false);
        ReporteFacturas.setVisible(false);
    }//GEN-LAST:event_BotonNuevoActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String dia_Inicio = Integer.toString(FechaInicioReport.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mes_inicio = Integer.toString(FechaInicioReport.getCalendar().get(Calendar.MONTH) + 1);
        String year_Inicio = Integer.toString(FechaInicioReport.getCalendar().get(Calendar.YEAR));
        String FechaInicio = String.valueOf(year_Inicio + "-" + mes_inicio + "-" + dia_Inicio);

        String diafin = Integer.toString(FechaFinReport.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mesfin = Integer.toString(FechaFinReport.getCalendar().get(Calendar.MONTH) + 1);
        String yearfin = Integer.toString(FechaFinReport.getCalendar().get(Calendar.YEAR));
        String FechaFin = String.valueOf(yearfin + "-" + mesfin + "-" + diafin);

        try {
            JasperReport reporte = null;

            String path = "src\\reportes\\report1.jasper";

            Map Perametro1 = new HashMap();
            Perametro1.put("Fachi_Inicio", FechaInicio);
            Perametro1.put("FechaFinal", FechaFin);

            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

            JasperPrint jprint1 = JasperFillManager.fillReport(path, Perametro1, cn);

            JasperViewer view = new JasperViewer(jprint1, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (TexCodigoFactura.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Campo Esta basio");
        } else {
            String Dato = String.valueOf(TexCodigoFactura.getText());
            String SQL = "SELECT a.id FROM ventas a WHERE a.id like '" + Dato + "%'";
            try {
                Statement st = cn.createStatement();
                ResultSet es = st.executeQuery(SQL);
                if (es.next()) {
                    String ID = es.getString("id");
                    ReporteFactura(ID);
                } else {
                    JOptionPane.showMessageDialog(null, "La Factura No Existe");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "1" + ex);
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ReporteFechas.setVisible(false);
        ReporteFacturas.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void BotonGuardarCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonGuardarCantidadActionPerformed
        int fila1 = jTablaCantidadLaboratorios.getSelectedRow(); // captura numero de fila       
        if (fila1 >= 0) {
            if (JtexEntradasLaboratorio.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El Campo Esta basio");
            } else {
                int fila = jTablaCantidadLaboratorios.getSelectedRow(); // captura numero de fila
                int colubna = jTablaCantidadLaboratorios.getSelectedColumn(); // captura dato de la colubna
                if (fila >= 0) {
                    String Entradas = String.valueOf(jTablaCantidadLaboratorios.getValueAt(fila, 0)); // captura valor de la primera colubna cedula
                    int DatoNuevo = Integer.valueOf(JtexEntradasLaboratorio.getText());
                    int DatoViejo = Integer.parseInt(Jtexcantidad.getText());
                    int valor = DatoNuevo + DatoViejo;
                    String Dato = String.valueOf(valor);

                    String sql = "UPDATE  medicamentos set Cantidad = '" + Dato + "' WHERE id = '" + Entradas + "'";

                    try {
                        Statement st = cn.createStatement();
                        int es = st.executeUpdate(sql); // executeUpdate eliminar actualizar y insertar
                        ComboBuscarLaboratorioEntradas.setSelectedIndex(0); // primer item 
                        JtexEntradasLaboratorio.setText("");
                        Jtexcantidad.setText("");
                        JOptionPane.showMessageDialog(null, "se actualizo la cantidaad");

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione Dato a Modificar o Eliminar");
        }
    }//GEN-LAST:event_BotonGuardarCantidadActionPerformed

    private void ComboBuscarLaboratorioEntradasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboBuscarLaboratorioEntradasItemStateChanged
        String valor = (String) ComboBuscarLaboratorioEntradas.getSelectedItem();
        llenar_Jtabla_medicamento(valor);
    }//GEN-LAST:event_ComboBuscarLaboratorioEntradasItemStateChanged

    private void jTablaCantidadLaboratoriosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaCantidadLaboratoriosMouseClicked

        String cantidad = "";
        int fila = jTablaCantidadLaboratorios.getSelectedRow(); // captura numero de fila
        if (jTablaCantidadLaboratorios.getRowCount() > 0) {
            cantidad = String.valueOf(jTablaCantidadLaboratorios.getValueAt(fila, 3));
            Jtexcantidad.setText(cantidad);
        } else {
            JOptionPane.showInputDialog("seleccione Dato");

        }
    }//GEN-LAST:event_jTablaCantidadLaboratoriosMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int fila = jTablaCantidadLaboratorios.getSelectedRow(); // captura nimero de fila
        if (fila >= 0) {
            String valor = String.valueOf(jTablaCantidadLaboratorios.getValueAt(fila, 0)); // captura valor de la primera colubna
            String sql = "Call EliminarMedicamento('" + valor + "')"; // yama procedimiento SQL
            int Respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro?", "Alerta!", JOptionPane.YES_NO_OPTION); // Pregunta de seguridad y se captura
            if (Respuesta == JOptionPane.YES_OPTION) {
                //JOptionPane.showMessageDialog(this, valor);
                try {
                    Statement st = cn.createStatement();
                    ResultSet es = st.executeQuery(sql); // se elimina
                    ComboBuscarLaboratorioEntradas.setSelectedIndex(0); // primer item 
                    JOptionPane.showMessageDialog(this, "Medicamento Eliminado");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione Dato a Modificar o Eliminar");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String respuesta = JOptionPane.showInputDialog("Dijite Codigo Factura");

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "El Campo Esta basio");
        } else {
            String Dato = respuesta;
            String SQL = "SELECT a.id_Ventas FROM backupventas a WHERE a.id_Ventas like '" + Dato + "%'";
            try {
                Statement st = cn.createStatement();
                ResultSet es = st.executeQuery(SQL);
                if (es.next()) {
                    String ID = es.getString("id_Ventas");
                    ReporteFacturasHistorial(ID);
                } else {
                    JOptionPane.showMessageDialog(null, "La Factura No Existe");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "1" + ex);
            }

        }


    }//GEN-LAST:event_jButton7ActionPerformed

    private void boton_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_actualizarActionPerformed
        int fila = jTable_Usuarios.getSelectedRow(); // captura numero de fila
        int colubna = jTable_Usuarios.getSelectedColumn(); // captura dato de la colubna
        if (fila >= 0) {
            String Cedula = String.valueOf(jTable_Usuarios.getValueAt(fila, 0)); // captura valor de la primera colubna cedula
            String DatoActualizar = String.valueOf(jTable_Usuarios.getValueAt(fila, colubna)); // captura valor que se va actualizar
            int i = jTable_Usuarios.getSelectedColumn();
            while (i <= 4) {
                String colubnaDato = "";
                if (i == 0) {
                    colubnaDato = "cc";
                    Actualizarpersona(colubnaDato, DatoActualizar, Cedula);
                    break;
                }
                if (i == 1) {
                    colubnaDato = "Nombre";
                    Actualizarpersona(colubnaDato, DatoActualizar, Cedula);
                    break;
                }
                if (i == 2) {
                    colubnaDato = "Primer_Apellido";
                    Actualizarpersona(colubnaDato, DatoActualizar, Cedula);
                    break;
                }
                if (i == 3) {
                    colubnaDato = "Segundo_Apellido";
                    Actualizarpersona(colubnaDato, DatoActualizar, Cedula);

                    break;
                }
                if (i == 4) {
                    String cargo = String.valueOf(jTable_Usuarios.getValueAt(fila, colubna));

                    if (cargo.contentEquals("Administrador")) {
                        String valor = String.valueOf(1);
                        colubnaDato = "id_Cargo";
                        Actualizarpersona(colubnaDato, valor, Cedula);
                        JOptionPane.showMessageDialog(this, "el Dato " + DatoActualizar + " Fue modificado con exito");
                        break;
                    }
                    if (cargo.contentEquals("Empleado")) {
                        String valor = String.valueOf(2);
                        colubnaDato = "id_Cargo";
                        Actualizarpersona(colubnaDato, valor, Cedula);
                        JOptionPane.showMessageDialog(this, "el Dato " + DatoActualizar + " Fue modificado con exito");
                        break;
                    }

                }
            }
            JOptionPane.showMessageDialog(this, "el Dato " + DatoActualizar + " Fue modificado con exito");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione Dato a Modificar o Eliminar");
        }
        CargarJtabletUser();
    }//GEN-LAST:event_boton_actualizarActionPerformed

    private void jButton_Eliminar_UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Eliminar_UsuarioActionPerformed
        int fila = jTable_Usuarios.getSelectedRow(); // captura nimero de fila
        if (fila >= 0) {
            String valor = String.valueOf(jTable_Usuarios.getValueAt(fila, 0)); // captura valor de la primera colubna
            String sql = "Call EliminarPersonaCargo('" + valor + "')"; // yama procedimiento SQL
            int Respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro?", "Alerta!", JOptionPane.YES_NO_OPTION); // Pregunta de seguridad y se captura
            if (Respuesta == JOptionPane.YES_OPTION) {

                llenar_TablaBackUp(valor); // envia los datos del eliminado a una tabla backup
                try {
                    Statement st = cn.createStatement();
                    ResultSet es = st.executeQuery(sql); // se elimina

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione Dato a Modificar o Eliminar");
        }
        CargarJtabletUser();
    }//GEN-LAST:event_jButton_Eliminar_UsuarioActionPerformed

    private void ComboBoxCargoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboBoxCargoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (this.ComboBoxCargo.getSelectedIndex() > 0) {
                TexCargo.setText("Administrador");
            }
        }

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (this.ComboBoxCargo.getSelectedIndex() > 1) {
                TexCargo.setText("Empleado");
            }
        } else {
            TexCargo.setText("NO A SELECCIONADO CARGO");
        }
    }//GEN-LAST:event_ComboBoxCargoItemStateChanged

    private void GuardarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarUsuariosActionPerformed
        String Password1 = TexPassword.getText(); // captura los caracteres de los jTexfiles
        String Password2 = TexPasswordRepetir.getText();

        String PasswordIncriptadoMD5 = DigestUtils.md5Hex(Password1); // incripta la contraseña
        if (TexPassword.getText().length() == 12) { // revisa si tiene 12 caracteres
            if (!Password1.equals(Password2)) { // Equals funcion para comparar variables
                JOptionPane.showMessageDialog(null, "Revise la contraseña, son diferentes");
                //JOptionPane.showMessageDialog(null, PasswordIncriptadoMD5); // revisa si queda incriptado

            } else {
                try {//guarda los datos
                    PreparedStatement pps = cn.prepareStatement("Insert INTO persona (cc, Nombre, Primer_Apellido, Segundo_Apellido, correo, user, password, id_Cargo)" + "VALUES(?,?,?,?,?,?,?,?)");
                    pps.setString(1, TexCedula.getText());
                    pps.setString(2, TexNombre.getText());
                    pps.setString(3, TexPrimerApellido.getText());
                    pps.setString(4, TexSegundoApellido.getText());
                    pps.setString(5, TexCorreo.getText());
                    pps.setString(6, TexUsuario.getText());
                    pps.setString(7, PasswordIncriptadoMD5);
                    pps.setString(8, (String) ComboBoxCargo.getSelectedItem());

                    pps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Datos de nuevo usuario ingresados correctamente");

                    limpiaCampos();
                    CargarJtabletUser();
                    
                } catch (SQLException | HeadlessException ex) {
                    JOptionPane.showMessageDialog(null, "no hay datos a guardar");
                    JOptionPane.showMessageDialog(this, "No Selecciono el cargo");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "La contraseña debe ser mas de 12 caracteres");
        }


    }//GEN-LAST:event_GuardarUsuariosActionPerformed

    private void TexCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexCedulaKeyTyped
        char validar = evt.getKeyChar(); // funcion para validar el campo que solo sea texto
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "Ingrese Solo Numeros");
        } else if (TexCedula.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_TexCedulaKeyTyped

    private void TexNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexNombreKeyTyped
        if (TexNombre.getText().length() >= 10) { // definir el tamaño del Jtex
            evt.consume();
        }
        validacionCaracteristicas(evt);
    }//GEN-LAST:event_TexNombreKeyTyped

    private void TexPrimerApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexPrimerApellidoKeyTyped
        if (TexPrimerApellido.getText().length() >= 10) { // definir el tamaño del Jtex
            evt.consume();
        }
        validacionCaracteristicas(evt);
    }//GEN-LAST:event_TexPrimerApellidoKeyTyped

    private void TexSegundoApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexSegundoApellidoKeyTyped
        if (TexSegundoApellido.getText().length() >= 10) { // definir el tamaño del Jtex
            evt.consume();
        }
        validacionCaracteristicas(evt);
    }//GEN-LAST:event_TexSegundoApellidoKeyTyped

    private void TexCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexCorreoKeyTyped
        if (TexCorreo.getText().length() >= 50) { // definir el tamaño del Jtex
            evt.consume();
        }
        validarcorreo(evt);
    }//GEN-LAST:event_TexCorreoKeyTyped

    private void TexUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexUsuarioKeyTyped
        if (TexUsuario.getText().length() >= 10) { // definir el tamaño del Jtexusua
            evt.consume();
        }
        validacionCaracteristicas(evt);
    }//GEN-LAST:event_TexUsuarioKeyTyped

    private void TexCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexCedulaKeyReleased
        validarcampos();
        habilitarboton();
    }//GEN-LAST:event_TexCedulaKeyReleased

    private void TexNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexNombreKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexNombreKeyReleased

    private void TexPrimerApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexPrimerApellidoKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexPrimerApellidoKeyReleased

    private void TexSegundoApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexSegundoApellidoKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexSegundoApellidoKeyReleased

    private void TexCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexCorreoKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexCorreoKeyReleased

    private void TexUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexUsuarioKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexUsuarioKeyReleased

    private void TexPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexPasswordKeyReleased
        validarcampos();
        habilitarboton();// TODO add your handling code here:
    }//GEN-LAST:event_TexPasswordKeyReleased

    private void TexPasswordRepetirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TexPasswordRepetirKeyReleased
validarcampos();
        habilitarboton();        // TODO add your handling code here:
    }//GEN-LAST:event_TexPasswordRepetirKeyReleased

    private void BotonCambiarPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCambiarPasswordActionPerformed
        int resp = JOptionPane.showConfirmDialog(null, "Para cambiar la contraseña tiene que salir del programa\n¿desea salir del programa?");
            int num = 1; // se inicializa en 1 para que habra el login para cambio de contraseña
            String usuario = TexUser.getText();// se coge el nombre del usuario
            
        if (JOptionPane.OK_OPTION == resp) {
            System.out.println("Selecciona opción Afirmativa");                    
            this.setVisible(false);
            login ingreso = new login(num,usuario);// se envia el nimero para el cambio de contraseña y el nombre del usuario
            ingreso.setVisible(true);
                        
        } else {
            System.out.println("No selecciona una opción afirmativa");
        }
    }//GEN-LAST:event_BotonCambiarPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AgregarLaboratorio;
    private javax.swing.JPanel AgregarMedicamento;
    private javax.swing.JPanel AgregarStan;
    private javax.swing.JPanel AgregarUsuario;
    private javax.swing.JButton BotonBuscar;
    private javax.swing.JButton BotonCambiarPassword;
    private javax.swing.JButton BotonGuardarCantidad;
    public static javax.swing.JButton BotonInformacion;
    public static javax.swing.JButton BotonNuevo;
    private javax.swing.JButton BotonSalir;
    private javax.swing.JPanel BotonesBuscar;
    private javax.swing.JPanel BotonesInformacion;
    private javax.swing.JPanel BotonesNuevo;
    private javax.swing.JPanel BotonesPrincipales;
    private javax.swing.JPanel BuscarEnStock;
    private javax.swing.JPanel BuscarLabora;
    private javax.swing.JButton BuscarLaboratorio;
    private javax.swing.JPanel BuscarMedicamento;
    private javax.swing.JButton BuscarStock;
    private javax.swing.JComboBox ComboBoxCargo;
    private javax.swing.JComboBox ComboBuscarLaboratorioEntradas;
    private javax.swing.JComboBox ComboLaboratorio;
    private javax.swing.JComboBox ComboUbicacionGaveta;
    private javax.swing.JComboBox ComboUbicacionStan;
    private com.toedter.calendar.JDateChooser FechaFinReport;
    private com.toedter.calendar.JDateChooser FechaInicioReport;
    private javax.swing.JButton GuardarMedicamento;
    private javax.swing.JButton GuardarStan;
    private javax.swing.JButton GuardarUsuarios;
    private com.toedter.calendar.JDateChooser JFECHA;
    private javax.swing.JButton JRealizarVenta;
    private javax.swing.JTextField JtexEntradasLaboratorio;
    private javax.swing.JTextField Jtexcantidad;
    private javax.swing.JButton NuevoLaboratorio;
    private javax.swing.JButton NuevoMedicamento;
    private javax.swing.JButton NuevoStan;
    private javax.swing.JButton NuevoUsuario;
    private javax.swing.JComboBox NumeroGabeta;
    private javax.swing.JPanel ReporteFacturas;
    private javax.swing.JPanel ReporteFechas;
    private javax.swing.JTable TablaBusquedaLaboratorio;
    private javax.swing.JTable TablaBusquedaMedicamento;
    private javax.swing.JTable TablaStock;
    private javax.swing.JTextField TexBuscarLaboratorio;
    private javax.swing.JTextField TexBuscarMedicamento;
    private javax.swing.JTextField TexBuscarMedicamentoCodigo;
    private javax.swing.JTextField TexCantidad;
    private javax.swing.JLabel TexCargo;
    private javax.swing.JTextField TexCedula;
    private javax.swing.JTextField TexCodigoFactura;
    private javax.swing.JTextField TexCodigoGaveta_Medicamento;
    private javax.swing.JTextField TexCodigoGavetas;
    private javax.swing.JTextField TexCodigoLaboratorio;
    private javax.swing.JTextField TexCodigoLaboratorio_Medicamento;
    private javax.swing.JTextField TexCodigoMedicamento;
    private javax.swing.JTextField TexCodigoStand;
    private javax.swing.JTextField TexCorreo;
    private javax.swing.JTextArea TexFuncion;
    private javax.swing.JTextField TexLetraStan;
    private javax.swing.JTextField TexNombre;
    private javax.swing.JTextField TexNombreComercialMedicamento;
    private javax.swing.JTextField TexNombreLaboratorio;
    private javax.swing.JPasswordField TexPassword;
    private javax.swing.JPasswordField TexPasswordRepetir;
    private javax.swing.JTextField TexPrecio_medicamento;
    private javax.swing.JTextField TexPrimerApellido;
    private javax.swing.JTextField TexSegundoApellido;
    private javax.swing.JTextField TexTelefonoLaboratorio;
    public static javax.swing.JLabel TexUser;
    private javax.swing.JTextField TexUsuario;
    private javax.swing.JButton botonStock;
    private javax.swing.JButton boton_actualizar;
    private javax.swing.JButton buscarMedicamento;
    private javax.swing.JLabel codigo_venta;
    private javax.swing.JComboBox comboPrecentacion;
    private javax.swing.ButtonGroup grupos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton_Eliminar_Usuario;
    public static com.toedter.calendar.JDateChooser jFechaVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBienvenido;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTablaCantidadLaboratorios;
    private javax.swing.JTable jTable_Usuarios;
    private javax.swing.JTextField jTexTotal;
    private javax.swing.JTextField texNombreGenericoMedicamento;
    // End of variables declaration//GEN-END:variables
}
