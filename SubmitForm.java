import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

class SubmitForm extends JPanel implements ActionListener {
	private GoodComboBox comboBox = new GoodComboBox();
	private int type = -1;
	
	private JLabel goodNamePrompt = new JLabel("商品名称：");
	private JTextField goodNameField = new JTextField();
	private String goodName = "";
	
	private JLabel pricePrompt = new JLabel("商品价格：");
	private JTextField priceField = new JTextField();
	private String price = "";
	
	private JLabel ownerNamePrompt = new JLabel("姓名：");
	private JTextField ownerNameField = new JTextField();
	private String ownerName = "";
	
	private JLabel ownerIDPrompt = new JLabel("学号：");
	private JTextField ownerIDField = new JTextField();
	private String ownerID = "";
	
	private JLabel ownerPhonePrompt = new JLabel("联系方式：");
	private JTextField ownerPhoneField = new JTextField();
	private String ownerPhone = "";
	
	private JButton submitButton = new JButton("提交");
	private JLabel submitResult = new JLabel("");
	
	private CampusTradeClient campusTradeClient;

	public SubmitForm(CampusTradeClient ct) {
		campusTradeClient = ct;
		
		ownerPhoneField.addActionListener(this);
		submitButton.addActionListener(this);
		
		setBackground(Color.WHITE);
		setLayout(new GridLayout(13, 1));
		add(comboBox);
		add(goodNamePrompt);
		add(goodNameField);
		add(pricePrompt);
		add(priceField);
		add(ownerNamePrompt);
		add(ownerNameField);
		add(ownerIDPrompt);
		add(ownerIDField);
		add(ownerPhonePrompt);
		add(ownerPhoneField);
		add(submitButton);
		add(submitResult);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == submitButton || ae.getSource() == ownerPhoneField)
			submitActionPerformed();
	}
	
	private void submitActionPerformed() {
		boolean filled = true;
		if ((type = comboBox.getSelectedIndex()) == 0 || (type = comboBox.getSelectedIndex()) == -1) {
			comboBox.setForeground(Color.RED);
			filled = false;
		}
		else
			comboBox.setForeground(Color.BLACK);
		if ((goodName = goodNameField.getText()).isEmpty()) {
			goodNamePrompt.setForeground(Color.RED);
			filled = false;
		}
		else
			goodNamePrompt.setForeground(Color.BLACK);
		if ((price = priceField.getText()).isEmpty()) {
			pricePrompt.setForeground(Color.RED);
			filled = false;
		}
		else
			pricePrompt.setForeground(Color.BLACK);
		if ((ownerName = ownerNameField.getText()).isEmpty()) {
			ownerNamePrompt.setForeground(Color.RED);
			filled = false;
		}
		else
			ownerNamePrompt.setForeground(Color.BLACK);
		if ((ownerID = ownerIDField.getText()).isEmpty()) {
			ownerIDPrompt.setForeground(Color.RED);
			filled = false;
		}
		else
			ownerIDPrompt.setForeground(Color.BLACK);
		if ((ownerPhone = ownerPhoneField.getText()).isEmpty()) {
			ownerPhonePrompt.setForeground(Color.RED);
			filled = false;
		}
		else
			ownerPhonePrompt.setForeground(Color.BLACK);
		if (filled == false) {
			submitResult.setForeground(Color.RED);
			submitResult.setText("请完整填写信息");
		}
		else {
			boolean success = true;
			if (!campusTradeClient.sendMsg("#SUBMIT"))
				success = false;
			if (!campusTradeClient.sendMsg(type + ""))
				success = false;
			if (!campusTradeClient.sendMsg(goodName))
				success = false;
			if (!campusTradeClient.sendMsg(price))
				success = false;
			if (!campusTradeClient.sendMsg(ownerName))
				success = false;
			if (!campusTradeClient.sendMsg(ownerID))
				success = false;
			if (!campusTradeClient.sendMsg(ownerPhone))
				success = false;
			if (success) {
				submitResult.setForeground(Color.BLUE);
				submitResult.setText("提交成功");
			}
			else {
				submitResult.setForeground(Color.RED);
				submitResult.setText("提交失败");
			}
		}
	}
}