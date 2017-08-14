package gui;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class PopupRemove extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane; 
	private JTextField textID;
	private DefaultTableModel model;
	private JLabel lblId;
	private JButton btnSubmit;

	/**
	 * Create the frame.
	 */
	public PopupRemove(DefaultTableModel model) {
		this.model = model;
		drawRemove();
	}
	
	public void drawRemove(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 150, 140);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Remove a field:");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 11, 114, 14);
		getContentPane().add(lblTitle);
		
		textID = new JTextField();
		textID.setBounds(32, 36, 92, 20);
		getContentPane().add(textID);
		textID.setColumns(10);
		
		lblId = new JLabel("ID");
		lblId.setBounds(10, 38, 46, 14);
		getContentPane().add(lblId);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = -1;
				int n = model.getRowCount();
				try{
					id = Integer.parseInt(textID.getText());
					if(!( id < 0 || id > n )){
						boolean found = false;
						for( int i = 0; i < n; i++ ){ // linear search 
							int id2 = Integer.parseInt( (String) model.getValueAt(i, 0) ); // get ids
							if( id == id2 ){
								model.removeRow(i);
								found = true;
								break; // dont forget to break since we are removing row
							}
						}
						
						if( !found ){
							lblTitle.setForeground(Color.RED);
							lblTitle.setText("	ID was not found!");
						}else{
							dispose();
						}
						
					}else{
						lblTitle.setForeground(Color.RED);
						lblTitle.setText("ID is an Integer > 0!");
					}
				}catch(NumberFormatException e){
					lblTitle.setForeground(Color.YELLOW);
					lblTitle.setText("Invalid ID!");
				}
			}
		});
		btnSubmit.setBounds(10, 67, 114, 23);
		getContentPane().add(btnSubmit);
	}

}
