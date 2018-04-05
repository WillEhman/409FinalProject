package frontend.components;

import java.util.ArrayList;
import javax.swing.JPanel;

public class BoxList <E> {
	private JPanel scrollBox;

	public BoxList(JPanel scrollBox) {
		this.scrollBox = scrollBox;
		// TODO: What's the difference between this and setScrollBox?
	}
	
	public <T> void addItem(T boxItem) {
		// TODO
	}
	
	public void setItems(ArrayList<?> boxItems) {
		// TODO
	}

	public JPanel getScrollBox() {
		return scrollBox;
	}

	public void setScrollBox(JPanel scrollBox) {
		this.scrollBox = scrollBox;
	}
	
}
