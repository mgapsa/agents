package com.pw.sag.messages;

//chciałbym enum ale juz wysyłam stringami to podtrzymam to, moge tez wtedy wyslac wiecej inforacji jak cos
//public enum Messages { 
//	OBSTACLE, OK, FINISH;
//}

public class Messages{
	public static final String OBSTACLE_MET = "Move not possible, obstacle met";
	public static final String MOVE_OK = "Move accepted";
	public static final String FINISH = "You got safely to the end";
	public static final String FROM_BOARD = "This message was sent from board";
}
