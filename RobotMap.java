package org.example.lesson1.example;

import java.util.*;

import org.example.lesson1.example.DefoltRobotMap.DefoltRobot;

interface RobotMap  {
    public Robot createRobot(Point point) throws RobotCreationException ; 
    public List<DefoltRobot> getRobotList();
    

}
