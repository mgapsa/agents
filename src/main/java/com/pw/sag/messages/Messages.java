package com.pw.sag.messages;

//chciałbym enum ale juz wysyłam stringami to podtrzymam to, moge tez wtedy wyslac wiecej inforacji jak cos
//public enum Messages { 
//	OBSTACLE, OK, FINISH;
//}

public class Messages{
	public static final String OBSTACLE_MET = "Obstacle";
	public static final String MOVE_OK = "Move accepted";
	public static final String FINISH = "You got safely to the end";
	public static final String FROM_BOARD = "From board";
	public static final String ASK_AOBUT_NEIGHBOURHOOD = "Neighbourhood question";
	public static final String MOVE_ORDER = "Move order";
	public static final String SOUTH = "South";
	public static final String NORTH = "North";
	public static final String WEST = "West";
	public static final String EAST = "East";
}
