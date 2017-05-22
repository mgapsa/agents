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
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.board.BoardAgent;
import com.pw.sag.utilities.Point;


public class BoardGUI {
	private int multiplier = 8;
	private JFrame guiFrame;
	private static final Logger logger = LoggerFactory.getLogger(BoardGUI.class);
	private JPanel panel = new JPanel();
	private BoardAgent boardAgent;
	JPanel[][] panelHolder;
	
	private Map<String, Point> map = new HashMap<String, Point>();
	//jakas lista z pozycjami i aktorami
	//metody do wyswietlnia
	
	public BoardGUI(BoardAgent boardAgent)
	{
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Example GUI");
		guiFrame.setSize(boardAgent.getY()*multiplier, boardAgent.getX()*multiplier);
		this.boardAgent = boardAgent;
		
		panelHolder = new JPanel[boardAgent.getX()][boardAgent.getY()];
		panel.setLayout(new GridLayout(boardAgent.getX(),boardAgent.getY(),1,1));
		panel.setBackground(Color.WHITE);
		
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
                else
                {
                	panelHolder[i][j].setBackground(Color.WHITE);
                }
            }
        }
		
		guiFrame.add(panel, BorderLayout.CENTER);
		
		
		guiFrame.setVisible(true);
	}
	
	public void displayCar(String name, int x, int y)
	{
		Point p = new Point(0,0);
		if(map.containsKey(name))
		{
			p = map.get(name);
			panelHolder[p.getX()][p.getY()].setBackground(Color.WHITE);
		}
		else
		{
			map.put(name, p);
		}
		p.setX(x);
		p.setY(y);
		
		panelHolder[x][y].setBackground(Color.BLACK);
	}
	//tutaj jakas metoda do otrzymywania nazwy actora i jego nowej pozycji od boardu
	//musi miec jakąś liste ktory gent to jaki leement zeby usuwac w starym miejscu i rysowac w nowym
}
