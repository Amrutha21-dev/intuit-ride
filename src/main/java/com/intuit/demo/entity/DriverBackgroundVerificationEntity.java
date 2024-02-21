package com.intuit.demo.entity;

import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Getter
@Setter
@Table(name = "driver_background_verification")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
@AllArgsConstructor
@Builder
public class DriverBackgroundVerificationEntity extends BaseWithDateEntity{

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private DriverEntity driverEntity;

    @Column(name = "status")
    @Convert(converter = DriverBackgroundVerificationStatus.Converter.class)
    private DriverBackgroundVerificationStatus driverBackgroundVerificationStatus;

}
