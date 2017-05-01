package com.pw.sag.agents.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pw.sag.agents.board.behaviors.RespondToCar;

import jade.core.Agent;

public class BoardAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(BoardAgent.class);
 
    @Override
    public void setup() {
        addBehaviour(new RespondToCar(this));
        
        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/
        //mvn -Pjade-main exec:java
        //mvn -Pjade-agent exec:java
    }
 
    @Override
    public void takeDown() {
    }
}
