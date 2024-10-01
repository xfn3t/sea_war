package com.greenatom;

import com.greenatom.controller.GameController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class App {

    private static Lang lang = Lang.ENG;
    private static String langPath = "lang\\ru\\";
    private final static String PROJECT_PATH = "src\\main\\java\\com\\greenatom\\resources\\";
    private static final GameController gameController = new GameController();

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String langPath() {
        switch (lang) {
            case RU -> langPath = PROJECT_PATH + "lang\\ru\\";
            case ENG -> langPath = PROJECT_PATH + "lang\\en\\";
        }
        return langPath;
    }

    private static void printFile(String fileName) throws IOException {

        File file = new File(langPath() + fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            System.out.println(st);
    }


    public static void main( String[] args ) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String key;

        while(true) {

            clearScreen();

            printFile("mainInterface.txt");
            key = scanner.nextLine();

            switch (key) {

                case "1" -> {
                    printFile("gameStyle.txt");
                    String key2 = scanner.nextLine();
                    if (key2.trim().isEmpty()) break;

                    switch (key2) {
                        case "1" -> gameController.gameForTwo(10, 10);
                        case "2" -> gameController.gameWithComputer(10, 10);
                        case "0" -> {
                            return;
                        }
                        default -> System.out.println("Unexpected value: " + key);
                    }
                }
                case "2" -> lang = lang.equals(Lang.RU) ? Lang.ENG : Lang.RU;
                case "0" -> {
                    return;
                }

                default -> System.out.println("Unexpected value: " + key);
            }
        }

    }
}
