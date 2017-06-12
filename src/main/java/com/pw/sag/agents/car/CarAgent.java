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
    private int previousDirection;
    private int[][] rewardsArray;
    // rewardsArray[E, N, W, S][L, F, R]
    private Boolean[] inaccessibleDirections;
//    private int[][] inaccessibleCellsArray;
    private int lastChangeDirection;
    private Random generator;
    
 
    @Override
    public void setup() {
        final String boardName = (String) this.getArguments()[0];
        addBehaviour(new AskBoard(this, boardName));
        
        generator = new Random();
        rewardsArray = new int[4][3];
        for (int i = 0; i < rewardsArray.length; i++)
    	{
        	for (int j = 0; j < rewardsArray[i].length; j++)
        	{
        		rewardsArray[i][j] = 100;
        	}
        }
        inaccessibleDirections = new Boolean[3];
        for (int i = 0; i < inaccessibleDirections.length; i++)
        {
        	inaccessibleDirections[i] = false;
        }
//        inaccessibleCellsArray = new int[4][3];
//        for (int i = 0; i < inaccessibleCellsArray.length; i++)
//    	{
//        	for (int j = 0; j < inaccessibleCellsArray[i].length; j++)
//        	{
//        		inaccessibleCellsArray[i][j] = 100;
//        	}
//        }
        currentX = 25 + generator.nextInt(50);
        currentY = 30 + generator.nextInt(90);
        nextX = currentX;
        nextY = currentY;
        direction = generator.nextInt(4);
        previousDirection = direction;
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
    		//prepareNextPosition(false);
    	}
    	else
    	{
    		//prepareNextPosition(true);
    	}
    }
    
    public void claimReward(int reward)
    {
    	switch (direction)
    	{
    	case 0:
    		switch (previousDirection)
    		{
    		case 0:
    			rewardsArray[previousDirection][1] += reward;
    			break;
    		case 1:
    			rewardsArray[previousDirection][2] += reward;
    			break;
    		case 3:
    			rewardsArray[previousDirection][0] += reward;
    			break;
    		}
    		break;
    	case 1:
    		switch (previousDirection)
    		{
    		case 0:
    			rewardsArray[previousDirection][0] += reward;
    			break;
    		case 1:
    			rewardsArray[previousDirection][1] += reward;
    			break;
    		case 2:
    			rewardsArray[previousDirection][2] += reward;
    			break;
    		}
    		break;
    	case 2:
    		switch (previousDirection)
    		{
    		case 1:
    			rewardsArray[previousDirection][0] += reward;
    			break;
    		case 2:
    			rewardsArray[previousDirection][1] += reward;
    			break;
    		case 3:
    			rewardsArray[previousDirection][2] += reward;
    			break;
    		}
    		break;
    	case 3:
    		switch (previousDirection)
    		{
    		case 0:
    			rewardsArray[previousDirection][2] += reward;
    			break;
    		case 2:
    			rewardsArray[previousDirection][0] += reward;
    			break;
    		case 3:
    			rewardsArray[previousDirection][1] += reward;
    			break;
    		}
    		break;
    	}
    }
    
    public void prepareNextPosition2(int north, int east, int south, int west,
    		boolean forceChangeDirection)
    {
    	if (!forceChangeDirection)
    	{
    		lastChangeDirection = 0;
    		previousDirection = direction;
    		switch (direction)
    		{
    		case 0:
    			if (north == 1)
    				inaccessibleDirections[0] = true;
    			if (east == 1)
    				inaccessibleDirections[1] = true;
    			if (south == 1)
    				inaccessibleDirections[2] = true;
    			break;
    		case 1:
    			if (west == 1)
    				inaccessibleDirections[0] = true;
    			if (north == 1)
    				inaccessibleDirections[1] = true;
    			if (east == 1)
    				inaccessibleDirections[2] = true;
    			break;
    		case 2:
    			if (south == 1)
    				inaccessibleDirections[0] = true;
    			if (west == 1)
    				inaccessibleDirections[1] = true;
    			if (north == 1)
    				inaccessibleDirections[2] = true;
    			break;
    		case 3:
    			if (east == 1)
    				inaccessibleDirections[0] = true;
    			if (south == 1)
    				inaccessibleDirections[1] = true;
    			if (west == 1)
    				inaccessibleDirections[2] = true;
    			break;
    		}
			int left = rewardsArray[direction][0];
			int forward = rewardsArray[direction][1];
			int right = rewardsArray[direction][2];
			if (inaccessibleDirections[0] && inaccessibleDirections[1] && inaccessibleDirections[2])
			{
				if (direction < 2)
					direction += 2;
				if (direction > 1)
					direction -= 2;
			}
			else
			{
				int randomInt = generator.nextInt(left + forward + right);
				while (randomInt >= 0 && randomInt <= left - 1 && inaccessibleDirections[0] ||
						randomInt >= left && randomInt <= left + forward - 1 && inaccessibleDirections[1] ||
						randomInt >= left + forward && randomInt <= left + forward + right - 1 && inaccessibleDirections[2])
				{
					randomInt = generator.nextInt(left + forward + right);
				}
				if (randomInt < left)
				{
					direction++;
					if (direction > 3)
						direction = 0;
				}
				else if (randomInt < left + forward)
				{
					// nie rób nic
				}
				else if (randomInt < left + forward + right)
				{
					direction--;
					if (direction < 0)
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
	    	}
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

