package advanced.ipt_library.controller;

import advanced.ipt_library.request.BookContractRequest;
import advanced.ipt_library.response.ApiResponse;
import advanced.ipt_library.service.BookContractService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("book_contracts")
@RequiredArgsConstructor
public class BookContractController {

    private final BookContractService bookContractService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get all records", bookContractService.getBookContracts()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody BookContractRequest request) throws ParseException {
        bookContractService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Record created", null));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> update(@RequestBody BookContractRequest request) throws ParseException {
        bookContractService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Record updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        bookContractService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Record deleted", null));
    }

    // import excel
    // lưu lại file để giữ phiên bản
    // lưu lại người import (username)
    // lưu lại thời gian import
    @PostMapping("/importexcel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        bookContractService.importExcel(file);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/exportexcel")
    public ResponseEntity<?> exportExcel(HttpServletResponse response) throws IOException {
        String fileName = "book_contracts_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        bookContractService.exportExcel(response.getOutputStream());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
