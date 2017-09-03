package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;
import utility.PasswordAuthentication;
import utility.PasswordGenerator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JCheckBox;

public class Register extends JFrame {

   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JPasswordField passwordField;
   private JButton btnRegister;
   private JLabel lblTitle;
   private JLabel lblText;
   private JLabel lblStrength;
   private JTextField textGenerate;
   private JButton btnGenerate;
   private JCheckBox chckbxShowPassword;
   private final char defaultEchoChar;

   /**
    * Create the frame.
    */
   public Register() {
      drawRegister();
      defaultEchoChar = passwordField.getEchoChar();
   }

   private void drawRegister() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 245, 232);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      btnRegister = new JButton("Register");
      btnRegister.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            // store password
            PasswordAuthentication pa = new PasswordAuthentication();
            /*
             * we are only using 1 user and 1 password final Random r = new
             * SecureRandom(); byte[] salt = new byte[32]; r.nextBytes(salt);
             * char[] array = new String(salt).toCharArray();
             */
            char[] salt = new String("1;*_=RlS_/;b 0AD(+Qx2&r43dD9<tvXNml5k|%Uqd|>W-{htC(YG+)qMAjU(`cw')")
                  .toCharArray();
            char[] password = passwordField.getPassword();
            char[] salt_password = new char[salt.length + password.length];
            System.arraycopy(password, 0, salt_password, 0, password.length);
            System.arraycopy(salt, 0, salt_password, password.length, salt.length);
            String hash = pa.hash(salt_password);
            PasswordAuthentication.storeHashToFile(hash, Main.FILEPATH);
            // start authentication
            Authentication frame = new Authentication();
            frame.setVisible(true);
            dispose();
         }
      });
      btnRegister.setBounds(10, 159, 209, 23);
      contentPane.add(btnRegister);

      lblTitle = new JLabel("Register Password\r\n");
      lblTitle.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 14));
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setBounds(10, 11, 209, 23);
      contentPane.add(lblTitle);

      lblText = new JLabel("Welcome! Generate/type password:");
      lblText.setBounds(10, 34, 209, 30);
      contentPane.add(lblText);

      lblStrength = new JLabel("Strength:");
      lblStrength.setForeground(Color.LIGHT_GRAY);
      lblStrength.setBounds(10, 113, 195, 14);
      contentPane.add(lblStrength);

      passwordField = new JPasswordField();
      passwordField.setBounds(10, 128, 209, 20);
      passwordField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            String strength = PasswordGenerator
                  .strengthConverter(PasswordGenerator.getPasswordStrength(new String(passwordField.getPassword())));
            lblStrength.setText("Strength: " + strength);
         }
      });
      contentPane.add(passwordField);

      textGenerate = new JTextField();
      textGenerate.setBackground(SystemColor.controlHighlight);
      textGenerate.setBounds(10, 63, 118, 20);
      textGenerate.setEditable(false);
      textGenerate.setColumns(10);
      contentPane.add(textGenerate);

      btnGenerate = new JButton("Generate");
      btnGenerate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            textGenerate.setText(PasswordGenerator.generateStrongPassword(16));
         }
      });
      btnGenerate.setBounds(128, 62, 91, 23);
      contentPane.add(btnGenerate);

      chckbxShowPassword = new JCheckBox("Show Password");
      chckbxShowPassword.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if (chckbxShowPassword.isSelected()) {
               passwordField.setEchoChar((char) 0);
            } else {
               passwordField.setEchoChar(defaultEchoChar);
            }
         }
      });
      chckbxShowPassword.setHorizontalAlignment(SwingConstants.LEFT);
      chckbxShowPassword.setBounds(6, 90, 209, 23);
      contentPane.add(chckbxShowPassword);
   }
}
