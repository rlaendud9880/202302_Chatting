package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.Gson;

import dto.ExitReqDto;
import dto.JoinReqDto;
import dto.JoinRoomReqDto;
import dto.MessageReqDto;
import dto.RequestDto;
import dto.RoomReqDto;
import lombok.Getter;

@Getter
public class Client extends JFrame {

	private JPanel mainPanel;
	private ImagePanel logo;
	private JTextField usernameField;
	private JTextField messageInput;
	private CardLayout mainCard;
	private JTextArea chatArea = new JTextArea();
	private JList<String> userList;
	private DefaultListModel<String> roomListModel;
	private JLabel roomTitleLabel;
	private JTextField ipInput;
	private JTextField portInput;
	
	private static Client instance;

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	private Socket socket;
	private Gson gson;
	private String username;
	private String roomname;
	private String joineduser;

	private boolean isConnected = false;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = Client.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Client() {

		gson = new Gson();

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
		
		
		JButton btnNewButton = new JButton("연결");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isConnected) {

					String ip = null;
					int port = 0;

					ip = ipInput.getText();
					port = Integer.parseInt(portInput.getText());

					try {
						socket = new Socket(ip, port); // #1 생성이되면
						isConnected = true;

						ClientRecive clientRecive = new ClientRecive(socket);
						clientRecive.start();

						username = usernameField.getText();
						RequestDto requestDto = new RequestDto("join", username);
						String requsetDtoJson = gson.toJson(requestDto);

						JOptionPane.showMessageDialog(null, username + "서버 접속", "접속성공",
								JOptionPane.INFORMATION_MESSAGE);

						OutputStream outputStream = socket.getOutputStream();
						PrintWriter out = new PrintWriter(outputStream, true);
						out.println(requsetDtoJson);
						mainCard.show(mainPanel, "chatListPanel");

					} catch (ConnectException e1) {

						JOptionPane.showMessageDialog(null, 
								"서버 접속 실패", 
								"접속실패", JOptionPane.ERROR_MESSAGE);

					} catch (UnknownHostException e1) {
						e1.printStackTrace();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
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

		ipInput = new JTextField();
		ipInput.setText("127.0.0.1");
		ipInput.setBounds(48, 639, 116, 21);
		joinPanel.add(ipInput);
		ipInput.setColumns(10);

		ipInput.setEditable(false);
		ipInput.setVisible(false);

		portInput = new JTextField();
		portInput.setText("9090");
		portInput.setBounds(176, 639, 116, 21);
		joinPanel.add(portInput);
		portInput.setColumns(10);

		portInput.setEditable(false);
		portInput.setVisible(false);

		JPanel chatListPanel = new JPanel();
		chatListPanel.setBackground(new Color(255, 255, 0));
		mainPanel.add(chatListPanel, "chatListPanel");
		chatListPanel.setLayout(null);

		JPanel smallLogoPanel1 = new JPanel();
		smallLogoPanel1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		smallLogoPanel1.setBounds(0, 0, 103, 111);
		logo = new ImagePanel(new ImageIcon("C:/hyunsu/hyunsu/workspace/TestProject1/image/kakao_logo.png").getImage());
		chatListPanel.add(smallLogoPanel1);
		smallLogoPanel1.setLayout(null);
		
		roomListModel = new DefaultListModel<>();
		userList = new JList<String>(roomListModel);
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					System.out.println("클릭완료");
					roomname = userList.getSelectedIndex() == 0 ? null : userList.getSelectedValue();
					joineduser = username;
//					System.out.println(roomname + ","+ joineduser);
					JoinRoomReqDto joinRoomReqDto = new JoinRoomReqDto(roomname, joineduser);
					
					OutputStream outputStream;
					
					try {
						
						
						chattingRoom(roomname);
							
						outputStream = socket.getOutputStream();
						PrintWriter out = new PrintWriter(outputStream, true);
						
						RequestDto requsetDto = new RequestDto("joinRoom", gson.toJson(joinRoomReqDto));
						out.println(gson.toJson(requsetDto));
						System.out.println(gson.toJson(requsetDto));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					 mainCard.show(mainPanel, "chatRoomPanel");
				}
				
			}
		});
		userList.setBounds(109, 0, 355, 761);
		
		JScrollPane roomListScroll = new JScrollPane();
		roomListScroll.setBounds(109, 0, 355, 756);
		roomListScroll.setViewportView(userList);
		chatListPanel.add(roomListScroll);
		
		JPanel plusPanel = new JPanel();
		plusPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					
					ClientRecive clientRecive = new ClientRecive(socket);
					clientRecive.start();

					roomname = JOptionPane.showInputDialog(null, "방 이름을 입력하세요", 
								"이름 입력", JOptionPane.INFORMATION_MESSAGE);
					
					RequestDto requestDto = new RequestDto("plus", roomname);
					String requsetDtoJson = gson.toJson(requestDto);
					
					chattingRoom(roomname);

					OutputStream outputStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outputStream,true);
					
					out.println(requsetDtoJson);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				mainCard.show(mainPanel, "chatRoomPanel"); // 바로 방으로 접속
			}
		});
		plusPanel.setBounds(0, 121, 103, 111);
		chatListPanel.add(plusPanel);
		plusPanel.setLayout(null);
	}
	
	public void chattingRoom(String roomname) {
	
			JPanel chatRoomPanel = new JPanel();
			mainPanel.add(chatRoomPanel, "chatRoomPanel");
			chatRoomPanel.setLayout(null);

			JScrollPane chatScroll = new JScrollPane();
			chatScroll.setBounds(0, 0, 464, 673);
			chatRoomPanel.add(chatScroll);

			
			chatArea = new JTextArea();
			chatArea.setEditable(false);
			chatArea.setLineWrap(true);
			chatArea.setWrapStyleWord(true);
			chatScroll.setViewportView(chatArea);

			
			JPanel columnHeader = new JPanel();
			chatScroll.setColumnHeaderView(columnHeader);
			BorderLayout bl_columnHeader = new BorderLayout();
			bl_columnHeader.setVgap(30);
			bl_columnHeader.setHgap(30);
			columnHeader.setLayout(bl_columnHeader);

			roomTitleLabel = new JLabel();
			columnHeader.add(roomTitleLabel, BorderLayout.CENTER);

			JButton exitButton = new JButton("exit");
			exitButton.addMouseListener(new MouseAdapter() {
				@Override
					public void mouseClicked(MouseEvent e) {
						
					try {
						
						ClientRecive clientRecive = new ClientRecive(socket);
						clientRecive.start();

						RequestDto requestDto = new RequestDto("exit", roomname);
						String reqestToJson = gson.toJson(requestDto);
						
						OutputStream outputStream = socket.getOutputStream();
						PrintWriter out = new PrintWriter(outputStream,true);
						
						out.println(reqestToJson);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
						
					mainCard.show(mainPanel, "chatListPanel");
				}
			});
			columnHeader.add(exitButton, BorderLayout.EAST);

			JPanel smallLogoPanel2 = new JPanel();
			FlowLayout flowLayout = (FlowLayout) smallLogoPanel2.getLayout();
			flowLayout.setVgap(30);
			flowLayout.setHgap(30);
			columnHeader.add(smallLogoPanel2, BorderLayout.WEST);
	
			JScrollPane sendScroll = new JScrollPane();
			sendScroll.setBounds(0, 673, 358, 88);
			chatRoomPanel.add(sendScroll);
	
			messageInput = new JTextField();
			messageInput.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						
						sendMessage();
					}
				}
			});
			sendScroll.setViewportView(messageInput);
			messageInput.setColumns(10);
	
			JButton sendButton = new JButton("send");
			sendButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					sendMessage();
				}
			});
	
			sendButton.setBounds(355, 673, 109, 88);
			chatRoomPanel.add(sendButton);

	}

	private void sendRequest(String resource, String body) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);

			RequestDto requestDto = new RequestDto(resource, body);

			out.println(gson.toJson(requestDto));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private void sendMessage() {
		if (!messageInput.getText().isBlank()) {
			String toUser = userList.getSelectedIndex() == 0 ? roomname : userList.getSelectedValue();
			String message = messageInput.getText();
			MessageReqDto messageReqDto = new MessageReqDto(toUser, username, message);
			sendRequest("sendMessage", gson.toJson(messageReqDto));
			messageInput.setText("");
		}
	}

}
