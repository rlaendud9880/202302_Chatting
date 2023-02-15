package client;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	private CardLayout mainCard;
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
		mainCard = new CardLayout();
		mainPanel.setLayout(mainCard);
		
		JPanel joinPanel = new JPanel();
		joinPanel.setBackground(new Color(255, 235, 0));
		mainPanel.add(joinPanel, "joinPanel");
		joinPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("join");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				JsonObject joinUser = new JsonObject();
//				joinUser.addProperty("usernameAndEmail", usernameField.getText());
				
				mainCard.show(mainPanel, "chatListPanel");
				
			}
		});
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
		mainPanel.add(chatListPanel, "chatListPanel");
		chatListPanel.setLayout(null);
		
		JPanel smallLogoPanel1 = new JPanel();
		smallLogoPanel1.setBounds(0, 0, 111, 111);
		logo = new ImagePanel(new ImageIcon("C:/hyunsu/hyunsu/workspace/TestProject1/image/kakao_logo.png").getImage());
		chatListPanel.add(smallLogoPanel1);
		smallLogoPanel1.setLayout(null);
		
		JButton plusButton = new JButton("+");
		plusButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainCard.show(mainPanel, "chatRoomPanel");
			}
		});
		plusButton.setBounds(0, 140, 111, 111);
		chatListPanel.add(plusButton);
		
		JScrollPane userListScroll = new JScrollPane();
		userListScroll.setBounds(109, 756, 355, -754);
		chatListPanel.add(userListScroll);
		
		userList = new JList();
		userList.setBounds(109, 0, 355, 761);
		chatListPanel.add(userList);
		
		JPanel chatRoomPanel = new JPanel();
		mainPanel.add(chatRoomPanel,"chatRoomPanel");
		chatRoomPanel.setLayout(null);
		
		JPanel smallLogoPanel2 = new JPanel();
		smallLogoPanel2.setBounds(0, 0, 70, 70);
		chatRoomPanel.add(smallLogoPanel2);
		smallLogoPanel2.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(70, 0, 288, 70);
		chatRoomPanel.add(lblNewLabel);
		
		JButton exitButton = new JButton("exit");
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				mainCard.show(mainPanel, "chatListPanel");
			}
		});
		exitButton.setBounds(355, 0, 109, 70);
		chatRoomPanel.add(exitButton);
		
		JScrollPane chatScroll = new JScrollPane();
		chatScroll.setBounds(0, 68, 464, 605);
		chatRoomPanel.add(chatScroll);
		
		JTextArea chatArea = new JTextArea();
		chatScroll.setViewportView(chatArea);
		
		JScrollPane sendScroll = new JScrollPane();
		sendScroll.setBounds(0, 673, 358, 88);
		chatRoomPanel.add(sendScroll);
		
		messageInput = new JTextField();
		messageInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		sendScroll.setViewportView(messageInput);
		messageInput.setColumns(10);
		
		JButton sendButton = new JButton("send");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		sendButton.setBounds(355, 673, 109, 88);
		chatRoomPanel.add(sendButton);
		

	}
	
    
    

	
}
