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

    //nie wiem czy dobrze ze rzutuje na CarAgent, jak zle to implementacje x,y przeniesc tutaj i po sprawie
    //bo narazie x i y są w klasie car, dlatego rzutuje. 
    //Tutaj zaczynamy pytac, na razie pytam tylko Board, bo tym sie zajmowałem. Wysyłam nazwe samochodu, zeby board mogl
    //odpowiedziec i odpowiednie auto ustawic na boardzie oraz poloenie do jakiego chce isc
    private void startAsking() {
    	CarAgent car = (CarAgent) agent;
        agent.send(inform().toLocal(boardName).withContent(agentName + ";" + car.getCurrentX() + ";" + car.getCurrentY() + ";").build());
        state = State.CONTINUE_MOVING;
    }

    //to samo co wyzej tylko najpeir czeka na odpowiedz. Stwierdzilem ze tutaj bedzie dostawal odpowiedzi od wszystkich wiec
    //w stringu ktory dostaje niech bedzie info czy ta odpowiedz jest od auta czy od boardu
    private void continueAsking() {
        listen(agent, this).forString((information) -> {
            logger.info("Recieved " + information);
            CarAgent car = (CarAgent) agent;
            try {
				Thread.sleep(5000);
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
            		case Messages.MOVE_OK:
            			car.moved(true);
            			agent.send(inform().toLocal(boardName).withContent(agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
            			break;
            		case Messages.OBSTACLE_MET:
            			car.moved(false);
            			agent.send(inform().toLocal(boardName).withContent(agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
            			break;
            		case Messages.FINISH:
            			state = State.STOP_MOVING;
            			break;
        			default:
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