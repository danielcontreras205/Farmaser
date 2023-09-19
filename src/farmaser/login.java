/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmaser;
import coneccion.coneccionSQL;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.digest.DigestUtils;


public class login extends javax.swing.JFrame {

    coneccionSQL cc = new coneccionSQL();
    Connection cn = cc.Conectar();

    private variable.codigo metodo = new variable.codigo(); // foma para llamar a las funciones y variables
    int timer; // bariable global para los intentos de acceso al software

    public login(int valor, String nombre) {// se le coloca un parametro int por el motivo de cambio de contraseña

        if (valor == 0) { // componentes normales
            initComponents();
            txtusuario.requestFocus();
            this.setLocationRelativeTo(null);
            setIconImage(new ImageIcon(getClass().getResource("/imagenes/LOGO.png")).getImage());
            this.setTitle("Farmaser");
            this.setBounds(430, 220, 525, 275); // pocision y tamaño del Jframe x, y,

            jPanel4.setBounds(240, 0, 270, 240);
            jPanel5.setBounds(240, 0, 270, 240);

            jPanel4.setVisible(false);
            jPanel1.setVisible(true);
            jPanel5.setVisible(false);
            TexNuevaContraseña.setEnabled(false);
            TexNuevaContraseña2.setEnabled(false);

            System.out.println("hola mundo");
        } if (valor != 0) { // componentes de cambio de contraseña
            initComponents();
            this.setLocationRelativeTo(null);
            setIconImage(new ImageIcon(getClass().getResource("/imagenes/LOGO.png")).getImage());
            this.setTitle("Cambio de contraseña");
            this.setBounds(430, 220, 525, 275); // pocision y tamaño del Jframe x, y,

            jPanel4.setBounds(240, 0, 270, 240);
            jPanel5.setBounds(240, 0, 270, 240);

            jPanel4.setVisible(false);
            jPanel1.setVisible(false);
            jPanel5.setVisible(true);
            TexNuevaContraseña.setEnabled(false);
            TexNuevaContraseña2.setEnabled(false);
            
            metodo.setUsuario(nombre);
            
            

        }

    } 

    void acceder(String usuario, String pass) {
        String cap = "";
        String sql = "SELECT * FROM persona WHERE user='" + usuario + "' && password='" + pass + "'";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cap = rs.getString("id_Cargo");
            }
            if (cap.equals("1")) {
                this.setVisible(false);
                JOptionPane.showMessageDialog(null, "Bienvenido");
                Principal ingreso = new Principal();
                ingreso.setVisible(true);

                Principal.TexUser.setText(usuario);

            }
            if (cap.equals("2")) {
                this.setVisible(false);
                JOptionPane.showMessageDialog(null, "Bienvenido");
                Principal ingresos = new Principal();
                ingresos.setVisible(true);
                Principal.BotonNuevo.setEnabled(false);
                Principal.jFechaVenta.setEnabled(false);
                Principal.BotonInformacion.setEnabled(false);
                Principal.TexUser.setText(usuario);
                
            } else if (timer == 3) {
                JOptionPane.showMessageDialog(this, "Ha excedido el numero de intentos. vuelva mas tarde","Error",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if ((!cap.equals("1")) && (!cap.equals("2"))) {
                JOptionPane.showMessageDialog(this, "El usuario o la contraseña son incorrectas","WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
                txtusuario.setText("");
                txtcontra.setText("");
                timer = timer + 1;
            }
        } catch (SQLException ex) {
            // Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "No Selecciono el cargo");
        }

    }
    
    void CodigoDeSeguridad(String CC) {

        String captura = "";
        String correo = "";
        String sql = "SELECT * FROM persona WHERE cc='" + CC + "'"; // busca en la BD
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                captura = rs.getString("cc");// captura el valor de la BD
                correo = rs.getString("correo");
            }
            if (captura.equals(CC)) {
                int valorEntero = (int) Math.floor(Math.random() * (10000 - 99999 + 1) + 99999); /// genera un numero Random
                                try {
                    Properties props = new Properties();// propiedades de conexxion (instancia)
                    props.setProperty("mail.smtp.host", "smtp.gmail.com");
                    props.setProperty("mail.smtp.starttls.enable", "true");
                    props.setProperty("mail.smtp.port", "587");
                    props.setProperty("mail.smtp.auth", "true");

                    Session session = Session.getDefaultInstance(props);

                    String CorreoRemitente = "";
                    String PasswordRemitente = "";
                    String CorreoReceptor = correo;

                    String asunto = "codigo de seguridad";
                    String mensaje = "tu codigo de seguridad es:  " + String.valueOf(valorEntero);

                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(CorreoRemitente)); 
                    
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress(CorreoReceptor));
                    message.setSubject(asunto);
                    message.setText(mensaje);
                    
                    Transport t = session.getTransport("smtp");
                    t.connect(CorreoRemitente,PasswordRemitente);
                    t.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
                    t.close();
                    
                    JOptionPane.showMessageDialog(this, "Codigo de seguridad se le envio a su correo");
                    jPanel4.setVisible(true);
                    jPanel1.setVisible(false);
                    metodo.setCodigo(String.valueOf(valorEntero)); // envio el codigo de seguridad al encabsulamiento
                                        
                } catch (MessagingException ex) {
                    Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(this, "No se encontro el usuario", "Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }
    
    void BuscarUsuarioCambioContraseña(String usuario, String Password) {
        String Password1 = TexNuevaContraseñaCambio.getText(); // captura los caracteres de los jTexfiles
        String PasswordIncriptadoMD5 = DigestUtils.md5Hex(Password1); // incripta la contraseña

        String sql = "CALL BuscarUsuarioCambioContraseña('" + usuario + "','" + Password + "')";
        try {
            Statement st = cn.createStatement();
            ResultSet es = st.executeQuery(sql); // ejecuta el Call
            if (es.next()) {
                String sql2 = "UPDATE persona set password = '" + PasswordIncriptadoMD5 + "' WHERE user = '" + usuario + "'";
                try {
                    Statement stq = cn.createStatement();
                    int esq = st.executeUpdate(sql2); // executeUpdate eliminar actualizar y insertar
                    JOptionPane.showMessageDialog(null, "Contraseña cambiada");
                    jPanel1.setVisible(true);
                    jPanel5.setVisible(false);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "su contraseña actual es incorrecta");
            }
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtcontra = new javax.swing.JPasswordField();
        INGRESAR = new javax.swing.JButton();
        txtusuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        BotonValidar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TexSeguridad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        BotonGuardarValidacion = new javax.swing.JButton();
        TexNuevaContraseña = new javax.swing.JPasswordField();
        TexNuevaContraseña2 = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        TexPasswordActual = new javax.swing.JPasswordField();
        TexNuevaContraseñaCambio = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        TexNuevaContraseñaCambio2 = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(123, 123, 12, 12));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        txtcontra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcontraKeyTyped(evt);
            }
        });

        INGRESAR.setBackground(new java.awt.Color(0, 204, 0));
        INGRESAR.setText("INGRESAR");
        INGRESAR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        INGRESAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INGRESARActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel2.setText("PASSWORD :");

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel1.setText("USUARIO    :");

        jLabel3.setText("¿Has olvidado la contraseña?");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(INGRESAR)
                .addGap(101, 101, 101))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtusuario, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(txtcontra)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel3)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1)
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtcontra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(INGRESAR)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel1);
        jPanel1.setBounds(240, 0, 300, 240);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LOGO2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel4)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel4)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2);
        jPanel2.setBounds(0, 0, 240, 240);

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));

        BotonValidar.setBackground(new java.awt.Color(0, 204, 0));
        BotonValidar.setText("VALIDAR");
        BotonValidar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonValidarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel5.setText("NUEVA CONTRASEÑA:");

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel6.setText("CODIGO DE SEGURIDAD:");

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel8.setText("REPITA CONTRASEÑA:");

        BotonGuardarValidacion.setBackground(new java.awt.Color(0, 204, 0));
        BotonGuardarValidacion.setText("GUARDAR");
        BotonGuardarValidacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonGuardarValidacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TexNuevaContraseña))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TexNuevaContraseña2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TexSeguridad))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(BotonValidar)
                        .addGap(25, 25, 25)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(BotonGuardarValidacion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TexSeguridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BotonValidar))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TexNuevaContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(TexNuevaContraseña2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BotonGuardarValidacion)
                .addGap(20, 20, 20))
        );

        jPanel3.add(jPanel4);
        jPanel4.setBounds(360, 260, 300, 240);

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel7.setText("<html><center>POR FAVOR ESCRIBA<p>LA ACTUAL CONTRASEÑA<html>");

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel10.setText("REPITA CONTRASEÑA:");

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 11)); // NOI18N
        jLabel9.setText("NUEVA CONTRASEÑA:");

        jButton1.setBackground(new java.awt.Color(0, 204, 0));
        jButton1.setText("GUARDAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel39.setForeground(new java.awt.Color(255, 0, 0));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("*");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(106, 106, 106)
                                .addComponent(jButton1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(TexPasswordActual))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TexNuevaContraseñaCambio2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TexNuevaContraseñaCambio, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TexPasswordActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(TexNuevaContraseñaCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(TexNuevaContraseñaCambio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );

        jPanel3.add(jPanel5);
        jPanel5.setBounds(10, 260, 300, 240);

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void INGRESARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INGRESARActionPerformed
        String usu=txtusuario.getText();
        String pas=new String(txtcontra.getPassword());
        
        String PasswordIncriptadoMD5 = DigestUtils.md5Hex(pas);
        acceder(usu, PasswordIncriptadoMD5);
    }//GEN-LAST:event_INGRESARActionPerformed

    private void txtcontraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcontraKeyTyped
        char tecla = evt.getKeyChar();
        if(tecla==KeyEvent.VK_ENTER){
               INGRESAR.doClick();
        }
    }//GEN-LAST:event_txtcontraKeyTyped

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        String Cedula = JOptionPane.showInputDialog("Ingrese su numero de cedula");
        metodo.setCedula(Cedula);
        CodigoDeSeguridad(Cedula);
        

    }//GEN-LAST:event_jLabel3MouseClicked

    private void BotonValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonValidarActionPerformed
     String codigo = metodo.getCodigo();
     String codigo2 = TexSeguridad.getText();
     if(codigo.equals(codigo2)){
        JOptionPane.showMessageDialog(null, "El codigo de seguridad es correcto");
        TexNuevaContraseña.setEnabled(true);
        TexNuevaContraseña2.setEnabled(true);   
         
     }else{
         JOptionPane.showMessageDialog(null, "El codigo de seguridad es incorrecto","Error",JOptionPane.ERROR_MESSAGE);
         
     }
        
        

    }//GEN-LAST:event_BotonValidarActionPerformed

    private void BotonGuardarValidacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonGuardarValidacionActionPerformed
        String Password1 = TexNuevaContraseña.getText(); // captura los caracteres de los leibols
        String Password2 = TexNuevaContraseña2.getText();

        String cedula = metodo.getCedula();

        String PasswordIncriptadoMD5 = DigestUtils.md5Hex(Password1); // incripta la contraseña
        if (TexNuevaContraseña.getText().length() >= 12) { // cuenta iniciando desde el 0
            if (!Password1.equals(Password2)) { // Equals funcion para comparar variables
                JOptionPane.showMessageDialog(null, "Revise la contraseña, son diferentes");

            } else {

                String sql = "UPDATE persona set password = '" + PasswordIncriptadoMD5 + "' WHERE cc = '" + cedula + "'";

                try {
                    Statement st = cn.createStatement();
                    int es = st.executeUpdate(sql); // executeUpdate eliminar actualizar y insertar
                    JOptionPane.showMessageDialog(null, "Contraseña cambiada");
                    jPanel4.setVisible(false);
                    jPanel1.setVisible(true);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }

            }
        }else{
            JOptionPane.showMessageDialog(null, "La contraseña debe ser mas de 12 caracteres");
        }
    }//GEN-LAST:event_BotonGuardarValidacionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String usuario = metodo.getUsuario(); // nombre del usuario
        String PasswordActual = TexPasswordActual.getText();
        String Password1 = TexNuevaContraseñaCambio.getText(); // captura los caracteres de los jTexfiles
        String Password2 = TexNuevaContraseñaCambio2.getText();
        String PasswordActualMD5 = DigestUtils.md5Hex(PasswordActual); // incripta la contraseña
        String PasswordIncriptadoMD5 = DigestUtils.md5Hex(Password1); // incripta la contraseña

        if (TexPasswordActual.getText().isEmpty()) {
            jLabel39.setText("Campo Obligatorio"); // que el campo no este vacio
        } else if (TexPasswordActual.getText().length() < 12) {// si es menor a 12
            jLabel39.setText("la contraseña debe tener 12 caracteres"); // que tenga minimo 12 caracteres
        } else if (Password1.equals(Password2) && TexNuevaContraseñaCambio.getText().length() >= 12) {// cuenta iniciando desde el 0
            jLabel39.setText("");
            BuscarUsuarioCambioContraseña(usuario,PasswordActualMD5); // metodo para verificar el usuario y la contraseña
} else {
            jLabel39.setText("");
            JOptionPane.showMessageDialog(null, "Error con el cambio de contraseña:\n  1. Revise que tenga minimo 12 caracteres\n  2. Las contraseñas deben que ser iguales");

        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login(0,"").setVisible(true);// se inicializa el parametro en 0 para que cargue los componentes de inicio y no de cambio de contraseña
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonGuardarValidacion;
    private javax.swing.JButton BotonValidar;
    private javax.swing.JButton INGRESAR;
    private javax.swing.JPasswordField TexNuevaContraseña;
    private javax.swing.JPasswordField TexNuevaContraseña2;
    private javax.swing.JPasswordField TexNuevaContraseñaCambio;
    private javax.swing.JPasswordField TexNuevaContraseñaCambio2;
    private javax.swing.JPasswordField TexPasswordActual;
    private javax.swing.JTextField TexSeguridad;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField txtcontra;
    private javax.swing.JTextField txtusuario;
    // End of variables declaration//GEN-END:variables
}
