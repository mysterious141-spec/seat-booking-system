import java.util.Scanner;

/**
 * Cinema Seat Booking System
 * Uses a 2D array (rows x columns) to store seat status and a matching
 * 2D array to store the name of whoever booked each seat.
 */
public class SeatingBooking {

    static final int ROWS = 5;
    static final int COLS = 6;
    static final double PRICE = 50.0;

    static boolean[][] booked = new boolean[ROWS][COLS];
    static String[][] holders = new String[ROWS][COLS];

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== CINEMA SEAT BOOKING =====");
            System.out.println("1. View seat map");
            System.out.println("2. Book a seat");
            System.out.println("3. Cancel a booking");
            System.out.println("4. Find a seat by customer name");
            System.out.println("5. Summary (available, booked, revenue)");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            try {
                choice = Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1: showSeats(); break;
                case 2: bookSeat(in); break;
                case 3: cancelSeat(in); break;
                case 4: findByName(in); break;
                case 5: summary(); break;
                case 6: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Enter 1-6.");
            }
        } while (choice != 6);
    }

    // Display the seat map: [ ] = available, [X] = booked
    static void showSeats() {
        System.out.println("\n          S C R E E N");
        System.out.print("    ");
        for (int c = 0; c < COLS; c++) {
            System.out.print(" " + (c + 1) + "  ");
        }
        System.out.println();
        for (int r = 0; r < ROWS; r++) {
            System.out.print(" " + (char) ('A' + r) + "  ");
            for (int c = 0; c < COLS; c++) {
                System.out.print(booked[r][c] ? "[X] " : "[ ] ");
            }
            System.out.println();
        }
        System.out.println("\n[ ] available   [X] booked");
    }

    static void bookSeat(Scanner in) {
        showSeats();
        System.out.print("Enter seat to book (e.g. B3): ");
        int[] pos = parseSeat(in.nextLine());
        if (pos == null) {
            System.out.println("Invalid seat. Use a row letter A-" + (char) ('A' + ROWS - 1)
                    + " and a number 1-" + COLS + ".");
            return;
        }
        int r = pos[0], c = pos[1];
        if (booked[r][c]) {
            System.out.println("Sorry, that seat is already taken by " + holders[r][c] + ".");
            return;
        }
        System.out.print("Customer name: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Booking cancelled.");
            return;
        }
        booked[r][c] = true;
        holders[r][c] = name;
        System.out.printf("Seat %c%d booked for %s. Price: GHS %.2f%n",
                (char) ('A' + r), c + 1, name, PRICE);
    }

    static void cancelSeat(Scanner in) {
        System.out.print("Enter seat to cancel (e.g. B3): ");
        int[] pos = parseSeat(in.nextLine());
        if (pos == null) {
            System.out.println("Invalid seat.");
            return;
        }
        int r = pos[0], c = pos[1];
        if (!booked[r][c]) {
            System.out.println("That seat is not booked.");
            return;
        }
        System.out.println("Booking for " + holders[r][c] + " cancelled.");
        booked[r][c] = false;
        holders[r][c] = null;
    }

    static void findByName(Scanner in) {
        System.out.print("Customer name to search: ");
        String name = in.nextLine().trim();
        boolean found = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (booked[r][c] && holders[r][c].equalsIgnoreCase(name)) {
                    System.out.println(holders[r][c] + " -> seat " + (char) ('A' + r) + (c + 1));
                    found = true;
                }
            }
        }
        if (!found) System.out.println("No booking found for " + name + ".");
    }

    static void summary() {
        int count = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (booked[r][c]) count++;
            }
        }
        int total = ROWS * COLS;
        System.out.println("\nTotal seats : " + total);
        System.out.println("Booked      : " + count);
        System.out.println("Available   : " + (total - count));
        System.out.printf("Revenue     : GHS %.2f%n", count * PRICE);
    }

    // Converts "B3" into {1, 2}; returns null if invalid
    static int[] parseSeat(String input) {
        input = input.trim().toUpperCase();
        if (input.length() < 2) return null;
        int r = input.charAt(0) - 'A';
        int c;
        try {
            c = Integer.parseInt(input.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        if (r < 0 || r >= ROWS || c < 0 || c >= COLS) return null;
        return new int[]{r, c};
    }
}
