package com.pw.sag.agents.board.behaviors;

import com.pw.sag.agents.board.BoardAgent;
import com.pw.sag.gui.BoardGUI;
import com.pw.sag.tools.ContainerKiller;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import static com.pw.sag.messages.MessageBuilder.inform;
import static com.pw.sag.messages.MessageReceiver.listen;
import com.pw.sag.messages.Messages;

//tutaj rowniez odwoluje sie do BoardAgent wiedziec ze tylko to z tego korzysta!
//jak to zla koncepcja, to mozna zmienic ze tutaj jest plansza a nie w BoardAgent
public class RespondToCar extends Behaviour {
    private static final Logger logger = LoggerFactory.getLogger(RespondToCar.class);
    private static final int MAX_INCREMENT = 10;

    private enum State {
        CONTINUE_RESPONDING, STOP_RESPONDING
    }

    private final Agent agent;
    private State state;
    private BoardGUI gui;

    public RespondToCar(Agent agent) {
        this.agent = agent;
        this.state = State.CONTINUE_RESPONDING;
        BoardAgent boardAgent = (BoardAgent) agent;
        
        gui = new BoardGUI(boardAgent);
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

    //Sprawdzac obstacles, CEL? Zając sie GUI
    private void continueResponding() {
        listen(agent, this).forString((information) -> {
            logger.info("Recieved " + information);
            //common variables
            String[] parts = information.split(";");
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            BoardAgent boardAgent = (BoardAgent) agent;
            
            switch(parts[0])
            {
            case Messages.ASK_AOBUT_NEIGHBOURHOOD:
            	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.ASK_AOBUT_NEIGHBOURHOOD + ";" + boardAgent.getBoard()[x-1][y] + ";" + boardAgent.getBoard()[x][y+1] + ";" + boardAgent.getBoard()[x+1][y] + ";"+ boardAgent.getBoard()[x][y-1] + ";").build());
            	break;
            case Messages.MOVE_ORDER:
                //ten to ylko upewnienie ze nie wjedzie na przeszkode
                if(boardAgent.getBoard()[x][y]==1)
                	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.MOVE_ORDER + ";" + Messages.OBSTACLE_MET + ";").build());
                else
                {
                	int reward = boardAgent.getX() + boardAgent.getY() - x - y;
                	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.MOVE_ORDER + ";" + Messages.MOVE_OK + ";" + reward + ";").build());
                	gui.displayCar(parts[1], x, y);
                }
            	break;
            }
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