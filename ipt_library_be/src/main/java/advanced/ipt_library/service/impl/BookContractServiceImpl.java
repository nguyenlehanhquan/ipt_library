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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
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

        // check tồn tại không?
        // contractId
        // sampleId
        // receivedItemId
        // signedSheetId

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
                .collect(Collectors.toMap(contract -> contract.getCode(), ct -> ct));

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

        // Lấy all bookContract -> list key (A_B_E_F) đã tồn tại trong DB
        List<BookContract> currentBookContracts = bookContractRepository.findAll();
        // map<String, bookContract> ở DB --> key(A_B_E_F), value: bookContract
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<String, BookContract> mapBookContractByKey = currentBookContracts.stream()
                .collect(Collectors.toMap(
                        bc ->
                                bc.getContract().getCode() + "_"
                                        + bc.getOrderNumber() + "_"
                                        + (Objects.nonNull(bc.getDeliveryDate()) ? sdf.format(bc.getDeliveryDate()) : StringUtils.EMPTY) + "_"
                                        + bc.getBook().getIsbn(), bookContract -> bookContract));
        // trong for
        // String key = A_B_E_F lấy từ trong excel

        DataFormatter formatter = new DataFormatter();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream()); // lấy được file excel ra
            Sheet sheet = workbook.getSheetAt(0); // lấy được sheet đầu tiên

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // bỏ qua hàng nang có index = 0 vì đây là hàm tiêu đề
                    continue;
                }
                if (StringUtils.isEmpty(row.getCell(2).getStringCellValue())) { // để tránh nó đọc vào hàng rỗng và vẫn đọc --> lỗi
                    break;
                }
                // đọc dữ liệu - lấy ra tất cả thông tin của file excel vừa được import vào
                String contractCode = row.getCell(0).getStringCellValue(); // A
                String orderNumber = formatter.formatCellValue(row.getCell(1)); // B
                String description = row.getCell(2).getStringCellValue(); // C
                String customerCode = row.getCell(3).getStringCellValue(); // D
                Date deliveryDate = row.getCell(4).getDateCellValue(); // E chua biet hien thi duoc hay khong
                String isbn = formatter.formatCellValue(row.getCell(5)); // F - isbn

                // đọc dữ liệu - lấy ra tất cả thông tin vị trí từng record của file excel vừa được import vào
                String sampleShelf = Objects.nonNull(row.getCell(6)) ? String.valueOf(row.getCell(6)) : null; // G - chỉ kiểm tra được null, không bắt được "" hay " "
                String sampleLocation = Objects.nonNull(row.getCell(7)) ? formatter.formatCellValue(row.getCell(7)) : null; // H
                String sampleQuantity = Objects.nonNull(row.getCell(8)) ? formatter.formatCellValue(row.getCell(8)) : null; // I
                String sampleRemark = Objects.nonNull(row.getCell(9)) ? String.valueOf(row.getCell(9)) : null; // J

                String receivedItemShelf = Objects.nonNull(row.getCell(10)) ? String.valueOf(row.getCell(10)) : null; // K
                String receivedItemLocation = Objects.nonNull(row.getCell(11)) ? formatter.formatCellValue(row.getCell(11)) : null; // L
                String receivedItemQuantity = Objects.nonNull(row.getCell(12)) ? formatter.formatCellValue(row.getCell(12)) : null; // M
                String receivedItemRemark = Objects.nonNull(row.getCell(13)) ? String.valueOf(row.getCell(13)) : null; // N

                String signedSheetShelf = Objects.nonNull(row.getCell(14)) ? String.valueOf(row.getCell(14)) : null; // O
                String signedSheetLocation = Objects.nonNull(row.getCell(15)) ? formatter.formatCellValue(row.getCell(15)) : null; // P
                String signedSheetQuantity = Objects.nonNull(row.getCell(16)) ? formatter.formatCellValue(row.getCell(16)) : null; // Q
                String signedSheetRemark = Objects.nonNull(row.getCell(17)) ? String.valueOf(row.getCell(17)) : null; // R

                String key = contractCode + "_" + orderNumber + "_" + (Objects.nonNull(deliveryDate) ? sdf.format(deliveryDate) : StringUtils.EMPTY) + "_" + isbn;

                BookContract bookContract;
                if (mapBookContractByKey.containsKey(key)) {
                    // lấy bookContract đã tồn tại - nếu key đã tồn tại trong list currentBookContract rồi thì ...
                    bookContract = mapBookContractByKey.get(key); // lấy ra book contract đã tồn tại đó
                } else {
                    // nếu cái key (contractCode_orderNumber_deliveryDate_ISBN) chưa tồn tại trong list currentBookContracts thì tạo mới
                    // tạo bookcontract mới, lưu bookcontract
                    bookContract = new BookContract();
                    bookContract.setOrderNumber(orderNumber); // B
                    bookContract.setDeliveryDate(deliveryDate); // E

                    if (mapBookByCode.containsKey(isbn)) { // C, F - xem xem ISBN của cuốn sách tồn tại trong hệ thống chưa?
                        Book book = mapBookByCode.get(isbn);
                        bookContract.setBook(book); // nếu rồi setBook của bookContract mới tạo là cuốn sách đó
                    } else {
                        Book book = new Book(); // nếu chưa có sách thì tạo mới book và set mới isbn cho cuốn sách đó
                        book.setIsbn(isbn);
                        book.setDescription(description);

                        newBooks.add(book); // thêm cuốn sách mới này vào list book mới được thêm
                        bookContract.setBook(book); // setBook này là book của record bookContract mới tạo
                        mapBookByCode.put(isbn, book); // thêm vào map các cuốn sách đang có hiện tại để sau có thể dùng để check lại
                    }

                    if (mapContractByCode.containsKey(contractCode)) { // A - xem xem mã hợp đồng tồn tại trong hệ thông chưa?
                        Contract contract = mapContractByCode.get(contractCode);
                        bookContract.setContract(contract); // nếu rồi thì setContract của bookContract mới tạo là cuốn sách đó
                    } else {
                        Contract contract = new Contract(); // nếu chưa có mã hợp đồng nào như này thì tạo mới hợp đồng
                        contract.setCode(contractCode); // set contractCode của contract mới là contractCode mới được thêm vào

                        newContracts.add(contract); // thêm contract này vào list các contract mới có
                        bookContract.setContract(contract); // set contract mớid được thêm vào này là contract trong bookContract
                        mapContractByCode.put(contractCode, contract); // thêm vào map các hợp đồng đang có hiện tại để sau có gì thì lôi ra check xem tồn tại chưa
                    }
                }

                bookContract.setCustomerNumber(customerCode); // D dù check có tồn tại hay không thì vẫn update

                bookContract.setSampleRemark(sampleRemark); // set remark sách mẫu
                if (StringUtils.isNotEmpty(sampleShelf)) { // nếu sampleSelf không null và không phải là "" trong file excel thì
                    bookContract.setSampleQuantity(Double.valueOf(sampleQuantity).intValue()); // set sampleQuantity cho bookContract được tạo mới
                    if (mapSampleArchiveByShelf.containsKey(sampleShelf)) { // nếu SampleArchive có tồn tại rồi thì
                        Archive sampleArchive = mapSampleArchiveByShelf.get(sampleShelf); // lấy ra cái sampleArchive đã tồn tại đó trong map currentArchive
                        bookContract.setSampleArchiveLocation(sampleArchive); // lưu map đó vào thằng bookcontract được tạo mới
                    } else {
                        Archive sampleArchive = new Archive();  // nếu chưa có sample archive nào cả thì tạo sampleArchive mới đó
                        sampleArchive.setArchiveType(ArchiveType.SAMPLE); // set type của archive bằng SAMPLE
                        sampleArchive.setShelf(sampleShelf); // set Shelf của sample là sampleShelf vừa lấy được từ excel
                        sampleArchive.setLocation(sampleLocation); // set Location của sample là sampleLocation vừa lấy được từ excel

                        newArchives.add(sampleArchive); // thêm cái archive này vào list các archive mới có
                        bookContract.setSampleArchiveLocation(sampleArchive); // set cái archive này là Sample Archive Location cho cái bookContract mới tạo
                        mapSampleArchiveByShelf.put(sampleShelf, sampleArchive); // thêm cái sampleShelf và vị trị này vào list map các sampleShelf đã tồn tại

                        // vì sao ngay từ ban đầu không tạo chỉ 1 list Archives thôi mà phải phân chia ra thành 3 list current Archive riêng biệt ?
                    }
                } else {
                    bookContract.setSampleArchiveLocation(null);
                    bookContract.setSampleQuantity(null);
                }

                bookContract.setReceivedItemRemark(receivedItemRemark); // set remark túi công việc đã nhận
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
                } else {
                    bookContract.setReceivedItemArchiveLocation(null);
                    bookContract.setReceivedItemQuantity(null);
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
                } else {
                    bookContract.setSignedSheetArchiveLocation(null);
                    bookContract.setSignedSheetQuantity(null);
                }

                newBookContracts.add(bookContract);
            }

            // Phải lưu theo thứ tự này
            bookRepository.saveAll(newBooks); // lưu tất cả các book mới thêm vào Databse
            contractsRepository.saveAll(newContracts); // lưu tất cả các contract mới thêm vào Database
            archiveRepository.saveAll(newArchives); // lưu tất cả các archive mới thêm vào Database
            bookContractRepository.saveAll(newBookContracts); // lưu tất cả các bookContract mới thêm vào Database

            // khuyến khích dùng String.valueOf() thay vì toString()

        } catch (Exception e) {
            e.printStackTrace(); // in ra lỗi
        }
    }

    @Override
    public void exportExcel(OutputStream os) {
        // xuất được một file excel trắng
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("book_contracts");

            // lưu các thông tin vào trong các cell
            workbook.write(os);
            os.flush();
            workbook.close();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// bỏ not null trong entity và database
// data formatter vào các trường dễ lỗi string
// thêm else vào trường hợp chạy vào "" (blank)
// postman gọi api - POST - body + param "file" - value: excel file
