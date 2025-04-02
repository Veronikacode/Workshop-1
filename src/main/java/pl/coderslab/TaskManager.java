package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class TaskManager {

    static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadDataToTab("/Users/wpaliniewicz/coderslab/Workshop1/src/main/java/pl/coderslab/tasks.csv");
        displayOptions();
        getUserInput();
    }

    public static String[][] loadDataToTab(String fileName) {
        Path dir = Paths.get(fileName);
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void displayOptions() {
        String[] options = {"add", "remove", "list", "exit"};

        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static void getUserInput() {
        Scanner scanner = new Scanner (System.in);
        String input = scanner.nextLine().trim();

        switch (input) {
            case "add":
                addTask();
                break;
            case "remove":
                removeTask(tasks, getTheNumber());
                break;
            case "list":
                listTasks(tasks);
                break;
            case "exit":
                System.out.println(ConsoleColors.RED + "Exiting program.");
                saveTabToFile ("/Users/wpaliniewicz/coderslab/Workshop1/src/main/java/pl/coderslab/tasks.csv", tasks);
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Please select a correct option.");
                displayOptions();
                getUserInput();
        }
    }

    public static void addTask() {
        System.out.println("Please add task description:");
        Scanner scanner = new Scanner(System.in);
        String taskDescription = scanner.nextLine();
        System.out.println("Please add task due date:");
        String taskDueDate = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
        String isImportant = scanner.nextLine();

        tasks =  Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length-1] = new String[3];
        tasks[tasks.length-1][0] = taskDescription;
        tasks[tasks.length-1][1] = taskDueDate;
        tasks[tasks.length-1][2] = isImportant;

        displayOptions ();
        getUserInput ();
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            n = scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
                System.out.println(ConsoleColors.GREEN + "Task removed." + ConsoleColors.RESET);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
        displayOptions ();
        getUserInput ();
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static void listTasks(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
        displayOptions();
        getUserInput();
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
