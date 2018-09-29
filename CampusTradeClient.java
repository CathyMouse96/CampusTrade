import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

class ImagePanel extends JPanel {
	private Image img;
	
	public ImagePanel(String img) {
	    this(new ImageIcon(img).getImage());
	  }

	  public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, 700, 650, null);
	  }
}

public class CampusTradeClient extends JFrame implements Runnable {
	
	/** Graphic User Interface */
	public final static String imgPath = "/Users/CMouse/Documents/workspace/TengQing1400012842/src/images/checkered.png";
	public final static String headerimgPath = "/Users/CMouse/Documents/workspace/TengQing1400012842/src/images/headerimg.png";
	
	private JPanel contentPane;
	private JPanel titlePanel;
	private JPanel centerPanel;
	private MainPage mainPage;
	private SubmitForm submitForm;
	private SearchResults searchResults;
	private ContactUs contactUs;
	
	private JButton homeButton;
	
	public CampusTradeClient() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setSize(700, 650);
		setTitle("Campus Trade");
		
		contentPane = (JPanel)getContentPane();
		contentPane.setLayout(null);
		
		homeButton = new JButton();
		homeButton.setBounds(new Rectangle(0, 0, 540, 100));
		homeButton.setIcon(new ImageIcon(headerimgPath));
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				showMainPage();
			}		
		});
		
		titlePanel = new JPanel();
		titlePanel.add(homeButton, null);
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setBounds(new Rectangle(80, 30, 540, 100));
		titlePanel.setLayout(null);
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBounds(new Rectangle(80, 150, 540, 450));
		centerPanel.setLayout(null);
		
		mainPage = new MainPage(this);
		mainPage.setBounds(new Rectangle(20, 20, 500, 410));
		
		submitForm = new SubmitForm(this);
		submitForm.setBounds(new Rectangle(20, 20, 500, 410));
		
		contactUs = new ContactUs(this);
		contactUs.setBounds(new Rectangle(20, 20, 500, 410));
		
		ImagePanel panel = new ImagePanel(new ImageIcon(imgPath).getImage());
		panel.setBounds(new Rectangle(0, 0, 700, 650));
		
		contentPane.add(titlePanel, null);
		contentPane.add(centerPanel, null);
		contentPane.add(panel, null);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		CampusTradeClient c = new CampusTradeClient();
		c.showMainPage();
		c.setVisible(true);
		c.startConnect();
	}
	
	private void showMainPage() {
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.revalidate();
		centerPanel.add(mainPage, null);
	}
	
	public void showMainClicked() {
		showMainPage();
	}
	
	private void showSubmitForm() {
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.revalidate();
		centerPanel.add(submitForm, null);
	}
	
	public void showSubmitClicked() {
		showSubmitForm();
	}
	
	private void showSearchResults(int c, String str) {
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.revalidate();
		searchResults = new SearchResults(this, c, str);
		searchResults.setBounds(new Rectangle(20, 20, 500, 410));
		centerPanel.add(searchResults, null);
	}
	
	public void showSearchClicked(int c, String str) {
		showSearchResults(c, str);
	}
	
	private void showContactUs() {
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.revalidate();
		centerPanel.add(contactUs, null);
	}
	
	public void showContactClicked() {
		showContactUs();
	}
	
	/** Networking */
	public final static int DEFAULT_PORT = 6789;
		
	Socket sock;
	Thread thread;
	BufferedReader in;
	PrintWriter out;
	boolean bConnected;
	
	public void startConnect() {
		bConnected = false;
		try {
			sock = new Socket("127.0.0.1", DEFAULT_PORT);
			bConnected = true;
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new java.io.PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(ERROR);
		}
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void run() {
		String line;
		int searchMode = 0;
		int searchReadNum = 6;
		int chatMode = 0;
		String c = "";
		String n = "";
		String p = "";
		String oN = "";
		String oID = "";
		String oP = "";
		while (true) {
			try {
				// read in a line
				line = receiveMsg();
				Thread.sleep(100L);
				if (line == null)
					break;
				if (line.equals("##SEARCH")) {
					searchMode = 1;
					searchReadNum = 0;
				}
				else if (line.equals("##ENDSEARCH")) {
					searchResults.refreshTable();
					searchMode = 0;
					searchReadNum = 6;
				}
				else if (line.equals("##CHAT"))
					chatMode = 1;
				else if (searchMode > 0) {
					switch(searchReadNum) {
					case 0:
						c = line;
						searchReadNum++;
						break;
					case 1:
						n = line;
						searchReadNum++;
						break;
					case 2:
						p = line;
						searchReadNum++;
						break;
					case 3:
						oN = line;
						searchReadNum++;
						break;
					case 4:
						oID = line;
						searchReadNum++;
						break;
					case 5:
						oP = line;
						searchReadNum = 0;
						GoodRecord gr = new GoodRecord(c, n, p, oN, oID, oP);
						searchResults.addRecord(gr);
					}
				}
				else if (chatMode > 0) {
				 	contactUs.addMsgFromServer(line);
					chatMode--;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException ei) {}
		}
	}
	
	public boolean sendMsg(String msg) {
		out.println(msg);
		out.flush();
		return true;
	}
	
	public String receiveMsg() throws IOException {
		String msg = new String();
		try {
			msg = in.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
}