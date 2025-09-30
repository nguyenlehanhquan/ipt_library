package advanced.ipt_library.service.impl;

import advanced.ipt_library.entity.Book;
import advanced.ipt_library.entity.Contract;
import advanced.ipt_library.exception.BusinessException;
import advanced.ipt_library.exception.ErrorCodeConstant;
import advanced.ipt_library.exception.NotFoundException;
import advanced.ipt_library.repository.BookRepository;
import advanced.ipt_library.request.CreateBookRequest;
import advanced.ipt_library.request.UpdateBookRequest;
import advanced.ipt_library.response.BookResponse;
import advanced.ipt_library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    @Override
    public List<BookResponse> findAll() {
        List<Book> books = bookRepository.findAll();

        List<BookResponse> bookResponses = new ArrayList<>();

        for (Book book : books) {
            BookResponse bookResponse = mapper.map(book, BookResponse.class);
            bookResponses.add(bookResponse);
        }
        return bookResponses;
    }

    @Override
    public BookResponse findById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data));
        return mapper.map(book, BookResponse.class);
    }

    @Override
    public void create(CreateBookRequest request) {

        Book bookByIsbn = bookRepository.findByIsbnIgnoreCase(request.getIsbn());
        if (bookByIsbn != null) {
            throw new BusinessException("Book already exists", ErrorCodeConstant.existing_data);
        }

        Book book = new Book();

        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setAuthor(request.getAuthor());

        bookRepository.save(book);
    }

    @Override
    public void update(Integer id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data));

        Book bookByIsbn = bookRepository.findByIsbnIgnoreCase(request.getIsbn());
        if (bookByIsbn != null && !bookByIsbn.getId().equals(id)) {
            throw new BusinessException("Book already exists", ErrorCodeConstant.existing_data);
        }

        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setAuthor(request.getAuthor());

        bookRepository.save(book);
    }

    @Override
    public void delete(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data));
        bookRepository.delete(book);
    }


}
