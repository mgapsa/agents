package com.pw.sag.gui;

//bibliotego do grafiki
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayoutInfo;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.board.BoardAgent;


public class BoardGUI {
	private int multiplier = 8;
	private JFrame guiFrame;
	private static final Logger logger = LoggerFactory.getLogger(BoardGUI.class);
	private JPanel panel = new JPanel();
	private BoardAgent boardAgent;
	JPanel[][] panelHolder;
	//jakas lista z pozycjami i aktorami
	//metody do wyswietlnia
	
	public BoardGUI(BoardAgent boardAgent)
	{
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Example GUI");
		guiFrame.setSize(boardAgent.getX()*multiplier, boardAgent.getY()*multiplier);
		this.boardAgent = boardAgent;
		
		panelHolder = new JPanel[boardAgent.getX()][boardAgent.getY()];
		panel.setLayout(new GridLayout(boardAgent.getX(),boardAgent.getY(),1,1));
		
		
		int[][] board = boardAgent.getBoard();
		//narysowac przeszkody
		for(int i =0; i<boardAgent.getX(); i++)
        {
            for(int j = 0; j < boardAgent.getY(); j++)
            {
            	panelHolder[i][j] = new JPanel();
            	panel.add(panelHolder[i][j]);
                if(board[i][j] == 1)
                {
                	panelHolder[i][j].setBackground(Color.RED);
                }
            }
        }
		
		guiFrame.add(panel, BorderLayout.CENTER);
		
		
		guiFrame.setVisible(true);
	}
	
	public void diplayObstacles()
	{
		
	}
	//tutaj jakas metoda do otrzymywania nazwy actora i jego nowej pozycji od boardu
	//musi miec jakąś liste ktory gent to jaki leement zeby usuwac w starym miejscu i rysowac w nowym
}
