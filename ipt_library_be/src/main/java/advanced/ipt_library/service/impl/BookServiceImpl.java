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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

        bookRepository.save(book);
    }

    @Override
    public void delete(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data));
        bookRepository.delete(book);
    }

    @Override
    public void exportPdf(OutputStream outputStream) throws JRException {
        // lấy 1 book bất kỳ
        Book book = bookRepository.findAll().stream().findFirst().orElse(null); // findFirst trả ra cho mình Optional<T>
        List<Contract> contracts = new ArrayList<>();
        if (Objects.nonNull(book)) {
            // xuất file
            String templatePath = "templates/jasper/demo.jrxml";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            Map<String, Object> parameters = new HashMap<>();
            // vì jasper report trả về các kiểu dữ liệu khác nhau nhưng cùng là dạng object
            // --> để dữ liệu là dạng Object, key là String
            parameters.put("description", book.getDescription()); // params bên jasper
            parameters.put("isbn", book.getIsbn()); // params bên jasper

            JRDataSource dataSource = new JRBeanCollectionDataSource(contracts);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            // jasperReport : file
            // parameters : biến được truyền vào
            // bình thường nếu truyền một list vào trong file thì dùng JRDataSource() ở biến thứ 3
            // nhưng nếu không truyền List<?> nào cả thì dùng JREmptyDataSource()
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }
    }
}
