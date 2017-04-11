package com.pw.sag.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Agent;

public class SampleAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(SampleAgent.class);
 
    @Override
    public void setup() {
        final String otherAgentName = (String) this.getArguments()[0];
        //addBehaviour(new IncrementBaseNumber(this, otherAgentName));
        
        //CHYBA dobra strona do ogarniecia podstaw JADE i agentów
        //http://ideaheap.com/2015/05/jade-setup-for-beginners/        
    }
 
    @Override
    public void takeDown() {
    }
}