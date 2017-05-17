package com.pw.sag.gui;

//bibliotego do grafiki
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.board.BoardAgent;


public class BoardGUI {
	private JFrame guiFrame;
	private static final Logger logger = LoggerFactory.getLogger(BoardGUI.class);
	//jakas lista z pozycjami i aktorami
	//metody do wyswietlnia
	
	public BoardGUI(BoardAgent boardAgent)
	{
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Example GUI");
		guiFrame.setSize(1000,1000);
		guiFrame.setVisible(true);
	}
	
	public void diplayObstacles()
	{
		
	}
	//tutaj jakas metoda do otrzymywania nazwy actora i jego nowej pozycji od boardu
	//musi miec jakąś liste ktory gent to jaki leement zeby usuwac w starym miejscu i rysowac w nowym
}
