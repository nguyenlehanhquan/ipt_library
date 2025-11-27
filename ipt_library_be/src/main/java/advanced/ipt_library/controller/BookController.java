package advanced.ipt_library.controller;

import advanced.ipt_library.request.CreateBookRequest;
import advanced.ipt_library.request.UpdateBookRequest;
import advanced.ipt_library.response.ApiResponse;
import advanced.ipt_library.response.BookResponse;
import advanced.ipt_library.service.BookService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get all books", bookService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        BookResponse book = bookService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Book got by id", book));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateBookRequest request) {
        bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Book created", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateBookRequest request) {
        bookService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Book updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
        bookService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Book deleted", null));
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportPdf(HttpServletResponse response) throws JRException, IOException {
        String fileName = "book_contracts_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        bookService.exportPdf(response.getOutputStream());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
