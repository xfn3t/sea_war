package com.greenatom.controller;

import com.greenatom.exception.ShipTypeException;
import com.greenatom.model.Area;
import com.greenatom.model.Point;
import com.greenatom.model.Ships;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GameController {

    private static final Random RANDOM = new Random();

    public void enterShip(Ships ships) {

        Scanner scanner = new Scanner(System.in);

        while (!ships.shipsRunOut()) {

            System.out.println(ships);
            System.out.println(ships.getShipsCounts());

            System.out.println("Enter Ship coordinates by mask (x,y x,y) or for single-desk(x,y)");
            System.out.print("Enter: ");
            try {
                ships.placeShip(scanner.nextLine());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void gameForTwo(int height, int width) throws IOException {

        Area area1 = new Area(height, width);
        Ships player1Ships = new Ships(area1);

        Area hiddenArea1 = new Area(height, width);
        Ships hiddenPlayer1Ships = new Ships(hiddenArea1);

        Area area2 = new Area(height, width);
        Ships player2Ships = new Ships(area2);

        Area hiddenArea2 = new Area(height, width);
        Ships hiddenPlayer2Ships = new Ships(hiddenArea2);


        player1Ships.placeShip(new Point(1, 1), new Point(1, 1));
        player1Ships.placeShip(new Point(2, 1), new Point(2, 1));
        player1Ships.placeShip(new Point(3, 1), new Point(3, 1));
        player1Ships.placeShip(new Point(4, 1), new Point(4, 1));

        player1Ships.placeShip(new Point(1, 2), new Point(2, 2));
        player1Ships.placeShip(new Point(1, 3), new Point(2, 3));
        player1Ships.placeShip(new Point(1, 4), new Point(2, 4));

        player1Ships.placeShip(new Point(3, 2), new Point(3, 4));
        player1Ships.placeShip(new Point(4, 2), new Point(4, 4));

        player1Ships.placeShip(new Point(5, 1), new Point(8, 1)); // 4-палубный корабль

        // Размещаем корабли для игрока 2
        player2Ships.placeShip(new Point(1, 1), new Point(1, 1));
        player2Ships.placeShip(new Point(2, 1), new Point(2, 1));
        player2Ships.placeShip(new Point(3, 1), new Point(3, 1));
        player2Ships.placeShip(new Point(4, 1), new Point(4, 1));

        player2Ships.placeShip(new Point(1, 2), new Point(2, 2));
        player2Ships.placeShip(new Point(1, 3), new Point(2, 3));
        player2Ships.placeShip(new Point(1, 4), new Point(2, 4));

        player2Ships.placeShip(new Point(3, 2), new Point(3, 4));
        player2Ships.placeShip(new Point(4, 2), new Point(4, 4));

        player2Ships.placeShip(new Point(5, 1), new Point(8, 1)); // 4-палубный корабль

        boolean player1Turn = true;
        boolean gameOn = true;

        while (gameOn) {
            if (player1Turn) {

                System.out.println("Your map:");
                System.out.println(player1Ships);

                System.out.println("Hidden map player 2:");
                System.out.println(hiddenPlayer2Ships);

                System.out.println("Player 1, choice point:");
                Point chosenPoint = getShipCoordinates();

                if (player2Ships.checkPointByZero(chosenPoint)) {

                    System.out.println("Miss!");
                    player2Ships.setMissByCoordinates(chosenPoint);
                    hiddenPlayer2Ships.setMissByCoordinates(chosenPoint);
                    player1Turn = false;

                } else if (player2Ships.isHit(chosenPoint)) {
                    System.out.println("It's a hit! Player 1 shoots again..");
                    player2Ships.setWhippedShipByCoordinates(chosenPoint);
                    hiddenPlayer2Ships.setWhippedShipByCoordinates(chosenPoint);

                    if (player1Ships.allShipsHit(player2Ships)) {
                        System.out.println("Player 1 is won!");
                        gameOn = false;
                    }
                }

            } else {

                System.out.println("Your map:");
                System.out.println(player2Ships);

                System.out.println("Hidden map player 1:");
                System.out.println(hiddenPlayer1Ships);

                System.out.println("Player 2, choice point:");
                Point chosenPoint = getShipCoordinates();

                if (player1Ships.checkPointByZero(chosenPoint)) {

                    System.out.println("Miss!");
                    player1Ships.setMissByCoordinates(chosenPoint);
                    hiddenPlayer1Ships.setMissByCoordinates(chosenPoint);
                    player1Turn = true;

                } else if (player1Ships.isHit(chosenPoint)) {

                    System.out.println("It's a hit! Player 2 shoots again.");
                    player1Ships.setWhippedShipByCoordinates(chosenPoint);
                    hiddenPlayer1Ships.setWhippedShipByCoordinates(chosenPoint);

                    if (player2Ships.allShipsHit(player1Ships)) {
                        System.out.println("Player 2 is won!");
                        gameOn = false;
                    }
                }
            }
        }
    }

    public void gameWithComputer(int height, int width) throws IOException {

        Area playerArea = new Area(height, width);
        Ships playerShips = new Ships(playerArea);

        Area computerArea = new Area(height, width);
        Ships computerShips = new Ships(computerArea);

        Area palyerHiddenArea = new Area(height, width);
        Ships playerHiddenShips = new Ships(palyerHiddenArea);// Скрытая карта для отображения ходов игрока

        Area computerHiddenArea = new Area(height, width);
        Ships computerHiddenShips = new Ships(computerHiddenArea); // Скрытая карта для отображения ходов компьютера


        System.out.println("Player, place your ships:");
        enterShip(playerShips);

        System.out.println("Computer is placing its ships...");
        placeComputerShips(computerShips);

        boolean playerTurn = true;
        boolean gameOn = true;

        while (gameOn) {
            if (playerTurn) {

                System.out.println("Computer's hidden map:");
                System.out.println(computerHiddenShips);

                System.out.println("Player, choose your point to attack:");
                Point chosenPoint = getShipCoordinates();

                if (computerShips.checkPointByZero(chosenPoint)) {

                    System.out.println("Miss!");
                    computerHiddenShips.setMissByCoordinates(chosenPoint);
                    computerShips.setMissByCoordinates(chosenPoint);
                    playerTurn = false;

                } else if (!computerHiddenShips.checkPointByZero(chosenPoint)) {
                    System.out.println("Already hit this spot!");
                } else {

                    computerHiddenShips.setWhippedShipByCoordinates(chosenPoint);
                    computerShips.setWhippedShipByCoordinates(chosenPoint);

                    System.out.println("Hit! Player goes again.");
                    if (playerShips.allShipsHit(computerShips)) {
                        System.out.println("Player wins!");
                        gameOn = false;
                    }
                }
            } else {

                System.out.println("Player's map:");
                System.out.println(playerHiddenShips);

                System.out.println("Computer's turn to attack...");

                Point computerChoice = computerMove(playerHiddenShips.getArea().getMap());

                if (playerShips.checkPointByZero(computerChoice)) {

                    System.out.println("Computer missed!");
                    playerHiddenShips.setMissByCoordinates(computerChoice);
                    playerShips.setMissByCoordinates(computerChoice);
                    playerTurn = true;

                } else if (!playerHiddenShips.checkPointByZero(computerChoice)) {
                    System.out.println("Computer already hit this spot!");
                } else {

                    playerHiddenShips.setWhippedShipByCoordinates(computerChoice);
                    playerShips.setWhippedShipByCoordinates(computerChoice);

                    System.out.println("Computer hit your ship!");
                    if (computerShips.allShipsHit(playerShips)) {
                        System.out.println("Computer wins!");
                        gameOn = false;
                    }

                }
            }
        }
    }


    public void placeComputerShips(Ships ships) throws IOException {

        // Массив, который задает количество кораблей для каждого размера
        int[] shipLengths = {4, 3, 2, 1};
        int[] shipCount = {1, 2, 3, 4}; // Количество кораблей для каждого размера

        for (int i = 0; i < shipLengths.length; i++) {
            int length = shipLengths[i];
            int count = shipCount[i];

            for (int j = 0; j < count; j++) {
                boolean placed = false;

                // Пытаемся разместить корабль, пока не удастся
                while (!placed) {

                    // Выбираем случайные координаты для начала корабля
                    int startX = RANDOM.nextInt(ships.getArea().getHeight());
                    int startY = RANDOM.nextInt(ships.getArea().getWidth());

                    // Выбираем случайное направление
                    boolean horizontal = RANDOM.nextBoolean();

                    // Определяем конечные координаты корабля в зависимости от направления и длины
                    int endX = horizontal ? startX : startX + length - 1;
                    int endY = horizontal ? startY + length - 1 : startY;

                    // Проверяем, не выходит ли корабль за границы карты
                    if (endX >= ships.getArea().getHeight() || endY >= ships.getArea().getWidth()) {
                        continue;
                    }

                    // Проверяем, не пересекается ли корабль с другими кораблями
                    boolean overlaps = false;
                    for (int x = startX; x <= endX; x++) {
                        for (int y = startY; y <= endY; y++) {
                            if (!ships.checkPointByZero(new Point(x, y))) {
                                overlaps = true;
                                break;
                            }
                        }
                        if (overlaps) {
                            break;
                        }
                    }

                    // Если корабль не пересекается с другими, размещаем его
                    if (!overlaps) {
                        for (int x = startX; x <= endX; x++) {
                            for (int y = startY; y <= endY; y++) {
                                ships.setShipByCoordinates(new Point(x, y));
                            }
                        }
                        placed = true;
                    }
                }
            }
        }
    }

    public Point computerMove(int[][] playerHiddenMap) {

        int x, y;
        do {
            x = RANDOM.nextInt(playerHiddenMap.length);
            y = RANDOM.nextInt(playerHiddenMap[0].length);
        } while (playerHiddenMap[x][y] == 2 || playerHiddenMap[x][y] == 3);
        return new Point(x, y);
    }



    public Point getShipCoordinates() throws ShipTypeException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter attack coordinates (x,y): ");
        String input = scanner.nextLine();
        return new Ships().stringToPoint(input);
    }


}
