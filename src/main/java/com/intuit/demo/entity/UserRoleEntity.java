package com.intuit.demo.entity;

import com.intuit.demo.enumeration.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Setter
@Getter
@Entity
@Table(name = "user_role")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
public class UserRoleEntity extends BaseWithDateEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private UserEntity userEntity;

    @Column(name = "role")
    @Convert(converter = UserType.Converter.class)
    private UserType role;

}
