package com.intuit.demo.entity;

import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import com.intuit.demo.enumeration.DriverRideStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Getter
@Setter
@Table(name = "driver_ride_status")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
@AllArgsConstructor
@Builder
public class DriverRideStatusEntity extends BaseWithDateEntity{

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private DriverEntity driverEntity;

    @Column(name = "status")
    @Convert(converter = DriverRideStatus.Converter.class)
    private DriverRideStatus driverRideStatus;

}
