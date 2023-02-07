package org.example.lesson1.example;

public class RobotMoveException extends Exception {

    private final DefoltRobotMap.DefoltRobot robot;

    public RobotMoveException(String message, DefoltRobotMap.DefoltRobot robot) {
        super(message);
        this.robot = robot;
    }

    // public RobotMap.Robot getRobot() {
    //     return robot;
    // }
}
