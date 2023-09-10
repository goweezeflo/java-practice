package com.goweezeflo.java;

import java.sql.*;

// $ docker run -d --name mysql -p 3306:3306 --mount source=mysql,target=/var/lib/mysql --restart unless-stopped goweezeflo/mysql
// $ docker exec -it mysql mysql -ugoweezeflo -p

public class Main {
    static final String SQL_QUERY = "SELECT * FROM books";
    static final String DB_HOST = "docker";
    static final int DB_PORT = 3306;
    static final String DB_NAME = "goweezeflo";
    static final String DB_USER = "goweezeflo";
    static final String DB_PASS = "Goweezeflo_123";
    static final String ID = "id";
    static final String BOOK_NAME = "book_name";
    static final String AUTHOR = "author";
    static final String YEAR = "year";
    static final String EDITION = "edition";

    public static void main(String[] args) {
        String connectionUrl = "jdbc:mysql://%s:%d/%s?serverTimezone=UTC".formatted(DB_HOST, DB_PORT, DB_NAME);

        try (Connection conn = DriverManager.getConnection(connectionUrl, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(SQL_QUERY);
             ResultSet rs = ps.executeQuery()) {
            printQueryResults(rs);
        } catch (SQLException e) {
            System.out.printf("JDBC - ERROR %s - %s%n", e.getSQLState(), e.getMessage());
        }
    }

    private static void printQueryResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            long id = rs.getLong(ID);
            System.out.println(ID + ": " + id);

            String book_name = rs.getString(BOOK_NAME);
            System.out.println(BOOK_NAME + ": " + book_name);

            String author = rs.getString(AUTHOR);
            System.out.println(AUTHOR + ": " + author);

            int year = rs.getInt(YEAR);
            System.out.println(YEAR + ": " + year);

            int edition = rs.getInt(EDITION);
            System.out.println(EDITION + ": " + edition);
        }
    }
}
