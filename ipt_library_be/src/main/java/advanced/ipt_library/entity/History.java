package advanced.ipt_library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "histories")
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object")
    private String object;

    @Column(name = "action")
    private String action;

    @Column(name = "data_before")
    private String dataBefore;

    @Column(name = "data_after")
    private String dataAfter;

    @Column(name = "description")
    private String description;

    @Column(name = "user_login")
    private String userLogin;

}
