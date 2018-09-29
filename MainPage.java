import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

class MainPage extends JPanel implements ActionListener {
	private SearchPanel searchPanel;
	private JButton submitFormButton;
	private JButton viewAllButton;
	private JButton contactUsButton;
	
	private final static String submitFormImgPath = "/Users/CMouse/Documents/workspace/TengQing1400012842/src/images/tradeout.png";
	private final static String viewAllImgPath = "/Users/CMouse/Documents/workspace/TengQing1400012842/src/images/tradein.png";
	
	private CampusTradeClient campusTradeClient;

	public MainPage(CampusTradeClient ct) {
		campusTradeClient = ct;
		
		setLayout(null);
		
		searchPanel = new SearchPanel(ct);
		searchPanel.setBounds(new Rectangle(0, 0, 500, 50));
		
		submitFormButton = new JButton();
		submitFormButton.setBounds(new Rectangle(10, 50, 235, 310));
		submitFormButton.setIcon(new ImageIcon(submitFormImgPath));
		submitFormButton.addActionListener(this);
		
		viewAllButton = new JButton();
		viewAllButton.setBounds(new Rectangle(255, 50, 235, 310));
		viewAllButton.setIcon(new ImageIcon(viewAllImgPath));
		viewAllButton.addActionListener(this);
		
		contactUsButton = new JButton("联系客服");
		contactUsButton.setBounds(new Rectangle(10, 370, 480, 40));
		contactUsButton.addActionListener(this);
		
		setBackground(Color.WHITE);
		add(searchPanel);
		add(submitFormButton);
		add(viewAllButton);
		add(contactUsButton);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == submitFormButton)
			campusTradeClient.showSubmitClicked();
		else if (ae.getSource() == viewAllButton)
			campusTradeClient.showSearchClicked(0, null);
		else if (ae.getSource() == contactUsButton)
			campusTradeClient.showContactClicked();
	}
}