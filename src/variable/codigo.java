/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable;

import coneccion.coneccionSQL;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author chach
 */
public class codigo {

    coneccionSQL cc = new coneccionSQL();
    Connection cn = cc.Conectar();

    DefaultTableModel model = new DefaultTableModel();
    
    String codigo, cedula, usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    

    public DefaultComboBoxModel llena_combo() { //creacion de metodo para llenar el combobox
        DefaultComboBoxModel ListaStan = new DefaultComboBoxModel();
        ListaStan.addElement("seleccione un stand");
        String sql = "SELECT * FROM stan";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                ListaStan.addElement(es.getString("Letra_Stan"));

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return ListaStan;
    }

    public DefaultComboBoxModel llena_combo_Laboratorio() { //creacion de metodo para llenar el combobox
        DefaultComboBoxModel ListaStan = new DefaultComboBoxModel();
        ListaStan.addElement("seleccione un Laboratorio");
        String sql = "SELECT * FROM `laboratorio`";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                ListaStan.addElement(es.getString("Nombre"));

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return ListaStan;
    }

    public DefaultComboBoxModel llena_comboGaveta(String valor) { //creacion de metodo para llenar el combobox
        DefaultComboBoxModel ListaStan = new DefaultComboBoxModel();
        ListaStan.addElement("seleccione un stand");
        String sql = "SELECT a.No_gaveta FROM gaveta a, stan b WHERE b.Letra_Stan like '%" + valor + "' AND a.id_Stan = b.id";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                ListaStan.addElement(es.getString("No_gaveta"));

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return ListaStan;
    }

    public DefaultComboBoxModel llenar_comboLaboratorio() {
        DefaultComboBoxModel ListaStan = new DefaultComboBoxModel();
        ListaStan.addElement("seleccione un Laboratorio");
        String sql = "SELECT * FROM laboratorio";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql);
            while (es.next()) {
                ListaStan.addElement(es.getString("Nombre"));

            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return ListaStan;

    }

    public DefaultComboBoxModel limpiar_combobox() {
        DefaultComboBoxModel ListaStan = new DefaultComboBoxModel();

        ListaStan.addElement(String.valueOf(" "));

        return ListaStan;
    }


    


}

