package org.example.lesson1.example;

import java.util.*;

public class DefoltRobotMap implements RobotMap {

    private final int n;
    private final int m;
    private final List<DefoltRobot> robots;

    public DefoltRobotMap(int n, int m) throws RobotMapCreationException {
        if (n < 0 || m < 0) {
            throw new RobotMapCreationException("Некоректный размер карты");
        }

        this.n = n;
        this.m = m;
        this.robots = new ArrayList<>();
    }

    // public Robot createRobot(Point point) throws RobotCreationException {
    //     final MapPoint robotPosition;
    //     try {
    //         validatePoint(point);
    //         robotPosition = new MapPoint(point.getX(), point.getY());
    //     } catch (PointValidationException e) {
    //         throw new RobotCreationException(e.getMessage());
    //     }

    //     Robot robot = new Robot(robotPosition);
    //     robots.add(robot);
    //     return robot;
    // }

    @Override
    public List<DefoltRobot> getRobotList(){
        return (robots);
    }



    private void validatePoint(Point point) throws PointValidationException {
        validatePointIsFree(point);
    }

    private void validatePointIsFree(Point point) throws PointValidationException {
        for (DefoltRobot robot : robots) {
            if (point.equals(robot.getPoint())) {
                throw new PointValidationException("Позиция " + point + " занята другим роботом: " + robot);
            }
        }
    }

    public class DefoltRobot implements Robot{

        public static final Direction DEFAULT_DIRECTION = Direction.TOP;

        private static Long idSequence = 1L;

        private final Long id;
        private MapPoint point;
        private Direction direction;

        public DefoltRobot(MapPoint point) {
            this.id = idSequence++; //UUID.randomUUID();
            this.point = point;
            this.direction = DEFAULT_DIRECTION;
        }

        public void move(int steps) throws RobotMoveException {
            final MapPoint newPoint;
            try {
                newPoint = switch (direction) {
                    case TOP -> new MapPoint(point.getX() - steps, point.getY());
                    case RIGHT -> new MapPoint(point.getX(), point.getY() + steps);
                    case BOTTOM -> new MapPoint(point.getX() + steps, point.getY());
                    case LEFT -> new MapPoint(point.getX(), point.getY() - steps);
                };

                validatePoint(newPoint);
            } catch (PointValidationException e) {
                throw new RobotMoveException(e.getMessage(), this);
            }

            this.point = newPoint;
        }

        public void changeDirection(Direction direction) {
            this.direction = direction;
        }

        public MapPoint getPoint() {
            return point;
        }

        @Override
        public Long getId(){
            return id;
        }

        public Direction getDirection(){
            return direction;
        }

        @Override
        public String toString() {
            return "Robot-" + id + point;
        }

        
    }

    public class MapPoint extends Point {

        public MapPoint(int x, int y) throws PointValidationException {
            super(x, y);

            if (x < 0 || x > n || y < 0 || y > m) {
                throw new PointValidationException("Недопустимое значение Point: " + this);
            }
        }
    }

    @Override
    public DefoltRobot createRobot(Point point) throws RobotCreationException {
        final MapPoint robotPosition;
        try {
            validatePoint(point);
            robotPosition = new MapPoint(point.getX(), point.getY());
        } catch (PointValidationException e) {
            throw new RobotCreationException(e.getMessage());
        }

        DefoltRobot robot = new DefoltRobot(robotPosition);
        robots.add(robot);
        return robot;
    }

}
