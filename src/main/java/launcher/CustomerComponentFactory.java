
package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class CustomerComponentFactory {

    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static CustomerComponentFactory instance;

    public synchronized static CustomerComponentFactory getInstance(Boolean componentsForTest, Stage stage){
        if (instance == null){
            instance = new CustomerComponentFactory(componentsForTest, stage);
        }
        return instance;
    }

    public CustomerComponentFactory(Boolean componentsForTest, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        List<BookDTO> soldBookDTOs=BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        this.bookView = new BookView(stage, bookDTOs, soldBookDTOs);
        this.bookController = new BookController(bookView, bookService);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public static CustomerComponentFactory getInstance() {
        return instance;
    }
}


