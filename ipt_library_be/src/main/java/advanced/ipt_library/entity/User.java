package advanced.ipt_library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)   // để cái này = true thì khi dùng equals và hashcode thì nó sẽ tính cả các fields của BaseEntity (class cha) vào trong phép so sánh
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;         // vì sao lại để int ở đây? sao không dùng Integer?

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}
