package advanced.ipt_library.service;

import advanced.ipt_library.request.BookContractRequest;
import advanced.ipt_library.response.BookContractResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

public interface BookContractService {

    // findAll
    List<BookContractResponse> getBookContracts();

    // create
    void create(BookContractRequest request) throws ParseException;

    // update
    void update(BookContractRequest bookContractRequest) throws ParseException;

    // delete
    void delete(int id);

    // import excel
    void importExcel(MultipartFile file);

    void exportExcel(OutputStream outputStream) throws IOException;
}
