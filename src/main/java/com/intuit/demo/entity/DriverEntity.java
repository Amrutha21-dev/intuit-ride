package com.intuit.demo.entity;

import com.intuit.demo.enumeration.DriverStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Getter
@Setter
@Table(name = "driver")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
@AllArgsConstructor
@Builder
public class DriverEntity extends BaseWithDateEntity{

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "adhaar_number")
    private String adhaarNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "driving_license_number")
    private String drivingLicenseNumber;

    @Column(name = "formatted address")
    private String formattedAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "state")
    private String state;

    @Column(name = "status")
    @Convert(converter = DriverStatus.Converter.class)
    private DriverStatus driverStatus;

}
