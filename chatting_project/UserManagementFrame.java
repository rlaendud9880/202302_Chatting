package chatting_project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class UserManagementFrame extends JFrame {
	private JTextField textField;
	private JTextField textField_1;
	public UserManagementFrame() {
		getContentPane().setBackground(new Color(255, 255, 0));
		getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(85, 345, 266, 56);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBackground(new Color(255, 255, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(85, 411, 266, 23);
		getContentPane().add(btnNewButton);
		
		textField_1 = new JTextField();
		textField_1.setBounds(85, 207, 266, 128);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
	}
}
