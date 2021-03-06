package com.webproject.javaservlets.dao;

import com.webproject.javaservlets.manager.ResourceManager;
import com.webproject.javaservlets.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    private final String jdbcURL;
    private final String jdbcUsername;
    private final String jdbcPassword;
    private Connection jdbcConnection;
    private PreparedStatement statement;

    private final String INSERT_BOOK_QUERY = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";
    private final String ALL_BOOKS_QUERY = "SELECT * FROM book ORDER BY id";
    private final String DELETE_BOOK_QUERY = "DELETE FROM book WHERE id = ?";
    private final String UPDATE_BOOK_QUERY = "UPDATE book SET title = ?, author = ?, price = ? WHERE id = ?";
    private final String GET_BOOK_QUERY = "SELECT * FROM book WHERE id = ?";

    public BookDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * Connect to the database
     */
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    /**
     * Disconnect from the database
     */
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    /**
     * Insert the book into the database
     */
    public boolean insertBook(Book book) throws SQLException {
        connect();
        statement = jdbcConnection.prepareStatement(INSERT_BOOK_QUERY);
        statement.setString(ResourceManager.parameterIndex1, book.getTitle());
        statement.setString(ResourceManager.parameterIndex2, book.getAuthor());
        statement.setFloat(ResourceManager.parameterIndex3, book.getPrice());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    /**
     * Get all books from database
     */
    public List<Book> listAllBooks() throws SQLException {
        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(ALL_BOOKS_QUERY);
        List<Book> listBook = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt(ResourceManager.ID);
            String title = resultSet.getString(ResourceManager.TITLE);
            String author = resultSet.getString(ResourceManager.AUTHOR);
            float price = resultSet.getFloat(ResourceManager.PRICE);

            Book book = new Book(id, title, author, price);
            listBook.add(book);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return listBook;
    }

    /**
     * Deleting a book in the database
     */
    public boolean deleteBook(Book book) throws SQLException {
        connect();
        statement = jdbcConnection.prepareStatement(DELETE_BOOK_QUERY);
        statement.setInt(ResourceManager.parameterIndex1, book.getId());

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }

    /**
     * Updating a book in the database
     */
    public boolean updateBook(Book book) throws SQLException {
        connect();
        statement = jdbcConnection.prepareStatement(UPDATE_BOOK_QUERY);
        statement.setString(ResourceManager.parameterIndex1, book.getTitle());
        statement.setString(ResourceManager.parameterIndex2, book.getAuthor());
        statement.setFloat(ResourceManager.parameterIndex3, book.getPrice());
        statement.setInt(ResourceManager.parameterIndex4, book.getId());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    /**
     * Get book from database
     */
    public Book getBook(Integer id) throws SQLException {
        connect();
        Book book = null;

        statement = jdbcConnection.prepareStatement(GET_BOOK_QUERY);
        statement.setInt(ResourceManager.parameterIndex1, id);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String title = resultSet.getString(ResourceManager.TITLE);
            String author = resultSet.getString(ResourceManager.AUTHOR);
            float price = resultSet.getFloat(ResourceManager.PRICE);
            book = new Book(id, title, author, price);
        }
        resultSet.close();
        statement.close();
        return book;
    }
}
