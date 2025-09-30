package advanced.ipt_library.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class BookContractRequest {

    private int id;
    private String orderNumber;
    private String customerNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String deliveryDate;

    private Integer bookId;
    private Integer contractId;

    private Integer sampleArchiveLocationId;
    private int sampleQuantity;
    private String sampleRemark;

    private Integer receivedItemArchiveLocationId;
    private int receivedItemQuantity;
    private String receivedItemRemark;

    private Integer signedSheetArchiveLocationId;
    private int signedSheetQuantity;
    private String signedSheetRemark;
}
