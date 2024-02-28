package com.intuit.demo.entity;

import com.intuit.demo.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
@Builder
public class UserEntity extends BaseWithDateEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    @Convert(converter = UserStatus.Converter.class)
    private UserStatus userStatus;

}
