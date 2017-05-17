package com.pw.sag.agents.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.board.behaviors.RespondToCar;

import jade.core.Agent;

public class BoardAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(BoardAgent.class);
    
    //jedynmka to sciana
    //TODO:
    //ja -> napisac metdoe w gui ktora pozwoli zmieniac pozycje samochodow graficznie no i ogolnie wysweitlanie zrobic bo to dummy teraz jest
//    private int[][] board = new int[][]{
//    	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
//    	  { 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
//    	  { 1, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 1, 1, 1, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//    	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//    	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
//    	};
    private int x,y;
    private int[][] board;
 
    @Override
    public void setup() {
    	//rozmiar wejsiowy, wygenerowac board
    	this.x = Integer.parseInt((String) this.getArguments()[0]); 
    	this.y = Integer.parseInt((String) this.getArguments()[1]);
    	createBoard();
    	
        addBehaviour(new RespondToCar(this));
        
        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/
        //mvn -Pjade-main exec:java
        //mvn -Pjade-agent exec:java
    }
    
    private void createBoard()
    {
    	board = new int[x][y];
    	for(int i = 0; i < x; i++)
    	{
    		for(int j = 0; j < y; j++)
    		{
    			if(i==0 || j==0 || i== (x-1) || j == (y-1))
    			{
    				board[i][j] = 1;
    			}
    			else
    			{
    				board[i][j] = 0;
    			}
    		}
    	}
    	
    }
    
    public int[][] getBoard()
    {
    	return board;
    }
    
    public boolean canMove(int x, int y)
    {
    	return(board[x][y] == 0);
    }
    
    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
 
    @Override
    public void takeDown() {
    }
}
