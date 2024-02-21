package com.intuit.demo.entity;

import com.intuit.demo.enumeration.UserRefreshTokenStatus;
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
@Table(name = "user_refresh_token")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
public class UserRefreshTokenEntity extends BaseWithDateEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private UserEntity userEntity;

    @Column(name = "refresh_token_generated_at")
    private ZonedDateTime refreshTokenGeneratedAt;

    @Column(name = "refresh_token_expires_at")
    private ZonedDateTime refreshTokenExpiresAt;

    @Column(name = "status")
    @Convert(converter = UserRefreshTokenStatus.Converter.class)
    private UserRefreshTokenStatus userRefreshTokenStatus;

}
