package org.example;

import java.sql.*;
import java.util.Scanner;


public class SimpleLibrarySystem {

    private static final String URL = "jdbc:mysql://localhost:3306/booklib?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "111111";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSimple Library System");
            System.out.println("1. List all books");
            System.out.println("2. Add a book");
            System.out.println("3. Delete a book");
            System.out.println("4. Search for a book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listBooks();
                    break;
                case "2":
                    addBook(scanner);
                    break;
                case "3":
                    deleteBook(scanner);
                    break;
                case "4":
                    searchBook(scanner);
                    break;
                case "5":
                    System.out.println("Exiting the system...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void listBooks() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Book")) {

            boolean hasBooks = false;
            while (rs.next()) {
                hasBooks = true;
                int bookID = rs.getInt("BookID");
                String title = rs.getString("Title");
                String isbn = rs.getString("ISBN");
                String publisher = rs.getString("Publisher");
                Date publicationDate = rs.getDate("PublicationDate");
                int pageCount = rs.getInt("PageCount");
                String language = rs.getString("Language");
                String coverImage = rs.getString("CoverImage");

                System.out.println("BookID: " + bookID + ", Title: " + title + ", ISBN: " + isbn +
                        ", Publisher: " + publisher + ", Publication Date: " + publicationDate +
                        ", Page Count: " + pageCount + ", Language: " + language + ", Cover Image: " + coverImage);
            }
            if (!hasBooks) {
                System.out.println("No books found.");
            }
        } catch (SQLException e) {
            System.out.println("Error listing books.");
            e.printStackTrace();
        }
    }

    private static void addBook(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter publication date (yyyy-MM-dd): ");
        String publicationDate = scanner.nextLine();
        System.out.print("Enter page count: ");
        int pageCount = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter language: ");
        String language = scanner.nextLine();
        System.out.print("Enter cover image path: ");
        String coverImage = scanner.nextLine();

        String sql = "INSERT INTO Book (Title, ISBN, Publisher, PublicationDate, PageCount, Language, CoverImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, isbn);
            pstmt.setString(3, publisher);
            try {
                pstmt.setDate(4, Date.valueOf(publicationDate));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                return;
            }
            pstmt.setInt(5, pageCount);
            pstmt.setString(6, language);
            pstmt.setString(7, coverImage);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteBook(Scanner scanner) {
        System.out.print("Enter book ISBN to delete: ");
        String isbn = scanner.nextLine();

        String sql = "DELETE FROM Book WHERE ISBN = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void searchBook(Scanner scanner) {
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Book WHERE Title LIKE ?")) {
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                int bookID = rs.getInt("BookID");
                // 获取实际的书名
                String actualTitle = rs.getString("Title");
                String isbn = rs.getString("ISBN");
                String publisher = rs.getString("Publisher");
                Date publicationDate = rs.getDate("PublicationDate");
                int pageCount = rs.getInt("PageCount");
                String language = rs.getString("Language");
                String coverImage = rs.getString("CoverImage");

                // 使用实际的书名进行打印
                System.out.println("BookID: " + bookID + ", Title: " + actualTitle + ", ISBN: " + isbn +
                        ", Publisher: " + publisher + ", Publication Date: " + publicationDate +
                        ", Page Count: " + pageCount + ", Language: " + language + ", Cover Image: " + coverImage);
            }
            if (!found) {
                System.out.println("No books found with that title.");
            }
        } catch (SQLException e) {
            System.out.println("Error searching for book.");
            e.printStackTrace();
        }
    }

    static class Book {
        private int bookID;
        private String title;
        private String isbn;
        private String publisher;
        private Date publicationDate;
        private int pageCount;
        private String language;
        private String coverImage;

        public Book(int bookID, String title, String isbn, String publisher, Date publicationDate, int pageCount, String language, String coverImage) {
            this.bookID = bookID;
            this.title = title;
            this.isbn = isbn;
            this.publisher = publisher;
            this.publicationDate = publicationDate;
            this.pageCount = pageCount;
            this.language = language;
            this.coverImage = coverImage;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "bookID=" + bookID +
                    ", title='" + title + '\'' +
                    ", isbn='" + isbn + '\'' +
                    ", publisher='" + publisher + '\'' +
                    ", publicationDate=" + publicationDate +
                    ", pageCount=" + pageCount +
                    ", language='" + language + '\'' +
                    ", coverImage='" + coverImage + '\'' +
                    '}';
        }
    }
}