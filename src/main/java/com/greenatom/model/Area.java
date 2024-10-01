package com.greenatom.model;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class Area {

    private int[][] area = new int[10][10];

    public Area(int height, int width) {
        this.area = new int[height][width];
    }

    public int getHeight() {
        return area.length;
    }

    public int getWidth() {
        return area[0].length;
    }

    public int getElement(Point point) throws IOException {
        if (point.getX() < 0 || point.getY() < 0)
            throw new IOException();

        return area[point.getX()][point.getY()];
    }

    public int getElement(int x, int y) throws IOException {
        if (x < 0 || y < 0)
            throw new IOException();

        return area[x][y];
    }


    public int[][] getMap() {
        return area;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i <= area.length; i++) {
            string.append("%2d\t".formatted(i)); // вывод номеров столбцов
        }
        string.append("\n");

        for (int i = 1; i <= area.length; i++) {
            string.append("%3d ".formatted(i));
            for (int j = 1; j <= area[0].length; j++) {
                string.append("|").append(area[i-1][j-1]).append("|\t");
            }
            string.append("\n");
        }

        return string.toString();
    }
}
