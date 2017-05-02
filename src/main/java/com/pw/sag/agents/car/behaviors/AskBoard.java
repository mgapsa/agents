package com.pw.sag.agents.car.behaviors;

import com.pw.sag.agents.car.CarAgent;
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
    private void startAsking() {
    	CarAgent car = (CarAgent) agent;
        agent.send(inform().toLocal(boardName).withContent(agentName + ";" + car.getCurrentX() + ";" + car.getCurrentY() + ";").build());
        state = State.CONTINUE_MOVING;
    }

    //zamiast integra wsyylac stringa nazwaagetna;x;y; czyli gdzie cche sie udac. Jak zmieni pozycje to zwroci stringa true albo false
    private void continueAsking() {
        listen(agent, this).forInteger((toIncrement) -> {
            logger.info("Recieved " + toIncrement);
            CarAgent car = (CarAgent) agent;
            car.moved(true);
            toIncrement++;
            agent.send(inform().toLocal(boardName).withContent(agentName + ";" + car.getNextX() + ";" + car.getNextY()).build());
            if (toIncrement > MAX_INCREMENT) {
                state = State.STOP_MOVING;
            }
        });
    }

    private void stopAsking() {
        listen(agent, this).forInteger((toIgnore) -> {
            logger.info("I'm just going to ignore this: " + toIgnore);
            ContainerKiller.killContainerOf(agent);
        });
    }

    @Override
    public boolean done() {
        return false;
    }
}