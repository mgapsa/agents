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

    public AskBoard(Agent agent, String boardName) {
        this.agent = agent;
        this.boardName = boardName;
        this.state = State.START_MOVING;
        String[] parts = agent.getName().split("@");
        agentName = parts[0];
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
            try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
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
                			int reward = Integer.parseInt(parts[3]);
                			agent.send(inform().toLocal(boardName).withContent(Messages.ASK_AOBUT_NEIGHBOURHOOD + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                			break;
                		case Messages.OBSTACLE_MET:
                			//ten przypadek juz nie wsytąpi teraz, jak zrobicie algorytm poruszania
                			car.moved(false);
                			agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
                			break;
                		case Messages.FINISH:
                			state = State.STOP_MOVING;
                			break;
            			default:
            				break;            		
                	}
                	break;
                case Messages.ASK_AOBUT_NEIGHBOURHOOD:
                	//tutaj co robicie gdy dostaniecie odpowiedz o sasiedztwie
                	//0 to normalne pole, 1 to przeszkoda
                	int north = Integer.parseInt(parts[2]);
                	int east = Integer.parseInt(parts[3]);
                	int south = Integer.parseInt(parts[4]);
                	int west = Integer.parseInt(parts[5]);
                	//jakies przetwarzanie i na koniec idzie pytanie o ruch do samochodu. 
                	//YOURCODE HERE xD
                	car.moved(true);
                	agent.send(inform().toLocal(boardName).withContent(Messages.MOVE_ORDER + ";" + agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
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