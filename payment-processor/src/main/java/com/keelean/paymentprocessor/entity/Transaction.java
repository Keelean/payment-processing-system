package com.keelean.paymentprocessor.entity;

import com.keelean.paymentprocessor.constants.TxnStatus;
import com.keelean.paymentprocessor.entity.base.AbstractBasedAuditedEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Table(name = "transaction", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Transaction extends AbstractBasedAuditedEntity {

    @Column
    private BigDecimal amount;
    @Column
    @Enumerated(EnumType.STRING)
    private TxnStatus status;
}
