package gui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import utility.Encryption;
import utility.StorageHandler;
import utility.StringSimilar;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 5081954105464997549L;
	public static final String PATH = "program_data.json";
	public final String INITVECTOR;
	public final StorageHandler sh;
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private JPanel contentPane;
	private JTable dataTable;
	private DefaultTableModel model; 
	private JScrollPane scrollPane;
	private JTextField searchField;
	private Object[][] initialData;
	private HashMap<Integer, ArrayList<Character>> passwords = new HashMap<Integer, ArrayList<Character>>();
	private String[] columns;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnShow;
	private JLabel lblSearch;
	private JLabel lblSearchResult;
	@SuppressWarnings("rawtypes")
	private JComboBox typeBox;
	private JButton btnClear;
	private JButton btnSave;
	private JButton btnCopy;
	
	/**
	 * Create the frame.
	 */
	public UserInterface() {
		this.setTitle("Simple Password Manager");
		INITVECTOR = Encryption.getInitVector();
		sh =  new StorageHandler(PATH);
		columns = new String[] { "ID", "Application/Account", "Username", "Password" };
		initialData = load();
		draw();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void draw(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 764, 462);
		contentPane.add(scrollPane);
		
		model = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;
			@Override 
			 public boolean isCellEditable(int row, int column){
				 if( column==0 || column==3 ) // dont allow to edit id and passwords.
					 return false;
				 
				 return true;
			 }
		};
        model.setColumnIdentifiers(columns);
        loadInitialData();
        
		dataTable = new JTable();
        dataTable.setModel(model);
        dataTable.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(dataTable);
		
		lblSearch = new JLabel("Search/copy:");
		lblSearch.setBounds(10, 514, 106, 14);
		contentPane.add(lblSearch);
		
		typeBox = new JComboBox();
		typeBox.setModel(new DefaultComboBoxModel(columns));
		typeBox.setBounds(10, 530, 87, 20);
		
		contentPane.add(typeBox);
		
		lblSearchResult = new JLabel("no results found..");
		lblSearchResult.setForeground(Color.LIGHT_GRAY);
		lblSearchResult.setBounds(177, 514, 621, 14);
		contentPane.add(lblSearchResult);
		
		searchField = new JTextField();
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String search = searchField.getText() + e.getKeyChar();
				String type = (String) typeBox.getSelectedItem();
				lblSearchResult.setText("Searching for \"" + search + "\" on section: " + type  );	
				search(search, type);
			}
		});
		searchField.setBounds(177, 530, 597, 20);
		contentPane.add(searchField);
		searchField.setColumns(10);
		
		btnAdd = new JButton("Add\r\n");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PopupAdd frame = new PopupAdd(model, passwords);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnAdd.setBounds(10, 484, 157, 23);
		contentPane.add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PopupRemove frame = new PopupRemove(model);
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRemove.setBounds(177, 484, 157, 23);
		contentPane.add(btnRemove);
		
		btnSave = new JButton("Save");
		btnSave.setBackground(new Color(255, 127, 80));
		btnSave.setForeground(new Color(0, 0, 0));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
				btnSave.setBackground(Color.GREEN);
				service.schedule(new Runnable(){
	            public void run(){
	            	btnSave.setBackground(new Color(255, 127, 80));
	            }
				}, 1, TimeUnit.SECONDS);
			}
		});
		btnSave.setBounds(678, 484, 96, 23);
		contentPane.add(btnSave);
		
		btnClear = new JButton("Clear\r\n");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.setRowCount(0);
			}
		});
		btnClear.setBounds(344, 484, 157, 23);
		contentPane.add(btnClear);
		
		btnShow = new JButton("Show Passwords");
		btnShow.addActionListener(new ActionListener() {
			private boolean toggled = false;
			public void actionPerformed(ActionEvent arg0) {
				toggled = !toggled;
				int nRow = model.getRowCount();
				if(toggled){
					for (int i = 0; i < nRow; i++)				 
						model.setValueAt(getStringRepresentation(passwords.get(i)), i, 3);
				}else{
					for (int i = 0; i < nRow; i++)				 
						model.setValueAt(String.format("%0" + passwords.get(i).size() + "d", 0).replace("0","*"), i, 3);
				}
			}
		});
		btnShow.setBounds(511, 484, 157, 23);
		contentPane.add(btnShow);
		
		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent arg0) {
		      String type = (String) typeBox.getSelectedItem();
		      int type_index = -1;
		      int nCol = model.getColumnCount();
		      
		      for (int j = 0; j < nCol; j++ ) // get the index to copy on
		         type_index = (type.equals(columns[j])) ? j : type_index; 
		      
		      int selected_row_index = 0;  // if it is not selected default to top
		      if( dataTable.isRowSelected(0) ){
		         selected_row_index = dataTable.getSelectedRow();
		      }
		      
		      // save to clipboard
		      StringSelection stringSelection = new StringSelection((String) model.getValueAt(selected_row_index, type_index));
		      Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
		      clpbrd.setContents (stringSelection, null);
		      
		      // color change to notify the user that it worked
		      Color prev = btnCopy.getBackground();
		      btnCopy.setBackground(Color.GREEN);
		      service.schedule(new Runnable(){
               public void run(){
                  btnCopy.setBackground(prev);
               }
            }, 400, TimeUnit.MILLISECONDS);
		   }
		});
		btnCopy.setBounds(98, 530, 69, 20);
		contentPane.add(btnCopy);
	}
	
	private void loadInitialData(){
		if( initialData != null ){
	        for( int i = 0; i < initialData.length; i++){
	        	String strpass = (String) initialData[i][3]; // remove it out of this
	        	initialData[i][3] = String.format("%0" + strpass.length() + "d", 0).replace("0","*"); // place non sensitive here
	        	ArrayList<Character> password = new ArrayList<Character>(); // store it here
				for (char c : strpass.toCharArray() ) { // convert to list
					password.add(c);
				}
				passwords.put( Integer.parseInt((String) initialData[i][0]) , password );	
				model.addRow(initialData[i]);
	        }
        }
	}
	
	private void save(){
		int nRow = model.getRowCount(), nCol = model.getColumnCount();
	    Object[][] tableData = new Object[nRow][nCol];
	    for (int i = 0; i < nRow; i++){
	        for (int j = 0; j < nCol; j++){
	        	if( j==3 ){ // is a password	
	        		try {
	        		   String str = getStringRepresentation(passwords.get(i));
                  tableData[i][j] = Encryption.encrypt(Encryption.getKey(str.toCharArray()), INITVECTOR, str);
               } catch (NoSuchAlgorithmException | IOException e) {
                  e.printStackTrace();
               } catch (InvalidKeySpecException e) {
                  e.printStackTrace();
               }
	        	}else{
	        		tableData[i][j] = model.getValueAt(i,j);;
	        	}
	        }
	    }
	    sh.saveJSONData(tableData);
	}
	 
	private Object[][] load(){
		Object[][] data = sh.getJSONData();
		
		if( data == null )
			return new Object[0][0];
		
		int nR = data.length;
		
		for (int i = 0; i < nR ; i++){
	        for (int j = 0; j < data[0].length; j++){
	        	if( j==3 ){ // is a password
	        		try {
	        		   String str = (String) data[i][j];
                  data[i][j] = Encryption.decrypt(Encryption.getKey(str.toCharArray()), INITVECTOR, str);
               } catch (NoSuchAlgorithmException | IOException e) {
                  e.printStackTrace();
               } catch (InvalidKeySpecException e) {
                  e.printStackTrace();
               }
	        	}
	        }
	    }
		
		return data; 	
	}
	
	private void search( String txt, String type ){
		if(txt.equals(""))
			return;
		
		int type_index = -1;
		int nRow = model.getRowCount(), nCol = model.getColumnCount();
		
		for (int j = 0; j < nCol; j++ ) // get the index to search on
			type_index = (type.equals(columns[j])) ? j : type_index; 
		
		
		System.out.println("text " + txt);
		System.out.println("type " + type);
		System.out.println("index " + type_index);
		
		if(type_index==-1)
			return;
		
		float largest = 0;
		int row_to_move = 0;
		for (int i = 0 ; i < nRow ; i++){
			String data = (String) model.getValueAt(i, type_index);	
			StringSimilar ss = new StringSimilar(txt, data);
			float value = ss.getDistance();
			if( largest <= value ){ // found something interesting
				largest = value;
				row_to_move = i;
			}
		}
		
		int spos = dataTable.getSelectedRow();
		int pos = (spos==-1) ? 0 : spos;
		model.moveRow(row_to_move, row_to_move, pos);
		dataTable.setRowSelectionInterval(0, 0); // selects first row
	}
	
	public String getStringRepresentation(ArrayList<Character> list){    
	    StringBuilder builder = new StringBuilder(list.size());
	    for(Character ch: list){
	        builder.append(ch);
	    }
	    return builder.toString();
	}
}
