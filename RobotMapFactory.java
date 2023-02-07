package org.example.lesson1.example;

public class RobotMapFactory {
    

    public DefoltRobotMap create(int n, int m) throws RobotMapCreationException {
        return new DefoltRobotMap(n, m);
    }

}

