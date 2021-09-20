package com.webproject.javaservlets.manager;

public class ResourceManager {

    /**
     * Constant variables
     */
    public static final String ID = "id";
    public static final String BOOK = "book";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String PRICE = "price";
    public static final String LIST = "list";
    public static final String BOOK_LIST = "bookList";

    /**
     * Routes
     */
    public static final String NEW = "/new";
    public static final String INSERT = "/insert";
    public static final String DELETE = "/delete";
    public static final String EDIT = "/edit";
    public static final String UPDATE = "/update";

    /**
     * Views
     */
    public static final String BOOK_LIST_VIEW = "BookList.jsp";
    public static final String BOOK_FORM_VIEW = "BookForm.jsp";
    public static final String ERROR = "Error.jsp";

    /**
     * Parameter Index
     */
    public static final Integer parameterIndex1 = 1;
    public static final Integer parameterIndex2 = 2;
    public static final Integer parameterIndex3 = 3;
    public static final Integer parameterIndex4 = 4;
}
