package advanced.ipt_library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "archives") // vị trí lưu trữ
public class Archive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "archive_type")
    private ArchiveType archiveType;
    // có 3 loại EnumType.String

    @Column(nullable = false, unique = true, length = 127)
    private String shelf;

    @Column(nullable = false, unique = true, length = 127)
    private String location;
}
