package com.webproject.javaservlets.controller;

import com.webproject.javaservlets.dao.BookDao;
import com.webproject.javaservlets.model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    private BookDao bookDAO;

    private final String ID = "id";
    private final String BOOK = "book";
    private final String TITLE = "title";
    private final String AUTHOR = "author";
    private final String PRICE = "price";
    private final String LIST = "list";
    private final String BOOK_LIST = "bookList";

    private final String NEW = "/new";
    private final String INSERT = "/insert";
    private final String DELETE = "/delete";
    private final String EDIT = "/edit";
    private final String UPDATE = "/update";

    /**
     * Initializing the database
     */
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        bookDAO = new BookDao(jdbcURL, jdbcUsername, jdbcPassword);
    }

    /**
     * Post request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case NEW:
                    showNewForm(request, response);
                    break;
                case INSERT:
                    insertBook(request, response);
                    break;
                case DELETE:
                    deleteBook(request, response);
                    break;
                case EDIT:
                    showEditForm(request, response);
                    break;
                case UPDATE:
                    updateBook(request, response);
                    break;
                default:
                    listBook(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Displaying a list of all books
     */
    private void listBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Book> bookList = bookDAO.listAllBooks();
        request.setAttribute(BOOK_LIST, bookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Displaying the form for adding a book
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Displaying a form to change a book data
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter(ID));
        Book existingBook = bookDAO.getBook(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        request.setAttribute(BOOK, existingBook);
        dispatcher.forward(request, response);

    }

    /**
     * Adding a new book
     */
    private void insertBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String title = request.getParameter(TITLE);
        String author = request.getParameter(AUTHOR);
        float price = Float.parseFloat(request.getParameter(PRICE));

        Book newBook = new Book(title, author, price);
        bookDAO.insertBook(newBook);
        response.sendRedirect(LIST);
    }

    /**
     * Updating book data
     */
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter(ID));
        String title = request.getParameter(TITLE);
        String author = request.getParameter(AUTHOR);
        float price = Float.parseFloat(request.getParameter(PRICE));

        Book book = new Book(id, title, author, price);
        bookDAO.updateBook(book);
        response.sendRedirect(LIST);
    }

    /**
     * Removing a book from the database
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter(ID));

        Book book = new Book(id);
        bookDAO.deleteBook(book);
        response.sendRedirect(LIST);
    }
}
