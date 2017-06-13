package com.pw.sag.agents.car.behaviors;

import com.pw.sag.agents.car.CarAgent;
import com.pw.sag.messages.Messages;
import com.pw.sag.tools.ContainerKiller;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import static com.pw.sag.messages.MessageBuilder.inform;
import static com.pw.sag.messages.MessageReceiver.listen;

import java.util.ArrayList;
import java.util.List;

public class AskBoard extends Behaviour {
    private static final Logger logger = LoggerFactory.getLogger(AskBoard.class);
    private static final int MAX_INCREMENT = 10;

    private enum State {
        START_MOVING, CONTINUE_MOVING, STOP_MOVING
    }

    private final Agent agent;
    private final String boardName;
    private State state;
    private String agentName;
    private int north;
    private int south;
    private int east;
    private int west;
    private List<String> carsNames;
    private List<Boolean> carsReceived;

    public AskBoard(Agent agent, String boardName) {
        this.agent = agent;
        this.boardName = boardName;
        this.state = State.START_MOVING;
        String[] parts = agent.getName().split("@");
        agentName = parts[0];
        north = 1;
        south = 1;
        east = 1;
        west = 1;
        carsNames = new ArrayList<String>();
        carsReceived = new ArrayList<Boolean>();
        
    }

    @Override
    public void action() {
        switch (state) {
	        case START_MOVING:
	        	startAsking();
            case CONTINUE_MOVING:
                continueAsking();
                break;
            case STOP_MOVING:
                stopAsking();
                break;
            default:
                block();
        }
    }

    private void startAsking() {
    	CarAgent car = (CarAgent) agent;
        agent.send(inform().toLocal(boardName).withContent(Messages.ASK_AOBUT_NEIGHBOURHOOD + ";" + agentName + ";" + car.getCurrentX() + ";" + car.getCurrentY() + ";").build());
    	//agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getCurrentX() + ";" + car.getCurrentY() + ";").build());
        state = State.CONTINUE_MOVING;
    }

    private void continueAsking() {
        listen(agent, this).forString((information) -> {
            logger.info("Recieved " + information);
            CarAgent car = (CarAgent) agent;
            
            String[] parts = information.split(";");
            
           
            if(parts[0].equals(Messages.FROM_BOARD))
            {
            	//logger.info("OK " + parts[1]);
                switch(parts[1])
                {
                case Messages.MOVE_ORDER:
                	//tutaj co robicie gdy dostaniecie odpowedz od planszy o ruchu
                	switch(parts[2])
                	{
                		case Messages.MOVE_OK:
                			//to ponizej wywaliłem BO teraz to jak sie ruszy nie jest randomowe a zalezy od sąsiedztwa i od tablicy
                			//wiec to poszlo tam gdzie mamy odpiwiedz od sasiedztwa
                			//car.moved(true);
                			//ruch się udał, wiec mamy nagrode
                			//idzie pytanie o sąsiedztwo do planszy
                			double reward = Double.parseDouble(parts[3]);
                			car.setReward(reward);
				            try {
								Thread.sleep(100);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                			agent.send(inform().toLocal(boardName).withContent(Messages.ASK_AOBUT_NEIGHBOURHOOD + ";" + agentName + ";" + car.getCurrentX() + ";" + car.getCurrentY()).build());
                			break;
                		/*case Messages.OBSTACLE_MET:
                			//ten przypadek juz nie wsytąpi teraz, jak zrobicie algorytm poruszania
                			car.moved(false);
                			agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                			break;
                		case Messages.FINISH:
                			state = State.STOP_MOVING;
                			break;*/
            			default:
            				break;            		
                	}
                	break;
                case Messages.ASK_AOBUT_NEIGHBOURHOOD:
                	//tutaj co robicie gdy dostaniecie odpowiedz o sasiedztwie
                	//0 to normalne pole, 1 to przeszkoda
                	west = Integer.parseInt(parts[2]);
                	south = Integer.parseInt(parts[3]);
                	east = Integer.parseInt(parts[4]);
                	north = Integer.parseInt(parts[5]);
                	//jakies przetwarzanie i na koniec idzie pytanie o ruch do samochodu. 
                	//YOURCODE HERE xD
					if (west==1){
						car.setInaccessibleDirections(CarAgent.WEST);
					}
					if (south==1){
						car.setInaccessibleDirections(CarAgent.SOUTH);
					}
					if (east == 1){
						car.setInaccessibleDirections(CarAgent.EAST);
					}
					if (north == 1){
						car.setInaccessibleDirections(CarAgent.NORTH);
					}

                	agent.send(inform().toLocal(boardName).withContent(Messages.ASK_FOR_ALL_CARS + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                	
                	//car.moved(true);
                	//agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                	break;
                case Messages.ASK_FOR_ALL_CARS:
                	int carsCount = Integer.parseInt(parts[2]);
                	List<String> carsNamesLocal = new ArrayList<String>();
                	carsReceived.clear();
                	for (int i = 0; i < carsCount; i++)
                	{
                		carsNamesLocal.add(parts[3 + i]);
                		carsReceived.add(false);
                	}
                	carsNames = carsNamesLocal;
                	for (String carName : carsNames)
                	{
                		if (!carName.equals(agentName))
                			agent.send(inform().toLocal(carName).withContent(Messages.FROM_CAR + ";" + Messages.ASK_FOR_BLOKED_LOCATIONS + ";" + agentName).build());
                	}
                	break;
                }
            }
            else if(parts[0].equals(Messages.FROM_CAR))
            {
                switch(parts[1])
                {
                case Messages.ASK_FOR_BLOKED_LOCATIONS:
                	agent.send(inform().toLocal(parts[2]).withContent(Messages.FROM_CAR + ";" + Messages.ANSWER_BLOKED_LOCATIONS + ";" + agentName
                			+ ";" + car.getCurrentX() + ";" + car.getCurrentY() + ";" + car.getNextX() + ";" + car.getNextY()).build());
                	break;
                case Messages.ANSWER_BLOKED_LOCATIONS:
                	int index = carsNames.indexOf(parts[2]);
                	if (index >= 0)
                		carsReceived.set(index, true);
                	int curX = Integer.parseInt(parts[3]);
                	int curY = Integer.parseInt(parts[4]);
                	int nextX = Integer.parseInt(parts[5]);
                	int nextY = Integer.parseInt(parts[6]);
                	if (curX == car.getCurrentX() - 1 && curY == car.getCurrentY())
                		west = 1;
                	else if (curX == car.getCurrentX() && curY == car.getCurrentY() + 1)
                		south = 1;
                	else if (curX == car.getCurrentX() + 1 && curY == car.getCurrentY())
                		east = 1;
                	else if (curX == car.getCurrentX() && curY == car.getCurrentY() - 1)
                		north = 1;
                	if (nextX == car.getCurrentX() - 1 && nextY == car.getCurrentY())
                		west = 1;
                	else if (nextX == car.getCurrentX() && nextY == car.getCurrentY() + 1)
                		south = 1;
                	else if (nextX == car.getCurrentX() + 1 && nextY == car.getCurrentY())
                		east = 1;
                	else if (nextX == car.getCurrentX() && nextY == car.getCurrentY() - 1)
                		north = 1;
                	int carsNotReceivedCounter = 0;
                	for (Boolean carReceived : carsReceived)
                	{
                		if (carReceived == false )
                		{
                			carsNotReceivedCounter++;
                		}
                	}
                	if (carsNotReceivedCounter <= 1)
                	{
//                		car.prepareNextPosition2(north, east, south, west, false);
						if (west==1){
							car.setInaccessibleDirections(CarAgent.WEST);
						}
						if (south==1){
							car.setInaccessibleDirections(CarAgent.SOUTH);
						}
						if (east == 1){
							car.setInaccessibleDirections(CarAgent.EAST);
						}
						if (north == 1){
							car.setInaccessibleDirections(CarAgent.NORTH);
						}
						car.chooseNextPosition();
                		car.moved(true);
                    	agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                	}
                	
                	break;
                }
            }
        });
    }

    // to sie teraz nie odpala BO on tu nasluchuje, nic nie dostaje wiec nie niszczy i reszta sie moze dokrecic
    private void stopAsking() {
        listen(agent, this).forString((information) -> {
            logger.info("I'm just going to ignore this: " + information);
            ContainerKiller.killContainerOf(agent);
        });
    }

    @Override
    public boolean done() {
        return false;
    }
}