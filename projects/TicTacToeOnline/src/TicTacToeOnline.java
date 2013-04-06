import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TicTacToeOnline extends JApplet {
	public static final long serialVersionUID = 1L;
	public static final String br = "\n";
	public JButton gb[][] = new JButton[3][3];
	public int[][] state = new int[3][3];
	public JTextArea log;
	public JTextField out;
	public JScrollPane jsp;
	public ImageIcon blank = new ImageIcon(getClass().getResource("blank.gif"));
	public ImageIcon x = new ImageIcon(getClass().getResource("x.gif"));
	public ImageIcon o = new ImageIcon(getClass().getResource("o.gif"));
	public ImageIcon myIcon;
	public boolean connected;
	public String nick = "";

	public void init() {
		TicTacToeOnline.Engine en = new TicTacToeOnline.Engine();

		JPanel gp = new JPanel();
		Container c = this.getContentPane();
		c.setLayout(new FlowLayout(0, 0, 0));
		gp.setPreferredSize(new Dimension(300, 300));
		gp.setBackground(Color.WHITE);
		gp.setLayout(new GridLayout(3, 3, 0, 0));

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				gb[row][col] = new JButton();
				gb[row][col].setActionCommand("" + row + col);
				gb[row][col].setPreferredSize(new Dimension(100, 100));
				gb[row][col].addActionListener(en);
				gb[row][col].setIcon(this.blank);
				state[row][col] = 0;
				gp.add(gb[row][col]);
			}

		}

		JPanel cp = new JPanel();
		cp.setPreferredSize(new Dimension(192, 300));
		cp.setLayout(new FlowLayout(0, 0, 0));

		log = new JTextArea();
		log.setEditable(false);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		log.setAutoscrolls(true);
		jsp = new JScrollPane(this.log, 22, 31);
		jsp.setPreferredSize(new Dimension(192, 280));
		jsp.setAutoscrolls(true);
		cp.add(jsp);

		log.setText("Welcome to TicTacToeOnline!!\nUse //help for a list of commands\n");

		out = new JTextField(17);
		out.addKeyListener(en);
		cp.add(out);

		c.add(gp);
		c.add(cp);

		while ((this.nick == null) || (this.nick.equals("")))
			this.nick = JOptionPane.showInputDialog("Please enter a Nick name");
	}

	public void showError(String err) {
		JScrollPane errorPane = new JScrollPane(new JTextArea(err));
		errorPane.setPreferredSize(new Dimension(300, 150));
		JOptionPane.showMessageDialog(this, errorPane, "Error", -1);
	}

	public void scrollUp() {
		JScrollBar vbar = this.jsp.getVerticalScrollBar();
		vbar.setValue(vbar.getMaximum());
	}

	public class Engine implements ActionListener, KeyListener {
		public int yourChoice = -1;
		public int oppChoice = -1;
		public int yourState = -1;
		public int moves = 0;
		public boolean playing = false;
		public boolean yourTurn = false;
		public TicTacToeOnline.Engine.Connection c;

		public Engine() {
		}

		public void actionPerformed(ActionEvent e) {
			if ((this.playing) && (this.yourTurn)) {
				int tempx = Integer.parseInt(((JButton) e.getSource())
						.getActionCommand().substring(0, 1));
				int tempy = Integer.parseInt(((JButton) e.getSource())
						.getActionCommand().substring(1, 2));
				if (TicTacToeOnline.this.state[tempx][tempy] == 0) {
					TicTacToeOnline.this.gb[tempx][tempy]
							.setIcon(TicTacToeOnline.this.myIcon);
					TicTacToeOnline.this.state[tempx][tempy] = (this.yourState == 1 ? 1
							: 2);
					try {
						this.c.sendGame("T" + tempx + tempy);
						this.yourTurn = false;
					} catch (IOException e1) {
						TicTacToeOnline.this.showError(e1.toString());
						e1.printStackTrace();
					}
				}
				try {
					if ((this.c.host) && (checkWinner() == 1)) {
						this.c.sendGame("win");
						this.c.win();
						this.c.newGame();
					}
				} catch (Exception e1) {
					TicTacToeOnline.this.showError(e1.toString());
					e1.printStackTrace();
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == 10)
				try {
					checkCmd();
				} catch (Exception e1) {
					TicTacToeOnline.this.showError(e1.toString());
					e1.printStackTrace();
				}
		}

		public void checkCmd() throws IOException, InterruptedException {
			String temp = TicTacToeOnline.this.out.getText();

			String[][] commands = { { "help", "List commands" },
					{ "clear", "Clear the log" }, { "host", "Host a game" },
					{ "connect", "Connect to a game" },
					{ "new", "Start a new game" },
					{ "nick", "Set a nick name" },
					{ "disconnect", "Close all connections" } };
			if (!temp.equals("")) {
				if (!temp.startsWith("//")) {
					if (TicTacToeOnline.this.connected) {
						TicTacToeOnline.this.log
								.append(TicTacToeOnline.this.nick + ": " + temp
										+ "\n");
						TicTacToeOnline.this.scrollUp();
						this.c.sendMSG(TicTacToeOnline.this.nick + ": " + temp);
						TicTacToeOnline.this.out.setText("");
					} else {
						TicTacToeOnline.this.log
								.append("Cannot send message, not connected\n");
					}
				} else {
					String temp2 = temp.substring(2);
					if (temp2.equalsIgnoreCase("help")) {
						TicTacToeOnline.this.log.append("Commands:\n\n");
						for (int i = 0; i < commands.length; i++) {
							for (int u = 0; u < commands[i].length; u++) {
								TicTacToeOnline.this.log.append(commands[i][u]
										+ "\n");
							}
							TicTacToeOnline.this.log.append("\n");
							TicTacToeOnline.this.scrollUp();
						}
					} else if (temp2.equalsIgnoreCase("clear")) {
						TicTacToeOnline.this.log.setText("");
					} else if (temp2.equalsIgnoreCase("host")) {
						onlineGame(true);
					} else if (temp2.equalsIgnoreCase("connect")) {
						onlineGame(false);
					} else if (temp2.equalsIgnoreCase("disconnect")) {
						this.c.sendSystem("dc");
						this.playing = false;
						this.c.bIn.close();
						this.c.bOut.close();
						this.c.s.close();
						this.c.ss.close();
					} else if (temp2.equalsIgnoreCase("new")) {
						this.c.sendGame("N");
						this.c.newGame();
					} else if (temp2.equalsIgnoreCase("nick")) {
						String temp1 = JOptionPane
								.showInputDialog("Please enter a new Nick Name");
						if (!temp1.equals("")) {
							TicTacToeOnline.this.nick = temp1;
							TicTacToeOnline.this.log
									.append("Nick name changed to "
											+ TicTacToeOnline.this.nick + "\n");
						} else {
							TicTacToeOnline.this.log
									.append("Invalid Nick Name entered \n");
						}
					} else {
						TicTacToeOnline.this.log
								.append("No such command.\nUse //help to list commands.\n");
					}
				}
				TicTacToeOnline.this.out.setText("");
			}
		}

		public int checkWinner() throws IOException, InterruptedException {
			this.moves += 1;
			if ((TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[1][1])
					&& (TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[2][2])
					&& (TicTacToeOnline.this.state[0][0] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[0][2] == TicTacToeOnline.this.state[1][1])
					&& (TicTacToeOnline.this.state[0][2] == TicTacToeOnline.this.state[2][0])
					&& (TicTacToeOnline.this.state[0][2] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[0][1])
					&& (TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[0][2])
					&& (TicTacToeOnline.this.state[0][0] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[1][0] == TicTacToeOnline.this.state[1][1])
					&& (TicTacToeOnline.this.state[1][0] == TicTacToeOnline.this.state[1][2])
					&& (TicTacToeOnline.this.state[1][0] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[2][0] == TicTacToeOnline.this.state[2][1])
					&& (TicTacToeOnline.this.state[2][0] == TicTacToeOnline.this.state[2][2])
					&& (TicTacToeOnline.this.state[2][0] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[1][0])
					&& (TicTacToeOnline.this.state[0][0] == TicTacToeOnline.this.state[2][0])
					&& (TicTacToeOnline.this.state[0][0] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[0][1] == TicTacToeOnline.this.state[1][1])
					&& (TicTacToeOnline.this.state[0][1] == TicTacToeOnline.this.state[2][1])
					&& (TicTacToeOnline.this.state[0][1] != 0))
				return 1;
			if ((TicTacToeOnline.this.state[0][2] == TicTacToeOnline.this.state[1][2])
					&& (TicTacToeOnline.this.state[0][2] == TicTacToeOnline.this.state[2][2])
					&& (TicTacToeOnline.this.state[0][2] != 0))
				return 1;
			if (this.moves == 9) {
				TicTacToeOnline.this.log.append("Draw!\n");
				this.c.sendGame("D");
				this.c.newGame();
				return 2;
			}
			return 0;
		}

		public void onlineGame(boolean hosting) throws IOException {
			if (hosting) {
				TicTacToeOnline.this.log.append("Opening connection\n");
				this.c = new TicTacToeOnline.Engine.Connection();
			} else {
				String ip = JOptionPane.showInputDialog(
						"Please enter the hosts IP", "10.153.2.118");
				if ((ip != null))
					try {
						this.c = new TicTacToeOnline.Engine.Connection(ip);
					} catch (Exception e1) {
						TicTacToeOnline.this.showError("Invalid IP");
					}
			}
		}

		public void keyPressed(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent arg0) {
		}

		public class Connection implements Runnable {
			public int port;
			public String ip;
			public BufferedReader bIn;
			public BufferedWriter bOut;
			public ServerSocket ss;
			public Socket s;
			public boolean host;
			public boolean newGame;

			public Connection() throws IOException {
				this.host = true;
				openConnection();
			}

			public Connection(String ip) {
				this.host = false;
				this.ip = ip;
				openConnection();
			}

			public void openConnection() {
				try {
					port = 31337;
					if (this.host) {
						this.ss = new ServerSocket(port);
						this.s = this.ss.accept();
					} else {
						this.s = new Socket(this.ip, port);
					}
					this.bIn = new BufferedReader(new InputStreamReader(
							this.s.getInputStream()));
					this.bOut = new BufferedWriter(new OutputStreamWriter(
							this.s.getOutputStream()));
					Thread t = new Thread(this);
					t.start();
					TicTacToeOnline.this.connected = true;
					TicTacToeOnline.this.log.append("Starting game!\n");
					gameStart();
				} catch (Exception e1) {
					TicTacToeOnline.this.showError(e1.toString());
					e1.printStackTrace();
				}
			}

			public void gameStart() throws IOException {
				if (!this.newGame) {
					TicTacToeOnline.Engine.this.yourChoice = JOptionPane
							.showConfirmDialog(TicTacToeOnline.this,
									"Would you like to go First?\nX is First");
					if (TicTacToeOnline.Engine.this.yourChoice == 0)
						TicTacToeOnline.Engine.this.yourChoice = 1;
					else if (TicTacToeOnline.Engine.this.yourChoice == 1) {
						TicTacToeOnline.Engine.this.yourChoice = 2;
					}
					sendGame("C" + TicTacToeOnline.Engine.this.yourChoice);
					if (this.host) {
						TicTacToeOnline.this.log
								.append("Waiting for opponent to choose\n");
						while (TicTacToeOnline.Engine.this.oppChoice == -1) {
							try {
								Thread.sleep(20L);
							} catch (Exception e1) {
								TicTacToeOnline.this.showError(e1.toString());
								e1.printStackTrace();
							}
						}
						if (TicTacToeOnline.Engine.this.yourChoice == TicTacToeOnline.Engine.this.oppChoice) {
							double temp1 = Math.random();
							double temp2 = Math.random();
							if (temp1 > temp2) {
								TicTacToeOnline.Engine.this.yourState = TicTacToeOnline.Engine.this.yourChoice;
								sendGame("F"
										+ (TicTacToeOnline.Engine.this.yourChoice == 1 ? 2
												: 1));
							} else {
								TicTacToeOnline.Engine.this.yourState = (TicTacToeOnline.Engine.this.yourChoice == 1 ? 2
										: 1);
								sendGame("F"
										+ TicTacToeOnline.Engine.this.yourChoice);
							}
						} else {
							TicTacToeOnline.Engine.this.yourState = TicTacToeOnline.Engine.this.yourChoice;
							sendGame("F"
									+ TicTacToeOnline.Engine.this.oppChoice);
						}
					} else {
						TicTacToeOnline.this.log
								.append("Waiting for host to respond\n");
						while (TicTacToeOnline.Engine.this.yourState == -1) {
							try {
								Thread.sleep(20L);
							} catch (Exception e1) {
								TicTacToeOnline.this.showError(e1.toString());
								e1.printStackTrace();
							}
						}
					}
					TicTacToeOnline.this.log.append("You are "
							+ (TicTacToeOnline.Engine.this.yourState == 1 ? "X"
									: "O") + "\n");
					TicTacToeOnline.this.scrollUp();
					TicTacToeOnline.this.myIcon = (TicTacToeOnline.Engine.this.yourState == 1 ? TicTacToeOnline.this.x
							: TicTacToeOnline.this.o);
					for (int row = 0; row < 3; row++)
						for (int col = 0; col < 3; col++)
							TicTacToeOnline.this.gb[row][col].setVisible(true);
				} else {
					TicTacToeOnline.Engine.this.yourState = (TicTacToeOnline.Engine.this.yourState == 1 ? 2
							: 1);
					TicTacToeOnline.this.log.append("You are "
							+ (TicTacToeOnline.Engine.this.yourState == 1 ? "X"
									: "O") + "\n");
					TicTacToeOnline.this.scrollUp();
					TicTacToeOnline.this.myIcon = (TicTacToeOnline.Engine.this.yourState == 1 ? TicTacToeOnline.this.x
							: TicTacToeOnline.this.o);
					this.newGame = false;
				}
				TicTacToeOnline.Engine.this.playing = true;
				TicTacToeOnline.Engine.this.yourTurn = (TicTacToeOnline.Engine.this.yourState == 1);
			}

			public void newGame() throws IOException, InterruptedException {
				for (int row = 0; row < 3; row++) {
					for (int col = 0; col < 3; col++) {
						TicTacToeOnline.this.gb[row][col]
								.setIcon(TicTacToeOnline.this.blank);
						TicTacToeOnline.this.state[row][col] = 0;
					}
				}
				this.newGame = true;
				TicTacToeOnline.Engine.this.moves = 0;
				gameStart();
			}

			public void sendMSG(String temp) throws IOException {
				this.bOut.write("\r\n");
				this.bOut.write("&&MSG&&" + temp + "\r\n");
				this.bOut.flush();
			}

			public void sendGame(String temp) throws IOException {
				this.bOut.write("\r\n");
				this.bOut.write("&&GAM&&" + temp + "\r\n");
				this.bOut.flush();
			}

			public void sendSystem(String temp) throws IOException {
				this.bOut.write("\r\n");
				this.bOut.write("&&SYS&&" + temp + "\r\n");
				this.bOut.flush();
			}

			public void run() {
				try {
					while (this.bIn.readLine() != null) {
						String temp = this.bIn.readLine();
						if (temp.startsWith("&&MSG&&")) {
							TicTacToeOnline.this.log.append(temp.substring(7)
									+ "\n");
							TicTacToeOnline.this.scrollUp();
						} else if (temp.startsWith("&&GAM&&")) {
							if (temp.substring(7).startsWith("C")) {
								TicTacToeOnline.Engine.this.oppChoice = Integer
										.parseInt(temp.substring(8));
							} else if (temp.substring(7).startsWith("F")) {
								TicTacToeOnline.Engine.this.yourState = Integer
										.parseInt(temp.substring(8));
							} else if (temp.substring(7).startsWith("T")) {
								int tempx = Integer.parseInt(temp.substring(8)
										.substring(0, 1));
								int tempy = Integer.parseInt(temp.substring(8)
										.substring(1, 2));
								TicTacToeOnline.this.gb[tempx][tempy]
										.setIcon(TicTacToeOnline.Engine.this.yourState == 1 ? TicTacToeOnline.this.o
												: TicTacToeOnline.this.x);
								TicTacToeOnline.this.state[tempx][tempy] = (TicTacToeOnline.Engine.this.yourState == 1 ? 2
										: 1);
								TicTacToeOnline.Engine.this.yourTurn = true;
								if ((this.host)
										&& (TicTacToeOnline.Engine.this
												.checkWinner() == 1)) {
									sendGame("lose");
									lose();
									newGame();
								}
							} else if (temp.substring(7).startsWith("N")) {
								newGame();
							} else if (temp.substring(7).startsWith("D")) {
								TicTacToeOnline.this.log.append("Draw!\n");
								newGame();
							} else if (temp.substring(7).startsWith("win")) {
								lose();
								newGame();
							} else if (temp.substring(7).startsWith("lose")) {
								win();
								newGame();
							}
						} else {
							if ((!temp.startsWith("&&SYS&&"))
									|| (!temp.substring(7).equals("dc")))
								continue;
							this.bIn.close();
							this.bOut.close();
							this.s.close();
							if (this.host)
								this.ss.close();
						}
					}
				} catch (Exception e1) {
					TicTacToeOnline.this.showError(e1.toString());
					e1.printStackTrace();
				}
			}

			public void lose() {
				TicTacToeOnline.this.log.append("You lost!\n");
				TicTacToeOnline.this.scrollUp();
			}

			public void win() {
				TicTacToeOnline.this.log.append("You Won!!\n");
				TicTacToeOnline.this.scrollUp();
			}
		}
	}
}