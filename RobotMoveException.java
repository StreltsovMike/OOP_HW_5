package org.example.lesson1.example;

public class RobotMoveException extends Exception {

    private final RobotMap.Robot robot;

    public RobotMoveException(String message, RobotMap.Robot robot) {
        super(message);
        this.robot = robot;
    }

    // public RobotMap.Robot getRobot() {
    //     return robot;
    // }
}
