package client;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class ImagePanel extends JPanel {
	private Image img;

	public ImagePanel(Image img) {
		this.img = img;
		setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 3, 0, null);
	}
}

public class Client extends JFrame {

	private JPanel mainPanel;
	private JTextField usernameField;
	private JTextField messageInput;
	private ImagePanel logo;
	private JList userList;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 800);
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBorder(null);

		setContentPane(mainPanel);
		mainPanel.setLayout(new CardLayout(0, 0));
		
		JPanel joinPanel = new JPanel();
		joinPanel.setBackground(new Color(255, 235, 0));
		mainPanel.add(joinPanel, "name_352646027400");
		joinPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("join");
		btnNewButton.setForeground(new Color(254, 229, 0));
		btnNewButton.setBackground(new Color(254, 229, 0));
		btnNewButton.setIcon(new ImageIcon("C:\\hyunsu\\hyunsu\\workspace\\TestProject1\\Image\\k_join_btn.png"));
		btnNewButton.setBounds(91, 521, 300, 45);
		joinPanel.add(btnNewButton);
		
		JPanel logoPanel = new JPanel();
		logoPanel.setBounds(206, 243, 63, 63);
		logo = new ImagePanel(new ImageIcon("C:/hyunsu/hyunsu/workspace/TestProject1/image/kakao_logo.png").getImage());
		logo.setLocation(195, 238);
		joinPanel.add(logo);
		logoPanel.setLayout(null);
		
		usernameField = new JTextField();
		usernameField.setHorizontalAlignment(SwingConstants.CENTER);
		usernameField.setFont(new Font("굴림", Font.BOLD, 20));
		usernameField.setBounds(91, 406, 300, 40);
		joinPanel.add(usernameField);
		usernameField.setColumns(10);
		
		JPanel chatListPanel = new JPanel();
		mainPanel.add(chatListPanel, "name_352657145600");
		chatListPanel.setLayout(null);
		
		JPanel smallLogoPanel1 = new JPanel();
		smallLogoPanel1.setBounds(0, 0, 111, 122);
		logo = new ImagePanel(new ImageIcon("C:/hyunsu/hyunsu/workspace/TestProject1/image/kakao_logo.png").getImage());
		chatListPanel.add(smallLogoPanel1);
		
		JButton plusButton = new JButton("+");
		plusButton.setBounds(0, 121, 111, 67);
		chatListPanel.add(plusButton);
		
		JScrollPane userListScroll = new JScrollPane();
		userListScroll.setBounds(109, 756, 355, -754);
		chatListPanel.add(userListScroll);
		
		userList = new JList();
		userList.setBounds(109, 0, 355, 761);
		chatListPanel.add(userList);
		
		JPanel chattingPanel = new JPanel();
		mainPanel.add(chattingPanel);
		chattingPanel.setLayout(null);
		
		JPanel smallLogoPanel2 = new JPanel();
		smallLogoPanel2.setBounds(0, 0, 70, 70);
		chattingPanel.add(smallLogoPanel2);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(70, 0, 288, 70);
		chattingPanel.add(lblNewLabel);
		
		JButton exitButton = new JButton("exit");
		exitButton.setBounds(355, 0, 109, 70);
		chattingPanel.add(exitButton);
		
		JScrollPane talkScroll = new JScrollPane();
		talkScroll.setBounds(0, 68, 464, 605);
		chattingPanel.add(talkScroll);
		
		JTextArea talkArea = new JTextArea();
		talkScroll.setViewportView(talkArea);
		
		JScrollPane sendScroll = new JScrollPane();
		sendScroll.setBounds(0, 673, 358, 88);
		chattingPanel.add(sendScroll);
		
		messageInput = new JTextField();
		sendScroll.setViewportView(messageInput);
		messageInput.setColumns(10);
		
		JButton sendButton = new JButton("New button");
		sendButton.setBounds(355, 673, 109, 88);
		chattingPanel.add(sendButton);
		

	}
	
    
    

	
}
