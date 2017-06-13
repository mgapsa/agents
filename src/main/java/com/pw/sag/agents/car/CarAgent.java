package com.pw.sag.agents.car;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.car.behaviors.AskBoard;

import jade.core.Agent;

public class CarAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(CarAgent.class);
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static final int FORWARD = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private int currentX;
    private int currentY;
    private int steps = 0;

    private int nextX;
    private int nextY;

    private String agentName;

    private int direction;
    private int nextDirection;
    private int movedSide;
	private int nextSide;
	private double[][] rewardsArray;
    private Boolean[] inaccessibleDirections;
//    private int[][] inaccessibleCellsArray;
    private Random generator;
    
 
    @Override
    public void setup() {
        final String boardName = (String) this.getArguments()[0];
        addBehaviour(new AskBoard(this, boardName));
        
        String[] parts = this.getName().split("@");
        agentName = parts[0];

        generator = new Random();
        rewardsArray = new double[4][3];
        for (int i = 0; i < rewardsArray.length; i++)
    	{
        	for (int j = 0; j < rewardsArray[i].length; j++)
        	{
        		rewardsArray[i][j] = 1;
        	}
        }
        inaccessibleDirections = new Boolean[4];
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
        direction = generator.nextInt(4);
        chooseNextPosition();


        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/
        //mvn -Pjade-main exec:java
        //mvn -Pjade-board exec:java
    }

    public void chooseNextPosition() {
		double tempValue = 0;
		boolean flag = false;
		if (rewardsArray[direction][FORWARD] > tempValue  && !inaccessibleDirections[direction]) {
			tempValue = rewardsArray[direction][FORWARD];
			nextSide = FORWARD;
			nextDirection = direction;
			flag = true;
		}
		if (rewardsArray[direction][LEFT] > tempValue && !inaccessibleDirections[(direction+3)%4]) {
			tempValue = rewardsArray[direction][LEFT];
			nextSide = LEFT;
			nextDirection = (direction+3)%4;
			flag = true;
		}
		if (rewardsArray[direction][RIGHT] > tempValue && !inaccessibleDirections[(direction+5)%4]) {
			nextSide = RIGHT;
			nextDirection = (direction+5)%4;
			flag = true;
		}


		switch (nextDirection){
			case NORTH:
				nextX = currentX-1;
				nextY = currentY;
				break;
			case EAST:
				nextX = currentX;
				nextY = currentY+1;
				break;
			case WEST:
				nextX = currentX;
				nextY = currentY-1;
				break;
			case SOUTH:
				nextX = currentX+1;
				nextY = currentY;
				break;
			default:
				break;
		}
	}


	public void setInaccessibleDirections(int ... positions){
		for (int p :positions)
		{
			inaccessibleDirections[p] = true;
		}
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

    		movedSide = nextSide;
    		steps++;
			for (int i = 0; i < inaccessibleDirections.length; i++)
			{
				inaccessibleDirections[i] = false;
			}
    		//prepareNextPosition(false);
    	}
    	else
    	{
    		//prepareNextPosition(true);
    	}
    }

    public void setReward(double reward){
		double oldReward = 0;
		if(rewardsArray[direction][movedSide]==1.0){
			rewardsArray[direction][movedSide]= 0.0;
			rewardsArray[direction][movedSide] = (rewardsArray[direction][movedSide] + reward);
			oldReward = 0;
		}
		else
		{
			oldReward = rewardsArray[direction][movedSide];
			//rewardsArray[direction][movedSide] = (rewardsArray[direction][movedSide] + Math.pow(0.9,steps)* reward)/2;
			rewardsArray[direction][movedSide] = (rewardsArray[direction][movedSide] +  reward)/2;
		}
		if(agentName.equals("car-agent-1"))
		{
			logger.info("Kierunek " + direction + " poszedłem " + movedSide);
			logger.info("Nagroda " + reward);
			logger.info("Stara nagroda tutej " + oldReward);
		}

//		rewardsArray[direction][movedSide] = (rewardsArray[direction][movedSide] + Math.pow(0.9,steps)*reward)/2;
		direction = nextDirection;
		if(agentName.equals("car-agent-1")) {
			logger.info(currentX + " " + currentY);
			for (int i = 0; i < 4; i++) {
				logger.info(Double.toString(rewardsArray[i][0]) + " " + Double.toString(rewardsArray[i][1]) + " " + Double.toString(rewardsArray[i][2]));
			}
		}

	}
    

    
//    private void prepareNextPosition(boolean forceChangeDirection)
//    {
////    	nextX = generator.nextInt(80);
////    	nextY = generator.nextInt(80);
//    	if (!forceChangeDirection)
//    	{
//    		lastChangeDirection = 0;
//        	boolean changeDirection = generator.nextInt(100) % 10 == 0;
//        	if (changeDirection)
//        	{
//        		int changeValue = generator.nextInt(2) == 0 ? 1 : -1;
//        		direction += changeValue;
//        		if (direction > 3)
//        			direction = 0;
//        		else if (direction < 0)
//        			direction = 3;
//        	}
//    	}
//    	else
//    	{
//    		if (lastChangeDirection == 0)
//    		{
//        		int changeValue = generator.nextInt(2) == 0 ? 1 : -1;
//        		direction += changeValue;
//        		lastChangeDirection = changeValue;
//        		if (direction > 3)
//        			direction = 0;
//        		else if (direction < 0)
//        			direction = 3;
//    		}
//    		else
//    		{
//        		direction += lastChangeDirection;
//        		if (direction > 3)
//        			direction = 0;
//        		else if (direction < 0)
//        			direction = 3;
//
//    		}
//    	}
//    	switch (direction)
//    	{
//    	case 0:
//    		nextX = currentX + 1;
//    		nextY = currentY;
//    		break;
//    	case 1:
//    		nextY = currentY - 1; // jesli poczatek ukladu wspolrzednych jest w lewym gornym roku
//    		nextX = currentX;
//    		break;
//    	case 2:
//    		nextX = currentX - 1;
//    		nextY = currentY;
//    		break;
//    	case 3:
//    		nextY = currentY + 1;
//    		nextX = currentX;
//    		break;
////    	default:
////    		nextX = 10 + generator.nextInt(80);
////    		nextY = 10 + generator.nextInt(80);
//    	}
//    }
 
    @Override
    public void takeDown() {
    }
}

