package com.intuit.demo.entity;

import com.intuit.demo.enumeration.DriverRideStatus;
import com.intuit.demo.enumeration.UserType;
import com.intuit.demo.enumeration.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Setter
@Getter
@Entity
@Table(name = "driver_vehicle")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
@Builder
@AllArgsConstructor
public class DriverVehicleEntity extends BaseWithDateEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private DriverEntity driverEntity;

    @Column(name = "vehicle_type")
    @Convert(converter = VehicleType.Converter.class)
    private VehicleType vehicleType;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "chassis_number")
    private String chassisNumber;

    @Column(name = "model")
    private String model;

}
