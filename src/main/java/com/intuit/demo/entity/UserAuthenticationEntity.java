package com.intuit.demo.entity;

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
@Table(name = "user_authentication")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseWithDateEntity.class)
public class UserAuthenticationEntity extends BaseWithDateEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotAudited
    private UserEntity userEntity;

    @Column(name = "encrypted_password")
    private String encryptedPassword;

    public UserAuthenticationEntity(Long userId, String encryptedPassword){
        setId(userId);
        setEncryptedPassword(encryptedPassword);
    }
}
