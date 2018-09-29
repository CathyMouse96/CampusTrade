import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

class GoodRecord {
	private String category;
	private String name;
	private String price;
	private String ownerName;
	private String ownerID;
	private String ownerPhone;
	
	public GoodRecord(String c, String n, String p, String oN, String oI, String oP) {
		category = c;
		name = n;
		price = p;
		ownerName = oN;
		ownerID = oI;
		ownerPhone = oP;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public String getOwnerID() {
		return ownerID;
	}
	
	public String getOwnerPhone() {
		return ownerPhone;
	}
}

class ServerChatPanel extends JPanel implements ActionListener {
	private CampusTradeServer campusTradeServer;
	private List lstMsg = new List();
	private JTextField txtInput = new JTextField();
	private JButton btnSend = new JButton("Send");
	
	public ServerChatPanel(CampusTradeServer cts) {
		campusTradeServer = cts;
		setLayout(null);
		setBackground(Color.WHITE);
		
		lstMsg.setBounds(0, 0, 220, 210);
		lstMsg.setMultipleMode(false);
		txtInput.setBounds(0, 210, 160, 30);
		txtInput.addActionListener(this);
		btnSend.setBounds(160, 210, 60, 30);
		btnSend.addActionListener(this);
		
		add(lstMsg, null);
		add(txtInput, null);
		add(btnSend, null);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String msg = txtInput.getText();
		if (msg != null && !msg.equals(""))
			campusTradeServer.sendButtonClicked(msg);
	}
	
	public String returnSelected() {
		return lstMsg.getSelectedItem();
	}
	
	public void addMsg(String msg) {
		lstMsg.add(msg);
	}
}

public class CampusTradeServer extends JFrame implements Runnable {
	
	/** Graphic User Interface */
	private JPanel contentPane;
	
	private JLabel systemPrompt = new JLabel("System Messages:");
	private JLabel submitPrompt = new JLabel("New Records:");
	private JLabel chatPrompt = new JLabel("Client Messages:");
	
	private List lstSystem = new List();
	private List lstSubmit = new List();
	public ServerChatPanel serverChat = new ServerChatPanel(this);
	
	public CampusTradeServer() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setSize(500, 400);
		setTitle("Campus Trade Server");
		
		contentPane = (JPanel)getContentPane();
		contentPane.setLayout(null);
		
		systemPrompt.setBounds(25, 10, 450, 20);
		lstSystem.setBounds(25, 30, 450, 60);
		submitPrompt.setBounds(25, 100, 220, 20);
		lstSubmit.setBounds(25, 120, 220, 240);
		chatPrompt.setBounds(255, 100, 220, 20);
		serverChat.setBounds(255, 120, 220, 240);
		
		contentPane.add(systemPrompt, null);
		contentPane.add(lstSystem, null);
		contentPane.add(submitPrompt, null);
		contentPane.add(lstSubmit, null);
		contentPane.add(chatPrompt, null);
		contentPane.add(serverChat, null);
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		CampusTradeServer server = new CampusTradeServer();
		server.setVisible(true);
		server.initialRead();
		server.ServerListen();
	}
	
	/** Do Search */
	private int searchCategory = 0;
	private String searchKeyword = null;
	
	java.util.Vector<GoodRecord> goodRecords;
	
	public void initialRead() {
		goodRecords = new java.util.Vector<GoodRecord>();
		File file = new File(dbPath);
		if (!file.exists())
			return;
		try {
			FileReader fileReader = new FileReader(file.getName());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 0;
			String c = "";
			String n = "";
			String p = "";
			String oN = "";
			String oID = "";
			String oP = "";
			String line = bufferedReader.readLine();
			while (line != null) {
				switch(i) {
				case 0:
					c = line;
					i++;
					break;
				case 1:
					n = line;
					i++;
					break;
				case 2:
					p = line;
					i++;
					break;
				case 3:
					oN = line;
					i++;
					break;
				case 4:
					oID = line;
					i++;
					break;
				case 5:
					oP = line;
					i = 0;
					GoodRecord gr = new GoodRecord(c, n, p, oN, oID, oP);
					goodRecords.add(gr);
				}
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addToGoodRecords(GoodRecord gr) {
		goodRecords.add(gr);
	}
	
	public void doSearch(int cID) {
		if (cID < clients.size()) {
			Connection c = (Connection)clients.get(cID);
			try {
				c.sendMsg("##SEARCH");
				if (searchCategory == 0) {
					for (int i = 0; i < goodRecords.size(); i++) {
						if (searchKeyword != null) {
							if (searchKeyword.length() > 0 && goodRecords.elementAt(i).getName().indexOf(searchKeyword) == -1)
								continue;
						}
						c.sendMsg(goodRecords.elementAt(i).getCategory());
						c.sendMsg(goodRecords.elementAt(i).getName());
						c.sendMsg(goodRecords.elementAt(i).getPrice());
						c.sendMsg(goodRecords.elementAt(i).getOwnerName());
						c.sendMsg(goodRecords.elementAt(i).getOwnerID());
						c.sendMsg(goodRecords.elementAt(i).getOwnerPhone());
					}
				}
				else {
					for (int i = 0; i < goodRecords.size(); i++) {
						if (Integer.parseInt(goodRecords.elementAt(i).getCategory()) != searchCategory)
							continue;
						if (searchKeyword != null)
							if (searchKeyword.length() > 0 && goodRecords.elementAt(i).getName().indexOf(searchKeyword) == -1)
							continue;
						c.sendMsg(goodRecords.elementAt(i).getCategory());
						c.sendMsg(goodRecords.elementAt(i).getName());
						c.sendMsg(goodRecords.elementAt(i).getPrice());
						c.sendMsg(goodRecords.elementAt(i).getOwnerName());
						c.sendMsg(goodRecords.elementAt(i).getOwnerID());
						c.sendMsg(goodRecords.elementAt(i).getOwnerPhone());
					}
				}
				c.sendMsg("##ENDSEARCH");
			} catch (Exception ee) {}
		}
	}
	
	public void getSearchCategory(String s) {
		if (s != null)
			searchCategory = Integer.parseInt(s);
	}
	
	public void getSearchKeyword(String s) {
		searchKeyword = s;
	}
	
	/** Networking */
	public final static int DEFAULT_PORT = 6789;
	private final static String dbPath = "/Users/CMouse/Documents/workspace/TengQing1400012842/database.txt";
	
	protected ServerSocket listen_socket;
	Thread thread;
	java.util.Vector clients;
	int count = 0;
	
	/** Create a ServerSocket to listen for connections on; start the thread. */
	public void ServerListen() {
		try {
			listen_socket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lstSystem.add("Server: listening on port" + DEFAULT_PORT);
		clients = new java.util.Vector();
		thread = new Thread(this);
		thread.start();
	}
	
	/** The body of the server thread. Loop forever, listening for and accepting connections from clients. 
	 *  For each connection, create a Connection object to handle communication through the new Socket. */
	public void run() {
		try {
			while(true) {
				Socket client_socket = listen_socket.accept();
				Connection c = new Connection(client_socket, this, count);
				clients.add(c);
				count++;
				lstSystem.add("One Client Comes in");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processSubmitMsg(String str) {
		lstSubmit.add(str);
		File file = new File(dbPath);
		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file.getName(), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			if (str != null)
				printWriter.println(str);
			printWriter.flush();
			printWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processChatMsg(String str, int cID) {
		String msg = "Client" + cID + ": " + str;
		serverChat.addMsg(msg);
	}
	
	void sendButtonClicked(String msg) {
		String selected = serverChat.returnSelected();
		if (selected == null) {
			return;
		}
		char index = selected.charAt(6);
		int i = index - '0';
		if (i < clients.size()) {
			Connection c = (Connection)clients.get(i);
			try {
				c.sendMsg("##CHAT");
				c.sendMsg(msg);
			} catch (Exception ee) {}
		}
	}
}

/** This class is the thread that handles all communication with a client. */
class Connection extends Thread {
	protected Socket client;
	protected BufferedReader in;
	protected PrintWriter out;
	CampusTradeServer server;
	int clientID = 0;
	
	/** Initialize the streams and start the thread. */
	public Connection (Socket client_socket, CampusTradeServer server_frame, int cID) {
		client = client_socket;
		server = server_frame;
		clientID = cID;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new java.io.PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException e2) { }
			e.printStackTrace();
			return;
		}
		this.start();
	}
	
	/** Provide the service. Read a line. */
	public void run() {
		String line;
		int submitMode = 0;
		int searchMode = 0;
		int chatMode = 0;
		String c = "";
		String n = "";
		String p = "";
		String oN = "";
		String oID = "";
		String oP = "";
		try {
			while(true) {
				// read in a line
				line = receiveMsg();
				if (line == null)
					break;
				if (line.equals("#SUBMIT"))
					submitMode = 6;
				else if (line.equals("#SEARCH"))
					searchMode = 2;
				else if (line.equals("#CHAT"))
					chatMode = 1;
				else if (submitMode > 0) {
					switch(submitMode) {
					case 6:
						c = line;
						break;
					case 5:
						n = line;
						break;
					case 4:
						p = line;
						break;
					case 3:
						oN = line;
						break;
					case 2:
						oID = line;
						break;
					case 1:
						oP = line;
						GoodRecord gr = new GoodRecord(c, n, p, oN, oID, oP);
						server.addToGoodRecords(gr);
					}
					server.processSubmitMsg(line);
					submitMode--;
				}
				else if (searchMode > 0) {
					if (searchMode == 2)
						server.getSearchCategory(line);
					else if (searchMode == 1) {
						if (!line.equals("null"))
							server.getSearchKeyword(line);
						server.doSearch(clientID);
					}
					searchMode--;
				}
				else if (chatMode > 0) {
					server.processChatMsg(line, clientID);
					chatMode--;
				}
			}
		} catch (IOException e) {}
		finally {
			try {
				client.close();
			} catch (IOException e2) {}
		}
	}
	
	public String receiveMsg() throws IOException {
		String msg = new String();
		try {
			msg = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public void sendMsg(String msg) throws IOException {
		out.println(msg);
		out.flush();
	}
}