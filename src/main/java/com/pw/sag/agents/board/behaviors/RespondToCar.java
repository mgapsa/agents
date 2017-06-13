package com.pw.sag.agents.board.behaviors;

import com.pw.sag.agents.board.BoardAgent;
import com.pw.sag.gui.BoardGUI;
import com.pw.sag.tools.ContainerKiller;
import com.pw.sag.utilities.Point;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import static com.pw.sag.messages.MessageBuilder.inform;
import static com.pw.sag.messages.MessageReceiver.listen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pw.sag.messages.Messages;

//tutaj rowniez odwoluje sie do BoardAgent wiedziec ze tylko to z tego korzysta!
//jak to zla koncepcja, to mozna zmienic ze tutaj jest plansza a nie w BoardAgent
public class RespondToCar extends Behaviour {
    private static final Logger logger = LoggerFactory.getLogger(RespondToCar.class);
    private static final int MAX_INCREMENT = 10;
    private Map<String, Double> map = new HashMap<String, Double>();

    private enum State {
        CONTINUE_RESPONDING, STOP_RESPONDING
    }

    private final Agent agent;
    private State state;
    private BoardGUI gui;
    private List<String> carsNames;

    public RespondToCar(Agent agent) {
        this.agent = agent;
        this.state = State.CONTINUE_RESPONDING;
        BoardAgent boardAgent = (BoardAgent) agent;
        carsNames = new ArrayList<String>();
        
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
            //logger.info("Recieved " + information);
            //common variables
            String[] parts = information.split(";");
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            BoardAgent boardAgent = (BoardAgent) agent;
            if (!carsNames.contains(parts[1]))
            {
            	carsNames.add(parts[1]);
            }
            switch(parts[0])
            {
            case Messages.ASK_AOBUT_NEIGHBOURHOOD:
            	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.ASK_AOBUT_NEIGHBOURHOOD + ";" + boardAgent.getBoard()[x-1][y] + ";" + boardAgent.getBoard()[x][y+1] + ";" + boardAgent.getBoard()[x+1][y] + ";"+ boardAgent.getBoard()[x][y-1] + ";").build());
            	break;
            case Messages.MOVE_ORDER:
                //ten to ylko upewnienie ze nie wjedzie na przeszkode
//                if(boardAgent.getBoard()[x][y]==1)
//                	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.MOVE_ORDER + ";" + Messages.OBSTACLE_MET).build());
//                else
                {
                	double reward = boardAgent.getWeights()[x][y];
                	double oldReward = 0;
                    if(map.containsKey(parts[1]))
                    {
                        oldReward = map.get(parts[1]);
                        map.replace(parts[1],reward);
                    }
                    else
                    {
                        map.put(parts[1], reward);
                    }
                    reward = reward - oldReward;
                	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.MOVE_ORDER + ";" + Messages.MOVE_OK + ";" + reward).build());
                	gui.displayCar(parts[1], x, y);
                }
            	break;
            case Messages.ASK_FOR_ALL_CARS:
            	String messageText = Integer.toString(carsNames.size());
            	for (int i = 0; i < carsNames.size(); i++) 
            	{
            		messageText += (";" + carsNames.get(i));
            	}
            	agent.send(inform().toLocal(parts[1]).withContent(Messages.FROM_BOARD + ";" + Messages.ASK_FOR_ALL_CARS + ";" + messageText).build());
            	break;
            }
        });
    }

    private void stopResponding() {
        listen(agent, this).forInteger((toIgnore) -> {
            //logger.info("I'm just going to ignore this: " + toIgnore);
            ContainerKiller.killContainerOf(agent);
        });
    }

    @Override
    public boolean done() {
        return false;
    }
}