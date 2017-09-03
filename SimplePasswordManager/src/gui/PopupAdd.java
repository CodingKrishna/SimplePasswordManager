package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import utility.PasswordGenerator;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.SystemColor;
import javax.swing.JCheckBox;

public class PopupAdd extends JFrame {

   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JTextField textApp;
   private JLabel lblUsername;
   private JTextField textUsername;
   private JLabel lblPassword;
   private JButton btnSubmit;
   private DefaultTableModel model;
   private JPasswordField passwordField;
   private JLabel lblStrength;
   private JLabel lblTitle;
   private JLabel lblApp;
   private HashMap<Integer, ArrayList<Character>> passwords = new HashMap<Integer, ArrayList<Character>>(); // new
                                                                                                            // reference
                                                                                                            // to
                                                                                                            // password
                                                                                                            // for
                                                                                                            // safety.
   private JTextField textGenerate;
   private JButton btnGenerate;
   private JCheckBox chckbxShowPassword;
   private final char defaultEchoChar;

   public PopupAdd(DefaultTableModel model, HashMap<Integer, ArrayList<Character>> passwords) {
      this.model = model;
      this.passwords = passwords;
      drawAdd();
      defaultEchoChar = passwordField.getEchoChar();
   }

   public void drawAdd() {
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setBounds(100, 100, 256, 343);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      lblTitle = new JLabel("Add a New Field");
      lblTitle.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 18));
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setBounds(10, 0, 220, 23);
      contentPane.add(lblTitle);

      lblApp = new JLabel("Application/Thing");
      lblApp.setBounds(10, 36, 205, 14);
      contentPane.add(lblApp);

      textApp = new JTextField();
      textApp.setBounds(10, 61, 220, 20);
      contentPane.add(textApp);
      textApp.setColumns(10);

      lblUsername = new JLabel("Username");
      lblUsername.setBounds(10, 92, 205, 14);
      contentPane.add(lblUsername);

      textUsername = new JTextField();
      textUsername.setBounds(10, 117, 220, 20);
      contentPane.add(textUsername);
      textUsername.setColumns(10);

      lblStrength = new JLabel("Strength: ");
      lblStrength.setForeground(Color.LIGHT_GRAY);
      lblStrength.setBounds(10, 224, 205, 14);
      contentPane.add(lblStrength);

      lblPassword = new JLabel("Generate/Type password:");
      lblPassword.setBounds(10, 148, 205, 14);
      contentPane.add(lblPassword);

      passwordField = new JPasswordField();
      passwordField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            String strength = PasswordGenerator
                  .strengthConverter(PasswordGenerator.getPasswordStrength(new String(passwordField.getPassword())));
            lblStrength.setText("Strength: " + strength);
         }
      });
      passwordField.setBounds(10, 239, 220, 20);
      contentPane.add(passwordField);

      btnSubmit = new JButton("Submit");
      btnSubmit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // check for complete fields
            if (textApp.getText() == "" || textUsername.getText() == "" || (passwordField.getPassword().length < 1)) {
               lblTitle.setText("Complete all fields!");
               lblTitle.setForeground(Color.RED);
               return;
            }

            // loop because we dont know the order of things
            int newid = -1;
            int n = model.getRowCount();
            for (int i = 0; i < n; i++) {
               int id = Integer.parseInt((String) model.getValueAt(i, 0));
               if (id > newid) {
                  newid = id;
               }
            }

            newid++; // new id is the highest id plus one. (ofc there is problem
                     // of overflow but idgaf)

            ArrayList<Character> password = new ArrayList<Character>();
            for (char c : passwordField.getPassword()) { // convert to list
               password.add(c);
            }
            passwords.put(newid, password); // store list in general list
            model.insertRow(n, new String[] { Integer.toString(newid), textApp.getText(), textUsername.getText(),
                  String.format("%0" + passwords.get(newid).size() + "d", 0).replace("0", "*") // create
                                                                                               // length
                                                                                               // times
                                                                                               // "*"
                                                                                               // string
            });
            dispose();
         }
      });
      btnSubmit.setBounds(10, 270, 220, 23);
      contentPane.add(btnSubmit);

      textGenerate = new JTextField();
      textGenerate.setBackground(SystemColor.controlHighlight);
      textGenerate.setBounds(10, 169, 128, 20);
      contentPane.add(textGenerate);
      textGenerate.setColumns(10);

      btnGenerate = new JButton("Generate");
      btnGenerate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            textGenerate.setText(PasswordGenerator.generateStrongPassword(16));
         }
      });
      btnGenerate.setBounds(138, 168, 92, 23);
      contentPane.add(btnGenerate);

      chckbxShowPassword = new JCheckBox("Show password");
      chckbxShowPassword.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (chckbxShowPassword.isSelected()) {
               passwordField.setEchoChar((char) 0);
            } else {
               passwordField.setEchoChar(defaultEchoChar);
            }
         }
      });
      chckbxShowPassword.setBounds(6, 194, 228, 23);
      contentPane.add(chckbxShowPassword);
   }
}
