package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;
import utility.PasswordAuthentication;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.Font;

public class Authentication extends JFrame {

   private static final long serialVersionUID = 4060643900642552194L;
   private static final PasswordAuthentication pa = new PasswordAuthentication();
   private int counter = 3;
   private JPanel contentPane;
   private JButton btnUnlock;
   private JLabel lblEnterPassword;
   private JLabel lblState;
   private JPasswordField passwordField;

   /**
    * Create the frame.
    */
   public Authentication() {
      setTitle("Simple Password Manager");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 287, 161);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      btnUnlock = new JButton("Unlock");
      btnUnlock.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            String hash = PasswordAuthentication.getHashFromFile(Main.FILEPATH);

            if (counter < 1)
               System.exit(0);

            char[] salt = new String("1;*_=RlS_/;b 0AD(+Qx2&r43dD9<tvXNml5k|%Uqd|>W-{htC(YG+)qMAjU(`cw')")
                  .toCharArray();
            char[] password = passwordField.getPassword();
            char[] salt_password = new char[salt.length + password.length];
            System.arraycopy(password, 0, salt_password, 0, password.length);
            System.arraycopy(salt, 0, salt_password, password.length, salt.length);

            if (pa.authenticate(salt_password, hash)) {

               try {
                  dispose();
               } catch (Exception e) {
                  e.printStackTrace();
               }

               UserInterface frame = new UserInterface();
               frame.setVisible(true);
            } else {
               lblState.setText("Wrong password! Tries: " + counter);
               lblState.setForeground(Color.RED);
               counter--;
            }
         }
      });
      btnUnlock.setBounds(59, 88, 154, 23);
      contentPane.add(btnUnlock);

      lblEnterPassword = new JLabel("[LOCKED] Enter Password:");
      lblEnterPassword.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 12));
      lblEnterPassword.setBounds(10, 11, 251, 20);
      contentPane.add(lblEnterPassword);

      lblState = new JLabel("waiting..");
      lblState.setForeground(Color.LIGHT_GRAY);
      lblState.setBounds(10, 39, 179, 14);
      contentPane.add(lblState);

      passwordField = new JPasswordField();
      passwordField.setBounds(10, 54, 251, 23);
      contentPane.add(passwordField);
   }
}
