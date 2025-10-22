package advanced.ipt_library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

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

    @PrePersist
    public void onCreate() {
        this.creator = this.getUsername();
    }

    @PreUpdate
    public void onUpdate() {
        this.updater = this.getUsername();
    }

    private String getUsername() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
