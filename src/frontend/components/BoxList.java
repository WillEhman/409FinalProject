package frontend.components;

import java.util.ArrayList;
import javax.swing.JPanel;

public class BoxList <E> {
	private JPanel scrollBox;

	public BoxList(JPanel scrollBox) {
		this.scrollBox = scrollBox;
	}
	
	public <T> void addItem(T boxItem) {
		
	}
	
	public void setItems(ArrayList boxItems) {
		
	}

	public JPanel getScrollBox() {
		return scrollBox;
	}

	public void setScrollBox(JPanel scrollBox) {
		this.scrollBox = scrollBox;
	}
	
}
