package com.pw.sag.agents.board.behaviors;

import com.pw.sag.tools.ContainerKiller;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import static com.pw.sag.messages.MessageBuilder.inform;
import static com.pw.sag.messages.MessageReceiver.listen;

public class RespondToCar extends Behaviour {
    private static final Logger logger = LoggerFactory.getLogger(RespondToCar.class);
    private static final int MAX_INCREMENT = 10;

    private enum State {
        CONTINUE_RESPONDING, STOP_RESPONDING
    }

    private final Agent agent;
    private State state;
    //jakas plansza by sie przydała, array

    public RespondToCar(Agent agent) {
        this.agent = agent;
        this.state = State.CONTINUE_RESPONDING;
    }

    @Override
    public void action() {
        switch (state) {
	        case CONTINUE_RESPONDING:
	        	continueResponding();
	        	break;
            case STOP_RESPONDING:
                stopResponding();
                break;
            default:
                block();
        }
    }

    private void continueResponding() {
        listen(agent, this).forString((information) -> {
            logger.info("Recieved " + information);
            String[] parts = information.split(";");
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            if(x==5)
            	agent.send(inform().toLocal(parts[0]).withContent(20).build());
            else
            	agent.send(inform().toLocal(parts[0]).withContent(9).build());
        });
    }

    private void stopResponding() {
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