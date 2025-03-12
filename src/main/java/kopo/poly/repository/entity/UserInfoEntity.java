package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "user_name", length = 500, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "password", length = 1, nullable = false)
    private String password;

    @NonNull
    @Column(name = "email", nullable = false)
    private String email;

    @NonNull
    @Column(name = "addr1", nullable = false)
    private String addr1;

    @NonNull
    @Column(name = "addr2", nullable = false)
    private String addr2;

    @Column(name = "reg_id", nullable = false)
    private String regId;

    @Column(name = "reg_dt", nullable = false)
    private String regDt;

    @Column(name = "chg_id", nullable = false)
    private String chgId;

    @Column(name = "chg_dt", nullable = false)
    private String chgDt;

    @Column(name = "roles") // 권한 데이터는 ,를 구분자로 여러 개(예 : 관리자, 일반사용자) 정의 가능함
    private String roles;

}
