package com.intuit.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Table(name = "user_login_info")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
public class UserLoginInfoEntity extends BaseWithDateEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private UserEntity userEntity;

    @Column(name = "last_login_date")
    private ZonedDateTime lastLoginDate;

    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "login_count")
    private Long loginCount;

    public UserLoginInfoEntity(Long id) {
        this.id = id;
        this.loginCount = 1L;
    }
}
