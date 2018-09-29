import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GoodComboBox extends JComboBox{
	private static String s[] = {"", "[Books]教材", "[Bicycle]自行车", "[Electronics]电子产品", "[Grooming]个人护理", "[Housekeeping]家居用品", "[Others]其他"};
	private static int blanks[] = {0, 22, 20, 8, 9, 3, 21};
	
	public GoodComboBox() {
		setEditable(false);
		
		addItem("请选择类别");
		addItem("[Books]教材");
		addItem("[Bicycle]自行车");
		addItem("[Electronics]电子产品");
		addItem("[Grooming]个人护理");
		addItem("[Housekeeping]家居用品");
		addItem("[Others]其他");
	}
	
	public static String getNameByIndex(int i) {
		return s[i];
	}
	
	public static int getBlanksByIndex(int i) {
		return blanks[i];
	}
}
