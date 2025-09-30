package advanced.ipt_library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//

    @Column(name = "isbn", unique = true, length = 15)
    // isbn varchar(15) not null unique, java sẽ tự tạo bảng cho mình nếu nó không thấy có bảng trong SQL
    private String isbn;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    // độ dài mặc định của String đã là 255 rồi nên không nên cần để là length = 255 . columnDefinition = "TEXT" thì sẽ giúp mình viết text vô hạn
    private String description;

    @Column(name = "author")
    private String author;

//    @Column(name = "name", length = 255)
//    private String name;

//    @OneToMany(mappedBy = "book")
//    private List<BookContract> bookContracts;

}
