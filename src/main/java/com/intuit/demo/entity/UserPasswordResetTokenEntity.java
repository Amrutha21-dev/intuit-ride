package com.intuit.demo.entity;

import com.intuit.demo.enumeration.ResetPasswordTokenStatus;
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
@Table(name = "user_password_reset_token")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
public class UserPasswordResetTokenEntity extends BaseWithDateEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private UserEntity userEntity;

    @Column(name = "reset_password_token_generated_at")
    private ZonedDateTime resetPasswordTokenGeneratedAt;

    @Column(name = "reset_password_token_expires_at")
    private ZonedDateTime resetPasswordTokenExpiresAt;

    @Column(name = "reset_password_token_status")
    @Convert(converter = ResetPasswordTokenStatus.Converter.class)
    private ResetPasswordTokenStatus resetPasswordTokenStatus;

    public UserPasswordResetTokenEntity(UserEntity userEntity, String resetPasswordToken, ZonedDateTime
            resetPasswordTokenGeneratedAt, ZonedDateTime resetPasswordTokenExpiresAt, ResetPasswordTokenStatus
                                                     resetPasswordTokenStatus){
        setUserEntity(userEntity);
        setResetPasswordToken(resetPasswordToken);
        setResetPasswordTokenGeneratedAt(resetPasswordTokenGeneratedAt);
        setResetPasswordTokenExpiresAt(resetPasswordTokenExpiresAt);
        setResetPasswordTokenStatus(resetPasswordTokenStatus);
    }

}
