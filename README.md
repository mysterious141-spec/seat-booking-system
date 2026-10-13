# Arrays in Real-World Applications: Cinema Seat Booking System

## Assignment Statement

> Describe real-world applications that require arrays as a data structure, explain why arrays are necessary, and choose one application to develop.

---

## 1. Real-World Applications That Require Arrays

An array is a fixed-size, ordered collection of items of the same type, stored side by side in memory and accessed by an index. Many everyday systems depend on this structure.

| Application | How arrays are used |
|---|---|
| **Student records** | Scores or names of many students stored in order, e.g. `scores[0]` to `scores[n-1]` |
| **Banking** | A customer's list of transactions or monthly balances |
| **Image processing** | A photo is a 2D array of pixels (rows x columns), each holding a colour value |
| **Seat booking** | Cinema, bus, train or flight seats modelled as a grid of rows and columns |
| **Sensor and weather data** | Hourly temperature readings stored in sequence for analysis |
| **Games** | Chess, draughts, tic-tac-toe and Sudoku boards are 2D arrays |
| **Inventory and point of sale** | Item prices and stock levels held in parallel arrays |
| **Spreadsheets and tables** | Rows and columns of data are 2D arrays |

---

## 2. Why Arrays Are Necessary

1. **Fast direct access.** Any element is reached immediately by its index (`scores[5]`) without searching through the others.
2. **Grouping related data.** One array replaces dozens of separate variables such as `score1`, `score2`, `score3`, and so on.
3. **Easy processing with loops.** A single `for` loop can read, update or total thousands of items in a few lines of code.
4. **Efficient memory use.** Elements are stored next to each other in memory, which makes reading and processing fast.
5. **Natural fit for tables and grids.** Two-dimensional arrays represent rows and columns directly, such as seats, boards and images.
6. **Foundation for other structures.** Lists, stacks, queues and matrices are built on arrays.

---

## 3. Chosen Application: Cinema Seat Booking System

### Problem
A cinema needs a simple system to show which seats are free, book a seat for a customer, cancel a booking, find a customer's seat, and report how many seats are sold and the revenue earned.

### Why arrays suit this problem
- A cinema hall is a grid, so a **2D array** (`[row][column]`) matches it exactly.
- The number of seats is fixed (5 rows x 6 seats = 30), which fits the fixed size of an array.
- Every seat must be reachable instantly, e.g. seat `B3` maps straight to `booked[1][2]`.
- Loops over rows and columns draw the seat map, count bookings and search by name without repeating code.

### Data design
| Array | Type | Purpose |
|---|---|---|
| `booked[ROWS][COLS]` | `boolean` | `true` if the seat is taken, `false` if free |
| `holders[ROWS][COLS]` | `String` | Name of the customer who booked the seat |

Both arrays use the same indexes, so `booked[1][2]` and `holders[1][2]` describe the same seat.

### Features
1. View the seat map (`[ ]` available, `[X]` booked)
2. Book a seat (rejects invalid input, taken seats and empty names)
3. Cancel a booking
4. Find a seat by customer name
5. Summary: total, booked and available seats, and revenue (GHS 50 per seat)
6. Exit

### How the program works
1. The menu repeats in a `do-while` loop until the user chooses Exit.
2. `parseSeat()` converts input such as `B3` into array indexes `[1][2]`, and returns `null` if the seat is invalid.
3. `bookSeat()` checks the seat is free, asks for a name, then sets `booked[r][c] = true` and stores the name in `holders[r][c]`.
4. `cancelSeat()` reverses the booking by resetting both arrays at that position.
5. `showSeats()` and `summary()` use nested `for` loops to go through every row and column.
6. `findByName()` loops through the grid and compares names ignoring upper and lower case.

---

## 4. The Code (`SeatingBooking.java`)

```java
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
```

### How to run
Requires Java (JDK 8 or later).

```
javac SeatingBooking.java
java SeatingBooking
```

---

## 5. Sample Output

```
===== CINEMA SEAT BOOKING =====
1. View seat map
2. Book a seat
3. Cancel a booking
4. Find a seat by customer name
5. Summary (available, booked, revenue)
6. Exit
Choose: 2

          S C R E E N
     1   2   3   4   5   6
 A  [ ] [ ] [ ] [ ] [ ] [ ]
 B  [ ] [ ] [ ] [ ] [ ] [ ]
 C  [ ] [ ] [ ] [ ] [ ] [ ]
 D  [ ] [ ] [ ] [ ] [ ] [ ]
 E  [ ] [ ] [ ] [ ] [ ] [ ]

Enter seat to book (e.g. B3): A1
Customer name: Alex
Seat A1 booked for Alex. Price: GHS 50.00
```

Seat map after the booking:

```
          S C R E E N
     1   2   3   4   5   6
 A  [X] [ ] [ ] [ ] [ ] [ ]
 B  [ ] [ ] [ ] [ ] [ ] [ ]
 C  [ ] [ ] [ ] [ ] [ ] [ ]
 D  [ ] [ ] [ ] [ ] [ ] [ ]
 E  [ ] [ ] [ ] [ ] [ ] [ ]
```

Booking the same seat again:

```
Sorry, that seat is already taken by Alex.
```

---

## 6. Conclusion

Arrays are essential wherever data comes in an ordered, fixed-size collection or a grid. In the seat booking system, 2D arrays give instant access to every seat, keep the data organised, and let simple loops handle the whole hall. The same idea powers student record systems, image processing, game boards and many other real-world applications.
