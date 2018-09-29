import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SearchPanel extends JPanel implements ActionListener {
	private JLabel searchPrompt = new JLabel("Search:");
	private JTextField keywordField = new JTextField(null);
	private GoodComboBox comboBox = new GoodComboBox();
	private JButton searchButton = new JButton("Go");
	
	private CampusTradeClient campusTradeClient;
	
	SearchPanel(CampusTradeClient ct) {
		campusTradeClient = ct;
		
		setBackground(Color.WHITE);
		keywordField.setColumns(10);
		keywordField.addActionListener(this);
		searchButton.addActionListener(this);
		
		add(searchPrompt);
		add(keywordField);
		add(comboBox);
		add(searchButton);
	}
	
	public void actionPerformed(ActionEvent ae) {
		campusTradeClient.showSearchClicked(comboBox.getSelectedIndex(), keywordField.getText());
	}
}
