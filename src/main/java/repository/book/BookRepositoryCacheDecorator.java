package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {

    private Cache<Book> cache;
   // private Cache<Book> soldBooksCache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache=cache;
        //this.soldBooksCache=soldBooksCache;

    }
    @Override
    public List<Book> findAll() {

        if(cache.hasResult()){
            return cache.load();
        }

        List<Book> books=decoratedbookRepository.findAll(); //mergem pana in BAZA DE DATE
        cache.save(books); //incarc cache

        return books;
    }

    @Override
    public List<Book> findSoldBooks() {
//        if (soldBooksCache.hasResult()) {
//            return soldBooksCache.load();
//        }

        List<Book> soldBooks = decoratedbookRepository.findSoldBooks();
        //soldBooksCache.save(soldBooks);

        return soldBooks;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }
        return decoratedbookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedbookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedbookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedbookRepository.removeAll();

    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        return Optional.empty();
    }

    @Override
    public boolean sellBook(Book book) {
        cache.invalidateCache();
        //soldBooksCache.invalidateCache();
        return decoratedbookRepository.sellBook(book);
    }


}
