package advanced.ipt_library.service.impl;

import advanced.ipt_library.entity.*;
import advanced.ipt_library.exception.ErrorCodeConstant;
import advanced.ipt_library.exception.NotFoundException;
import advanced.ipt_library.repository.ArchiveRepository;
import advanced.ipt_library.repository.BookContractRepository;
import advanced.ipt_library.repository.BookRepository;
import advanced.ipt_library.repository.ContractRepository;
import advanced.ipt_library.request.BookContractRequest;
import advanced.ipt_library.response.BookContractResponse;
import advanced.ipt_library.service.BookContractService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookContractServiceImpl implements BookContractService {

    private final BookContractRepository bookContractRepository;
    private final BookRepository bookRepository;
    private final ContractRepository contractsRepository;
    private final ArchiveRepository archiveRepository;

    @Override
    public List<BookContractResponse> getBookContracts() {
        List<BookContract> bookContracts = bookContractRepository.findAll();
        List<BookContractResponse> bookContractResponses = new ArrayList<>();

        for (BookContract bookContract : bookContracts) {
            bookContractResponses.add(new BookContractResponse(bookContract));
        }
//        List<BookContractResponse> bookContractResponses = bookContracts.stream().map(b -> new BookContractResponse(b)).collect(Collectors.toList());
        return bookContractResponses;
    }

    @Override
    public void create(BookContractRequest bookContractRequest) throws ParseException {
        // bookContractRequest -> entity BookContract -> save
        Optional<Book> optionalBook = bookRepository.findById(bookContractRequest.getBookId());

        // BookId -> Book
        if (optionalBook.isEmpty()) {
            BookContract bookContract = new BookContract();

            bookContract.setOrderNumber(bookContractRequest.getOrderNumber());
            bookContract.setCustomerNumber(bookContractRequest.getCustomerNumber());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date deliveryDate = formatter.parse(bookContractRequest.getDeliveryDate());
            bookContract.setDeliveryDate(deliveryDate);

            bookContract.setSampleQuantity(bookContractRequest.getSampleQuantity());
            bookContract.setSampleRemark(bookContractRequest.getSampleRemark());

            bookContract.setReceivedItemQuantity(bookContractRequest.getReceivedItemQuantity());
            bookContract.setReceivedItemRemark(bookContractRequest.getReceivedItemRemark());

            bookContract.setSignedSheetQuantity(bookContractRequest.getSignedSheetQuantity());
            bookContract.setSignedSheetRemark(bookContractRequest.getSignedSheetRemark());

            bookContractRepository.save(bookContract);

        } else {
            throw new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data);
        }
    }

    @Override
    public void update(BookContractRequest bookContractRequest) throws ParseException {

        BookContract bookContract = bookContractRepository.findById(bookContractRequest.getId()).orElseThrow(() -> new NotFoundException("Book contract does not exist", ErrorCodeConstant.no_existing_data));

        Optional<Book> optionalBook = bookRepository.findById(bookContractRequest.getBookId());
        if (optionalBook.isPresent()) {
            bookContract.setBook(optionalBook.get());
        } else {
            throw new NotFoundException("Book does not exist", ErrorCodeConstant.no_existing_data);
        }

        bookContract.setOrderNumber(bookContractRequest.getOrderNumber());
        bookContract.setCustomerNumber(bookContractRequest.getCustomerNumber());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveryDate = formatter.parse(bookContractRequest.getDeliveryDate());
        bookContract.setDeliveryDate(deliveryDate);

        bookContract.setSampleQuantity(bookContractRequest.getSampleQuantity());
        bookContract.setSampleRemark(bookContractRequest.getSampleRemark());

        bookContract.setReceivedItemQuantity(bookContractRequest.getReceivedItemQuantity());
        bookContract.setReceivedItemRemark(bookContractRequest.getReceivedItemRemark());

        bookContract.setSignedSheetQuantity(bookContractRequest.getSignedSheetQuantity());
    }

    @Override
    public void delete(int id) {
        BookContract bookContract = bookContractRepository.findById(id).orElseThrow(() -> new NotFoundException("Book contract does not exist", ErrorCodeConstant.no_existing_data));
        bookContractRepository.delete(bookContract);
    }

    @Override
    public void importExcel(MultipartFile file) {
        // lưu lại cái gì
        List<BookContract> newBookContracts = new ArrayList<>();
        List<Book> newBooks = new ArrayList<>(); // list books sẽ được tạo mới
        List<Contract> newContracts = new ArrayList<>(); //
        List<Archive> newArchives = new ArrayList<>();

        List<Book> currentBooks = bookRepository.findAll(); // lấy ra tất cả list book hiện tại
        Map<String, Book> mapBookByCode = currentBooks.stream()
                .collect(Collectors.toMap(book -> book.getIsbn(), book -> book)); //key: isbn, value: book tuong ung --> map book theo code để sau check

        List<Contract> currentContracts = contractsRepository.findAll();
        Map<String, Contract> mapContractByCode = currentContracts.stream()
                .collect(Collectors.toMap(contract -> contract.getCode(), ct  -> ct));

//        currentContracts.stream().filter(i -> i.getCode().equals("abc")).toList();
//        for (Contract contract : currentContracts) {
//            if (contract.getCode().equals("abc")) {
//                css.add(contract)
//            }
//        }

//        Workbook workbook;
//        Sheet sheet;
//        Row row;
        // Column column : có thể lặp theo cột nhưng ít khi dùng

        // vị trí lưu trữ
        List<Archive> currentArchives = archiveRepository.findAll();
        Map<String, Archive> mapSampleArchiveByShelf = currentArchives.stream()
                .filter(ar -> ar.getArchiveType() == ArchiveType.SAMPLE)
                .collect(Collectors.toMap(archive -> archive.getShelf(), archive -> archive));

        Map<String, Archive> mapReceivedItemArchiveByShelf = currentArchives.stream()
                .filter(ar -> ar.getArchiveType() == ArchiveType.RECEIVED_ITEM)
                .collect(Collectors.toMap(archive -> archive.getShelf(), archive -> archive));

        Map<String, Archive> mapArchiveBySignedSheet = currentArchives.stream()
                .filter(ar -> ar.getArchiveType() == ArchiveType.SIGNED_SHEET)
                .collect(Collectors.toMap(archive -> archive.getShelf(), archive -> archive));

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream()); // lấy được file excel ra
            Sheet sheet = workbook.getSheetAt(0); // lấy được sheet đầu tiên

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                // đọc dữ liệu
                String contractCode = row.getCell(0).getStringCellValue(); // A
                String orderNumber = row.getCell(1).getStringCellValue(); // B
                String description = row.getCell(2).getStringCellValue(); // C
                String customerCode = row.getCell(3).getStringCellValue(); // D
                Date deliveryDate = row.getCell(4).getDateCellValue(); // E chua biet hien thi duoc hay khong
                String isbn = row.getCell(5).getStringCellValue(); // F - isbn

                // vi tri
                String sampleShelf = Objects.nonNull(row.getCell(6)) ? String.valueOf(row.getCell(6)) : null; // G
                String sampleLocation = Objects.nonNull(row.getCell(7)) ? String.valueOf(row.getCell(7)) : null; // H
                String sampleQuantity = Objects.nonNull(row.getCell(8)) ? String.valueOf(row.getCell(8)) : null; // I
                String sampleRemark = Objects.nonNull(row.getCell(9)) ? String.valueOf(row.getCell(9)) : null; // J

                String receivedItemShelf = Objects.nonNull(row.getCell(10)) ? String.valueOf(row.getCell(10)) : null; // K
                String receivedItemLocation = Objects.nonNull(row.getCell(11)) ? String.valueOf(row.getCell(11)) : null; // L
                String receivedItemQuantity = Objects.nonNull(row.getCell(12)) ? String.valueOf(row.getCell(12)) : null; // M
                String receivedItemRemark = Objects.nonNull(row.getCell(13)) ? String.valueOf(row.getCell(13)) : null; // N

                String signedSheetShelf = Objects.nonNull(row.getCell(14)) ? String.valueOf(row.getCell(14)) : null; // O
                String signedSheetLocation = Objects.nonNull(row.getCell(15)) ? String.valueOf(row.getCell(15)) : null; // P
                String signedSheetQuantity = Objects.nonNull(row.getCell(16)) ? String.valueOf(row.getCell(16)) : null; // Q
                String signedSheetRemark = Objects.nonNull(row.getCell(17)) ? String.valueOf(row.getCell(17)) : null; // R0

                // tìm xem sách tồn tại chưa

                // tìm xem vị trị lưu tồn tại chưa

                // tạo bookcontract, lưu bookcontract
                BookContract bookContract = new BookContract(); // tạo đối tượng, chưa lưu vào DB
                bookContract.setOrderNumber(orderNumber);
                bookContract.setCustomerNumber(customerCode);
                bookContract.setDeliveryDate(deliveryDate);

                if (mapBookByCode.containsKey(isbn)) {
                    Book book = mapBookByCode.get(isbn);
                    bookContract.setBook(book);
                } else {
                    Book book = new Book();
                    book.setIsbn(isbn);
                    book.setDescription(description);

                    newBooks.add(book);
                    bookContract.setBook(book);
                    mapBookByCode.put(isbn, book);
                }

                if (mapContractByCode.containsKey(contractCode)) {
                    Contract contract = mapContractByCode.get(contractCode);
                    bookContract.setContract(contract);
                } else {
                    Contract contract = new Contract();
                    contract.setCode(contractCode);

                    newContracts.add(contract);
                    bookContract.setContract(contract);
                    mapContractByCode.put(contractCode, contract);
                }


                bookContract.setSampleRemark(sampleRemark);
                if (StringUtils.isNotEmpty(sampleShelf)) {
                    bookContract.setSampleQuantity(Double.valueOf(sampleQuantity).intValue());
                    if (mapSampleArchiveByShelf.containsKey(sampleShelf)) {
                        Archive sampleArchive = mapSampleArchiveByShelf.get(sampleShelf);
                        bookContract.setSampleArchiveLocation(sampleArchive);
                    } else {
                        Archive sampleArchive = new Archive();
                        sampleArchive.setArchiveType(ArchiveType.SAMPLE);
                        sampleArchive.setShelf(sampleShelf);
                        sampleArchive.setLocation(sampleLocation);

                        newArchives.add(sampleArchive);
                        bookContract.setSampleArchiveLocation(sampleArchive);
                        mapSampleArchiveByShelf.put(sampleShelf, sampleArchive);
                    }

                }

                bookContract.setSignedSheetRemark(receivedItemRemark);
                if (StringUtils.isNotEmpty(receivedItemShelf)) {
                    bookContract.setReceivedItemQuantity(Double.valueOf(receivedItemQuantity).intValue());
                    if (mapReceivedItemArchiveByShelf.containsKey(receivedItemShelf)) {
                        Archive receivedItemArchive = mapReceivedItemArchiveByShelf.get(receivedItemShelf);
                        bookContract.setReceivedItemArchiveLocation(receivedItemArchive);
                    } else {
                        Archive receivedItemArchive = new Archive();
                        receivedItemArchive.setArchiveType(ArchiveType.RECEIVED_ITEM);
                        receivedItemArchive.setShelf(receivedItemShelf);
                        receivedItemArchive.setLocation(receivedItemLocation);

                        newArchives.add(receivedItemArchive);
                        bookContract.setReceivedItemArchiveLocation(receivedItemArchive);
                        mapReceivedItemArchiveByShelf.put(receivedItemShelf, receivedItemArchive);
                    }
                }

                bookContract.setSignedSheetRemark(signedSheetRemark);
                if (StringUtils.isNotEmpty(signedSheetShelf)) {
                    bookContract.setSignedSheetQuantity(Double.valueOf(signedSheetQuantity).intValue());
                    if (mapArchiveBySignedSheet.containsKey(signedSheetShelf)) {
                        Archive newSampleArchive = mapArchiveBySignedSheet.get(signedSheetShelf);
                        bookContract.setSignedSheetArchiveLocation(newSampleArchive);
                    } else {
                        Archive newSampleArchive = new Archive();
                        newSampleArchive.setArchiveType(ArchiveType.SIGNED_SHEET);
                        newSampleArchive.setShelf(signedSheetShelf);
                        newSampleArchive.setLocation(signedSheetLocation);

                        newArchives.add(newSampleArchive);
                        bookContract.setSignedSheetArchiveLocation(newSampleArchive);
                        mapArchiveBySignedSheet.put(signedSheetShelf, newSampleArchive);
                    }
                }
            }

            // Phải lưu theo thứ tự này
            bookRepository.saveAll(newBooks);
            contractsRepository.saveAll(newContracts);
            archiveRepository.saveAll(newArchives);
            bookContractRepository.saveAll(newBookContracts);




        } catch (Exception e) {
            e.printStackTrace(); // in ra lỗi
        }
    }
}
