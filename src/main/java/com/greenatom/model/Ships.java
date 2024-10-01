package com.greenatom.model;

import com.greenatom.exception.ShipOverlapsException;
import com.greenatom.exception.ShipTypeException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Ships {

    private int countFourDeckShip = 1;
    private int countTripleDeckShip = 2;
    private int countDoubleDeckShip = 3;
    private int countSingleDeskShip = 4;

    private Area area;

    public Ships(){}
    public Ships(Area area) {
        this.area = area;
    }


    public boolean shipsRunOut() {
        return countSingleDeskShip == 0 &&
                countDoubleDeckShip == 0 &&
                countTripleDeckShip == 0 &&
                countFourDeckShip == 0;
    }

    public Point stringToPoint(String point) throws ShipTypeException {

        String[] points = point.replace(" ", "").split(",");

        if (point.length() < 2 || point.length() > 3)
            throw new ShipTypeException();

        int x = Integer.parseInt(points[0])-1;
        int y = Integer.parseInt(points[1])-1;


        return new Point(y, x);
    }

    public boolean isHit(Point chosenPoint) throws IOException {

        int valueAtPoint = area.getElement(chosenPoint.getX(), chosenPoint.getY());

        if (valueAtPoint == 1) {
            setWhippedShipByCoordinates(chosenPoint);
            return true;
        }

        return false;
    }

    public void placeShip(String str) throws IOException {

        String[] point = str.trim().split(" ");

        Point point1;
        Point point2;

        if (point.length == 1) {
            point1 = stringToPoint(point[0].replace(" ", ""));
            point2 = point1;
        } else if(point.length == 2) {

            String points1 = point[0].replace(" ", "");
            String points2 = point[1].replace(" ", "");

            point1 = stringToPoint(points1);
            point2 = stringToPoint(points2);

        } else {
            System.out.println("Bad coordinates");
            return;
        }

        placeShip(point1, point2);

    }

    public boolean checkPointByZero(Point point) throws IOException {
        return area.getElement(point) == 0;
    }

    public void setShipByCoordinates(Point ship) throws IOException {
        if (area.getElement(ship.getX(), ship.getY()) != 0) {
            throw new ShipOverlapsException();
        }
        area.getMap()[ship.getX()][ship.getY()] = 1;
    }

    public void setWhippedShipByCoordinates(Point ship) throws IOException {
        area.getMap()[ship.getX()][ship.getY()] = 2;
    }

    public void setKillShipByCoordinates(Point ship) {
        area.getMap()[ship.getX()][ship.getY()] = 3;
    }

    public void setMissByCoordinates(Point ship) {
        area.getMap()[ship.getX()][ship.getY()] = 4;
    }

    public void placeShip(Point first, Point second) throws IOException {

        // Проверка на корректность ориентации (корабль должен быть вертикальным или горизонтальным)
        if (first.getX() != second.getX() && first.getY() != second.getY()) {
            throw new RuntimeException("Bad point: Ship must be horizontal or vertical.");
        }

        int length;

        // Горизонтальное размещение
        if (first.getX() == second.getX()) {

            length = Math.abs(first.getY() - second.getY()) + 1; // Длина корабля

            if (length > area.getHeight() || length > area.getWidth())
                throw new IOException("Bad length");

            // Проверяем допустимость длины и доступность такого типа корабля
            checkLength(length);

            int row = first.getX();
            int startCol = Math.min(first.getY(), second.getY());
            int endCol = Math.max(first.getY(), second.getY());

            // Проверка на пересечение с другими кораблями
            for (int col = startCol; col <= endCol; col++) {
                if (!checkPointByZero(new Point(row, col))) {
                    throw new ShipOverlapsException();
                }
            }

            for (int col = startCol; col <= endCol; col++) {
                setShipByCoordinates(new Point(row, col));
            }

        } else {

            length = Math.abs(first.getX() - second.getX()) + 1;

            if (length > area.getHeight() || length > area.getWidth())
                throw new IOException("Bad length");

            checkLength(length);

            int col = first.getY();
            int startRow = Math.min(first.getX(), second.getX());
            int endRow = Math.max(first.getX(), second.getX());

            // Проверка на пересечение с другими кораблями
            for (int row = startRow; row <= endRow; row++) {
                if (!checkPointByZero(new Point(row, col))) {
                    throw new ShipOverlapsException();
                }
            }

            for (int row = startRow; row <= endRow; row++) {
                setShipByCoordinates(new Point(row, col));
            }
        }

        System.out.println("Placed ship of length: " + length);
    }

    public boolean allShipsHit(Ships opponentShips) throws IOException {
        int[][] opponentMap = opponentShips.area.getMap();
        for (int i = 0; i < opponentMap.length; i++) {
            for (int j = 0; j < opponentMap[i].length; j++) {
                if (opponentMap[i][j] == 1) {
                    if (area.getElement(i, j) != 2 && area.getElement(i, j) != 3) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void checkLength(int length) throws ShipTypeException {
        switch (length) {
            case 1 -> {
                if (countSingleDeskShip == 0)
                    throw new ShipTypeException("No single-deck ships available.");
                countSingleDeskShip--;
            }
            case 2 -> {
                if (countDoubleDeckShip == 0)
                    throw new ShipTypeException("No double-deck ships available.");
                countDoubleDeckShip--;
            }
            case 3 -> {
                if (countTripleDeckShip == 0)
                    throw new ShipTypeException("No triple-deck ships available.");
                countTripleDeckShip--;
            }
            case 4 -> {
                if (countFourDeckShip == 0)
                    throw new ShipTypeException("No four-deck ships available.");
                countFourDeckShip--;
            }
            default -> throw new RuntimeException("Invalid ship length: " + length + ". Allowed lengths: 1, 2, 3, 4.");
        }
    }

    public String getShipsCounts() {
        return new StringBuilder("Single-Desk Ships: ").append(countSingleDeskShip).append(", ")
                .append("Double-Deck Ships: ").append(countDoubleDeckShip).append(", ")
                .append("Triple-Deck Ships: ").append(countTripleDeckShip).append(", ")
                .append("Four-Deck Ships: ").append(countFourDeckShip)
                .toString();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i <= area.getHeight(); i++) {
            string.append("%3d\t".formatted(i)); // вывод номеров столбцов
        }
        string.append("\n");

        for (int i = 1; i <= area.getHeight(); i++) {
            string.append("%3d\t".formatted(i));
            for (int j = 1; j <= area.getWidth(); j++) {
                try {
                    string.append("|").append(area.getElement(new Point(i-1, j-1))).append("|\t");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            string.append("\n");
        }

        return string.toString();
    }
}

