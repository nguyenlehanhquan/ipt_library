package advanced.ipt_library.response;

import advanced.ipt_library.entity.BookContract;
import lombok.Data;

import java.util.Date;

@Data
public class BookContractResponse {
    private int id;
    private String bookName;
    private String contractCode;
    private String orderNumber;
    private String customerCode;
//    private String sampleArchiveLocationShelf;
//    private String sampleArchiveLocationLocation;
    private String sampleShelfLocation;
    private int sampleQuantity;
    private String sampleRemark;

    private String receivedShelfLocation;
    private int receivedQuantity;
    private String receivedRemark;

    private String signedShelfLocation;
    private int signedQuantity;
    private String signedRemark;

    private Date deliveryDate;

    // ko dung model mapper

    public BookContractResponse(BookContract bookContract) {
        this.id = bookContract.getId();
        this.contractCode = bookContract.getContract().getCode();
        this.orderNumber = bookContract.getOrderNumber();
        this.deliveryDate = bookContract.getDeliveryDate();

        this.sampleShelfLocation = bookContract.getSampleArchiveLocation().getShelf()
                + "_" + bookContract.getSampleArchiveLocation().getLocation();
        this.sampleQuantity =  bookContract.getSampleQuantity();
        this.sampleRemark = bookContract.getSampleRemark();

        this.receivedShelfLocation = bookContract.getReceivedItemArchiveLocation().getShelf()
                + "_" + bookContract.getReceivedItemArchiveLocation().getLocation();
        this.receivedQuantity = bookContract.getReceivedItemQuantity();
        this.receivedRemark = bookContract.getReceivedItemRemark();

        this.signedShelfLocation = bookContract.getSignedSheetArchiveLocation().getShelf()
                + "_" + bookContract.getSignedSheetArchiveLocation().getLocation();
        this.signedQuantity = bookContract.getSignedSheetQuantity();
        this.signedRemark = bookContract.getSignedSheetRemark();

    }


}
