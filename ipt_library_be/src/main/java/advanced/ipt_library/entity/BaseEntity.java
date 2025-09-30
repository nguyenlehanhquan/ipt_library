package advanced.ipt_library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Date;

@Data
@MappedSuperclass   //
public abstract class BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Column(name = "creator", length = 50)
    @CreatedBy
    private String creator;//abc

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated")
    private Date updated;

    @Column(name = "updater", length = 50)
    @LastModifiedBy
    private String updater;
}
