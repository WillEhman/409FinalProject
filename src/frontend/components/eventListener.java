package frontend.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class eventListener implements ActionListener {
	/**
	 * The display containing the buttons
	 */
	PageNavigator display;

	public eventListener(PageNavigator disp) {
		display = disp;
	}

	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == b1) {
//			display.search();
//		} else if (e.getSource() == b2) {
//			display.searchClear();
//		} else if (e.getSource() == b3) {
//			display.addNew();
//		} else if (e.getSource() == b4) {
//			display.save();
//		} else if (e.getSource() == b5) {
//			display.delete();
//		} else if (e.getSource() == b6) {
//			display.clear();
//		}
	}
}
