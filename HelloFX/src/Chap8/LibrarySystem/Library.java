package Chap8.LibrarySystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {
    private List<Book> collection = new ArrayList<>();

    public void addBook(Book book) {
        collection.add(book);
    }

    public int getBookCount() {
        return collection.size();
    }

    // Tries to find a book by title
    public Optional<Book> findBook(String title) {
        return collection.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    public void borrowBook(String title) {
        Optional<Book> bookOpt = findBook(title);

        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found in library");
        }

        Book book = bookOpt.get();

        if (book.isBorrowed()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        book.setBorrowed(true);
    }

    public void returnBook(String title) {
        Optional<Book> bookOpt = findBook(title);

        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found in library");
        }

        Book book = bookOpt.get();
        book.setBorrowed(false);
    }
}