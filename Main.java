package org.example.lesson1.example;

import java.util.*;
// import org.example.lesson1.example.RobotMap.Robot;

import org.example.lesson1.example.DefoltRobotMap.DefoltRobot;

public class Main {

    public static void main(String[] args) {
        // Есть двумерная карта с роботами
        // Роботы это объекты, они могут перемещаться по карте
        // Карта имеет какие-то границы
        // 2 робота не могут находиться в одной точке одновременно
        // Роботы имеют направление (которое можно менять (поворачивать)),
        //              роботы идут только вперед (или стоят на месте).

        Scanner userInput = new Scanner(System.in);

        System.out.println("Добро пожаловать в игру!");
        System.out.println("...описание...");

        DefoltRobotMap map;
        while (true) {
            System.out.println("Для создания карты введите 2 положительных числа через пробел");
            try {
                int n = userInput.nextInt();
                int m = userInput.nextInt();
                userInput.nextLine();

                map = new DefoltRobotMap(n, m);
                break;
            } catch (RobotMapCreationException | InputMismatchException e) {
                System.err.println("Возникла ошибка при создании карты: " + e.getMessage());
            } catch (Throwable e) {
                System.err.println("Возникла ошибка на стороне сервера: " + e.getMessage());
                System.exit(1);
            }
        }

        System.out.println("Карта успешно создана!");

        CommandManager commandManager = new CommandManager(map);

        System.out.println("Для просмотра списка допустимых команд введите h");

        while (true) {
            System.out.println("Введите команду");
            String command = userInput.nextLine();
            try {
                String commandExecutionResult = commandManager.handleCommand(command);
                if (Objects.nonNull(commandExecutionResult) && !commandExecutionResult.isBlank()) {
                    System.out.println(commandExecutionResult);
                }
            } catch (CommandNotFoundException e) {
                System.err.println("Команда [" + e.getMessage() + "] не найдена");
            } catch (CommandExecutionException e) {
                System.err.println("Во время исполнения команды произошла ошибка: " + e.getMessage());
            }
        }

    }

    private static class CommandManager {

        private final RobotMap map;
        private final Map<String, CommandExecutor> commands;

        public CommandManager(RobotMap map) {
            this.map = map;

            commands = new HashMap<>();
            commands.put("h", this::printHelp);
            commands.put("q", this::quit);
            commands.put("a", this::addRobot);
            commands.put("l", this::listRobots);
            commands.put("cd", this::changeRobotDirection);
            commands.put("m", this::moveRobot);
        }

        public DefoltRobot findRobot(Long id) throws CommandExecutionException {
            List<DefoltRobot> listRobots = map.getRobotList();
            if (id > listRobots.size() || id < 1) {
                throw new CommandExecutionException("Робота с таким  ID не существует");
            }
            
            for (int i = 0; i < listRobots.size(); i++) {
                if(listRobots.get(i).getId() == id){
                    DefoltRobot robot = listRobots.get(i);
                    return robot;
                }                 
            }
            return null;            
        }

        public String handleCommand(String command) throws CommandNotFoundException, CommandExecutionException {
            String[] split = command.split(" ");
            String commandCode = split[0];

            CommandExecutor executor = commands.get(commandCode);
            if (executor == null) {
                throw new CommandNotFoundException(command);
            }

            String[] args = Arrays.copyOfRange(split, 1, split.length);
            return executor.execute(args);
        }

        private String addRobot(String[] args) throws CommandExecutionException {
            if (args.length < 2) {
                throw new CommandExecutionException("Недостаточно аргументов");
            }

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);

            try {
                map.createRobot(new Point(x, y));
            } catch (RobotCreationException e) {
                throw new CommandExecutionException(e.getMessage());
            }

            return null;
        }

        private String listRobots(String[] args) {
            List<DefoltRobot> listRobots = map.getRobotList();
            for (int i = 0; i < listRobots.size(); i++) {
                System.out.println(listRobots.get(i));
            } 
            return null;
        }

        private String changeRobotDirection(String[] args)throws CommandExecutionException{
            if (args.length < 2) {
                throw new CommandExecutionException("Недостаточно аргументов");
            }

            Long id = Long.parseLong(args[0]);
            String strDirection = args[1];
            
            Direction direction;
            DefoltRobot robot = findRobot(id);
            if(strDirection.equals("t")){direction = Direction.TOP;}
            else if(strDirection.equals("r")){direction = Direction.RIGHT;}
            else if(strDirection.equals("b")){direction = Direction.BOTTOM;}
            else if(strDirection.equals("l")){direction = Direction.LEFT;}
            else{throw new CommandExecutionException("Введено неверное направление");}
    
            robot.changeDirection(direction);
            return null;
        }

        private String moveRobot(String[] args) throws CommandExecutionException {
            if (args.length < 2) {
                throw new CommandExecutionException("Недостаточно аргументов");
            }

            // Long id = Long.parseLong(args[0]);
            // Integer steps = Integer.parseInt(args[1]);

            DefoltRobot robot = findRobot(Long.parseLong(args[0]));

            try{
                robot.move(Integer.parseInt(args[1]));
            }catch(RobotMoveException e){
                throw new CommandExecutionException(e.getMessage());
            }
            System.out.println(robot);

            return null;
        }

        private String printHelp(String[] args) {
            return """
                    h                  -> распечатать список допустимых команд (help)
                    a 1 2              -> создать робота на позиции (1, 2) (add)
                    l                  -> распечатать всех роботов (list)
                    m id [5]           -> перемещаем робота на 1 единицу вперед (move)
                    cd id [t, r, b, l] -> изменить направление робота (change direction)
                    q                  -> завершить программу (quit)
                    """;
        }

        private String quit(String[] args) {
            System.exit(0);
            return null;
        }

        private interface CommandExecutor {
            String execute(String[] args) throws CommandExecutionException;
        }

    }

    // private void homework() {
    //     // Доделать остальные команды move, change direction и list
    // }

}
