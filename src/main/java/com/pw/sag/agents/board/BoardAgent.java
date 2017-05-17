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
    private int[][] board = new int[][]{
    	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    	  { 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
    	  { 1, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
    	  { 1, 0, 0, 1, 1, 1, 0, 0, 0, 1 },
    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
    	  { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
    	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
    	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
    	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
    	};
 
    @Override
    public void setup() {
        addBehaviour(new RespondToCar(this));
        
        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/
        //mvn -Pjade-main exec:java
        //mvn -Pjade-agent exec:java
    }
    
    public int[][] getBoard()
    {
    	return board;
    }
    
    public boolean canMove(int x, int y)
    {
    	return(board[x][y] == 0);
    }
 
    @Override
    public void takeDown() {
    }
}
