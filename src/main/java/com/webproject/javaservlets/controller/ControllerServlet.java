package com.webproject.javaservlets.controller;

import com.webproject.javaservlets.dao.BookDao;
import com.webproject.javaservlets.manager.ResourceManager;
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
                case ResourceManager.NEW:
                    showNewForm(request, response);
                    break;
                case ResourceManager.INSERT:
                    insertBook(request, response);
                    break;
                case ResourceManager.DELETE:
                    deleteBook(request, response);
                    break;
                case ResourceManager.EDIT:
                    showEditForm(request, response);
                    break;
                case ResourceManager.UPDATE:
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
        request.setAttribute(ResourceManager.BOOK_LIST, bookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher(ResourceManager.BOOK_LIST_VIEW);
        dispatcher.forward(request, response);
    }

    /**
     * Displaying the form for adding a book
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ResourceManager.BOOK_FORM_VIEW);
        dispatcher.forward(request, response);
    }

    /**
     * Displaying a form to change a book data
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter(ResourceManager.ID));
        Book existingBook = bookDAO.getBook(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher(ResourceManager.BOOK_LIST_VIEW);
        request.setAttribute(ResourceManager.BOOK, existingBook);
        dispatcher.forward(request, response);
    }

    /**
     * Adding a new book
     */
    private void insertBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String title = request.getParameter(ResourceManager.TITLE);
        String author = request.getParameter(ResourceManager.AUTHOR);
        float price = Float.parseFloat(request.getParameter(ResourceManager.PRICE));

        Book newBook = new Book(title, author, price);
        bookDAO.insertBook(newBook);
        response.sendRedirect(ResourceManager.LIST);
    }

    /**
     * Updating book data
     */
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter(ResourceManager.ID));
        String title = request.getParameter(ResourceManager.TITLE);
        String author = request.getParameter(ResourceManager.AUTHOR);
        float price = Float.parseFloat(request.getParameter(ResourceManager.PRICE));

        Book book = new Book(id, title, author, price);
        bookDAO.updateBook(book);
        response.sendRedirect(ResourceManager.LIST);
    }

    /**
     * Removing a book from the database
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter(ResourceManager.ID));

        Book book = new Book(id);
        bookDAO.deleteBook(book);
        response.sendRedirect(ResourceManager.LIST);
    }
}
