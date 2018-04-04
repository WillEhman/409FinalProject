package frontend.components;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class PageNavigator {
	
	private JPanel pageHolder;
	private CardLayout cardLayout;
	
	public PageNavigator(JPanel pageHolder, CardLayout cardLayout) {
		this.pageHolder = pageHolder;
		this.cardLayout = cardLayout;
	}

	public void showPage(String page) {
		
	}
	
	public void addPage(JPanel page, String name) {
		
	}
	
	public void removePage(String page) {
		
	}
	
	public JPanel searchPage(String page) {
		return null; //Set later
	}
	
	public JPanel getPageHolder() {
		return pageHolder;
	}

	public void setPageHolder(JPanel pageHolder) {
		this.pageHolder = pageHolder;
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}
	

}
