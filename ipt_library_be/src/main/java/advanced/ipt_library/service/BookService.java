package advanced.ipt_library.service;

import advanced.ipt_library.request.CreateBookRequest;
import advanced.ipt_library.request.UpdateBookRequest;
import advanced.ipt_library.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> findAll();
    BookResponse findById(Integer id);
    void create(CreateBookRequest request);
    void update(Integer id, UpdateBookRequest request);
    void delete(Integer id);
}
