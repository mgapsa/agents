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
    
    private int direction;
    private int lastChangeDirection;
    private Random generator;
    
 
    @Override
    public void setup() {
        final String boardName = (String) this.getArguments()[0];
        addBehaviour(new AskBoard(this, boardName));
        
        generator = new Random();
        currentX = 25 + generator.nextInt(50);
        currentY = 30 + generator.nextInt(90);
        nextX = currentX;
        nextY = currentY;
        direction = generator.nextInt(4);
        prepareNextPosition(false);
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
    		prepareNextPosition(false);
    	}
    	else
    	{
    		prepareNextPosition(true);
    	}
    }
    
    private void prepareNextPosition(boolean forceChangeDirection)
    {
//    	nextX = generator.nextInt(80);
//    	nextY = generator.nextInt(80);
    	if (!forceChangeDirection)
    	{
    		lastChangeDirection = 0;
        	boolean changeDirection = generator.nextInt(100) % 10 == 0;
        	if (changeDirection)
        	{
        		int changeValue = generator.nextInt(2) == 0 ? 1 : -1;
        		direction += changeValue;
        		if (direction > 3)
        			direction = 0;
        		else if (direction < 0)
        			direction = 3;
        	}
    	}
    	else
    	{
    		if (lastChangeDirection == 0)
    		{
        		int changeValue = generator.nextInt(2) == 0 ? 1 : -1;
        		direction += changeValue;
        		lastChangeDirection = changeValue;
        		if (direction > 3)
        			direction = 0;
        		else if (direction < 0)
        			direction = 3;
    		}
    		else
    		{
        		direction += lastChangeDirection;
        		if (direction > 3)
        			direction = 0;
        		else if (direction < 0)
        			direction = 3;

    		}
    	}
    	switch (direction)
    	{
    	case 0:
    		nextX = currentX + 1;
    		nextY = currentY;
    		break;
    	case 1:
    		nextY = currentY - 1; // jesli poczatek ukladu wspolrzednych jest w lewym gornym roku
    		nextX = currentX;
    		break;
    	case 2:
    		nextX = currentX - 1;
    		nextY = currentY;
    		break;
    	case 3:
    		nextY = currentY + 1;
    		nextX = currentX;
    		break;
//    	default:
//    		nextX = 10 + generator.nextInt(80);
//    		nextY = 10 + generator.nextInt(80);
    	}
    }
 
    @Override
    public void takeDown() {
    }
}

