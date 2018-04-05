package frontend.components;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListListener implements ListSelectionListener {
	/**
	 * The display containing the list
	 */
	PageNavigator display;

	public ListListener(PageNavigator disp) {
		display = disp;
	}

	public void valueChanged(ListSelectionEvent e) {
//		String selected = display.dispr.getSelectedValue();
//		if (selected != null) {
//			String[] ds = selected.split("\\s+");
//			PreparedStatement state = null;
//			try {
//				state = myConn.prepareStatement("SELECT * FROM client WHERE id = ?");
//				state.setInt(1, Integer.parseInt(ds[0]));
//				ResultSet rset = state.executeQuery();
//				while (rset.next()) {
//					display.cid.setText(rset.getString("ID"));
//					display.fname.setText(rset.getString("firstname"));
//					display.lname.setText(rset.getString("lastname"));
//					display.address.setText(rset.getString("address"));
//					display.pcode.setText(rset.getString("postalCode"));
//					display.pnum.setText(rset.getString("phoneNumber"));
//					if (rset.getString("clientType").equals("R")) {
//						ctype.setSelectedIndex(0);
//					} else {
//						ctype.setSelectedIndex(1);
//					}
//				}
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//			} catch (NumberFormatException ex) {
//				ex.printStackTrace();
//			}
//		}
	}
}