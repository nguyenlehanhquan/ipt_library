package advanced.ipt_library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "book_contract")
public class BookContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @JoinColumn(name = "contract_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "customer_code", nullable = false)
    private String customerNumber;

    @JoinColumn(name = "sample_archive_location_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Archive sampleArchiveLocation;

    @Column(name = "sample_quantity")
    private Integer sampleQuantity;

    @Column(name = "sample_remark")
    private String sampleRemark;

    @JoinColumn(name = "received_item_archive_location_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Archive receivedItemArchiveLocation;

    @Column(name = "received_item_quantity")
    private int receivedItemQuantity;

    @Column(name = "received_item_remark")
    private String receivedItemRemark;

    @JoinColumn(name = "signed_sheet_archive_location_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Archive signedSheetArchiveLocation;

    @Column(name = "signed_sheet_quantity")
    private Integer signedSheetQuantity;

    @Column(name = "signed_sheet_remark")
    private String signedSheetRemark;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    private Date deliveryDate;
}
