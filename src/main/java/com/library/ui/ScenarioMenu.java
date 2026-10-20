package com.library.ui;

import com.library.dao.ScenarioDao;
import com.library.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {
    private static final String MENU_LINE = "=".repeat(53);
    private static final String TABLE_TITLE_LINE = "=".repeat(88);
    private static final String BORDER =
            "+----+--------------------+--------------------+---------------+----------------+";
    private static final String ROW_FORMAT = "| %-2s | %-18s | %-18s | %-13s | %-14s |%n";

    private final ScenarioDao dao = new ScenarioDao();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("\nInvalid input. Please enter 1, 2, or 3.");
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear the input buffer

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid option. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n" + MENU_LINE);
        System.out.println(" ".repeat(14) + "LIBRARY BOOK CATALOG");
        System.out.println(MENU_LINE);
        System.out.println("[1] Add New Book");
        System.out.println("[2] View Book Catalog");
        System.out.println("[3] Exit System");
        System.out.println(MENU_LINE);
    }

    private void addBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        String title = promptNonEmpty("Enter Title: ");
        String author = promptNonEmpty("Enter Author: ");

        String isbn;
        while (true) {
            System.out.print("Enter ISBN: ");
            isbn = scanner.nextLine().trim();
            if (isbn.matches("\\d{13}")) break;
            System.out.println("ERROR: ISBN must be exactly 13 digits.");
        }

        String status;
        while (true) {
            System.out.print("Enter Status (AVAILABLE / BORROWED): ");
            status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("AVAILABLE") || status.equals("BORROWED")) break;
            System.out.println("ERROR: Status must be AVAILABLE or BORROWED.");
        }

        if (dao.insert(new ScenarioEntity(0, title, author, isbn, status))) {
            System.out.println("\nSUCCESS: Book cataloged into database!");
        } else {
            System.out.println("\nERROR: Failed to catalog book.");
        }
        System.out.println();
    }

    private void viewBooks() {
        List<ScenarioEntity> books = dao.findAll();

        System.out.println("\n");
        System.out.println(TABLE_TITLE_LINE);
        System.out.println(" ".repeat(32) + "LIBRARY BOOK CATALOG");
        System.out.println(TABLE_TITLE_LINE);

        if (books.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println(BORDER);
            System.out.printf("| %-2s | %-18s | %-18s | %-13s | %-14s |%n",
                    "ID", "TITLE", "AUTHOR", "ISBN", "STATUS");
            System.out.println(BORDER);
            for (ScenarioEntity b : books) {
                System.out.printf(ROW_FORMAT, b.id(), cut(b.title(), 18), cut(b.author(), 18),
                        b.isbn(), b.status());
            }
            System.out.println(BORDER);
            System.out.println("Total Records Displayed: " + books.size());
        }
        System.out.println();
    }

    private String promptNonEmpty(String label) {
        while (true) {
            System.out.print(label);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    private String cut(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
