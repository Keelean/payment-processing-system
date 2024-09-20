package com.keelean.paymentprocessor.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AbstractBasedAuditedEntity extends AbstractBaseEntity {
    @Column(length = 50, updatable = false)
    @CreatedBy
    private String createdBy;
    @Column(updatable = false)
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:SS")
    private LocalDateTime createdDate;

    @Column(length = 50, updatable = false)
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(updatable = false)
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:SS")
    private LocalDateTime lastModifiedDate;


    @PrePersist
    public void setCreatedAndModifiedBy() {
        if (createdBy == null) {
            this.createdBy = "System";
        }

        if (this.lastModifiedBy == null) {
            this.lastModifiedBy = "System";
        }

        if(this.createdDate == null){
            this.createdDate = LocalDateTime.now();
        }

        if(this.lastModifiedDate == null){
            this.lastModifiedDate = LocalDateTime.now();
        }
    }
}
