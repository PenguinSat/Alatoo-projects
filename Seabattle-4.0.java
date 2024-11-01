import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Main().startGame();
    }

    // Constants for the game setup
    public static final int FIELD_SIZE = 7;
    public static final int[][] SHIPS = {{3, 1}, {2, 2}, {1, 4}};
    public static final char EMPTY = ' ';
    public static final char SHIP = 'S';
    public static final char MISS = 'O';
    public static final char HIT = 'X';
    public static final String LETTERS = "ABCDEFG";

    // Game fields and variables
    public char[][] gameField;
    public char[][] playerField;
    public int shots;

    // Constructor initializes the game fields and places ships randomly
    public Main() {
        gameField = new char[FIELD_SIZE][FIELD_SIZE];
        playerField = new char[FIELD_SIZE][FIELD_SIZE];
        shots = 0;
        initializeField(gameField);
        initializeField(playerField);
        placeAllShips();
    }

    // Initializes the game field by filling it with empty spaces
    public void initializeField(char[][] field) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = EMPTY;
            }
        }
    }

    // Checks if a ship can be placed at the given coordinates with specified length and orientation
    public boolean canPlaceShip(int x, int y, int length, boolean horizontal) {
        if (horizontal) {
            if (y + length > FIELD_SIZE) return false;
            for (int i = 0; i < length; i++) {
                if (!isPositionFree(x, y + i)) return false;
            }
        } else {
            if (x + length > FIELD_SIZE) return false;
            for (int i = 0; i < length; i++) {
                if (!isPositionFree(x + i, y)) return false;
            }
        }
        return true;
    }

    // Checks if a cell and its surroundings are free to place part of a ship
    public boolean isPositionFree(int x, int y) {
        if (gameField[x][y] != EMPTY) return false;
        for (int i = Math.max(0, x - 1); i <= Math.min(FIELD_SIZE - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(FIELD_SIZE - 1, y + 1); j++) {
                if (gameField[i][j] != EMPTY) return false;
            }
        }
        return true;
    }

    // Places a ship on the game field at specified coordinates and orientation
    public void placeShip(int x, int y, int length, boolean horizontal) {
        if (horizontal) {
            for (int i = 0; i < length; i++) {
                gameField[x][y + i] = SHIP;
            }
        } else {
            for (int i = 0; i < length; i++) {
                gameField[x + i][y] = SHIP;
            }
        }
    }

    // Randomly places all ships defined in the SHIPS array on the game field
    public void placeAllShips() {
        Random random = new Random();
        for (int[] ship : SHIPS) {
            int length = ship[0];
            int count = ship[1];
            for (int i = 0; i < count; i++) {
                boolean placed = false;
                while (!placed) {
                    int x = random.nextInt(FIELD_SIZE);
                    int y = random.nextInt(FIELD_SIZE);
                    boolean horizontal = random.nextBoolean();
                    if (canPlaceShip(x, y, length, horizontal)) {
                        placeShip(x, y, length, horizontal);
                        placed = true;
                    }
                }
            }
        }
    }

    // Prints the current state of a given field; hides ships if hidden is true
    public void printField(char[][] field, boolean hidden) {
        System.out.print("  ");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print(LETTERS.charAt(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < FIELD_SIZE; j++) {
                char cell = field[i][j];
                if (hidden && cell == SHIP) {
                    System.out.print(EMPTY + " ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    // Converts user input coordinates (e.g., B3) to grid coordinates (e.g., [2, 1])
    public int[] convertCoordinates(String input) {
        if (input == null || input.length() < 2) {
            System.out.println("Error: Enter coordinates in the format letter+number, for example, B3.");
            return null;
        }

        // Convert input to uppercase and trim whitespace
        input = input.trim().toUpperCase();

        // Check if the first character is a valid letter
        char letter = input.charAt(0);
        int y = LETTERS.indexOf(letter); // Get index of the letter
        if (y == -1) {
            System.out.println("Error: Invalid column. Enter a letter from A to G.");
            return null;
        }

        // Ensure the remaining part of the input is a number
        String numberPart = input.substring(1);
        if (!numberPart.matches("\\d+")) { // Check that it's digits only
            System.out.println("Error: Enter a valid number after the letter, for example B3.");
            return null;
        }

        // Convert string to an integer and check bounds
        int number = Integer.parseInt(numberPart) - 1;
        if (number < 0 || number >= FIELD_SIZE) {
            System.out.println("Error: Invalid row number. Enter a number from 1 to " + FIELD_SIZE + ".");
            return null;
        }

        // If all checks pass, return the coordinates
        return new int[]{number, y};
    }

    // Processes a shot at the specified coordinates; returns true if it’s a hit, false otherwise
    public boolean shootAt(int x, int y) {
        if (gameField[x][y] == SHIP) {
            gameField[x][y] = HIT;
            playerField[x][y] = HIT;
            return true;
        } else if (gameField[x][y] == EMPTY) {
            gameField[x][y] = MISS;
            playerField[x][y] = MISS;
        }
        return false;
    }

    // Checks if all ships on the game field have been sunk
    public boolean allShipsSunk() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (gameField[i][j] == SHIP) return false;
            }
        }
        return true;
    }

    // Starts the game loop, handling player input and displaying game status until all ships are sunk
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printField(playerField, true);
            System.out.print("Enter the coordinates for shooting (for example, B3): ");
            String input = scanner.nextLine();
            int[] coords = convertCoordinates(input);

            if (coords == null) {
                System.out.println("Incorrect coordinates! Try again.");
                continue;
            }

            int x = coords[0];
            int y = coords[1];

            if (playerField[x][y] == HIT || playerField[x][y] == MISS) {
                System.out.println("You've already shot here! Try again.");
                continue;
            }
            shots++;
            if (shootAt(x, y)) {
                System.out.println("Hit!");
            } else {
                System.out.println("Miss!");
            }
            if (allShipsSunk()) {
                System.out.println("Congratulations! You won in " + shots + " shots.");
                break;
            }
        }
    }
}
