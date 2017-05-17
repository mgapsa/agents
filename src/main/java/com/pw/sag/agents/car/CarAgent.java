package com.pw.sag.agents.car;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.car.behaviors.AskBoard;

import jade.core.Agent;

public class CarAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(CarAgent.class);
    private int currentX;
    private int currentY;
    
    private int nextX;
    private int nextY;
    
 
    @Override
    public void setup() {
        final String boardName = (String) this.getArguments()[0];
        addBehaviour(new AskBoard(this, boardName));
        
        currentX = 0;
        currentY = 0;
        prepareNextPosition();
        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/
        //mvn -Pjade-main exec:java
        //mvn -Pjade-board exec:java
    }
    
    public int getCurrentX()
    {
    	return currentX;
    }
    
    public int getCurrentY()
    {
    	return currentY;
    }
    
    public int getNextX()
    {
    	return nextX;
    }
    
    public int getNextY()
    {
    	return nextY;
    }
    
    public void moved(boolean success)
    {
    	if (success)
    	{
    		currentX = nextX;
    		currentY = nextY;
    		prepareNextPosition();
    	}
    	else
    	{
    		prepareNextPosition();
    	}
    }
    
    private void prepareNextPosition()
    {
    	Random generator = new Random();
    	nextX = generator.nextInt(10);
    	nextY = generator.nextInt(10);
    }
 
    @Override
    public void takeDown() {
    }
}

