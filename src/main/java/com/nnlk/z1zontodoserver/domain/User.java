package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTime implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 128, nullable = false)
    private String name;

    private String password;

    private String role;

    @NotNull
    private String email;

    private String provider;

    public void update(UserUpsertRequestDto userUpsertRequestDto) {
        this.name = userUpsertRequestDto.getName();
        this.password = userUpsertRequestDto.getPassword();
        this.role = userUpsertRequestDto.getRole();
        this.email = userUpsertRequestDto.getEmail();
    }

    /**
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist() {
        this.provider = Optional.ofNullable(this.provider).orElse("local");
        this.role = Optional.ofNullable(this.role).orElse("public");
    }

    /*
    * ------------------------------------------------------------------------------
    * UserDetails에 대한 구현, SpringSecurity는 UserDetails객체를 통해 권한정보를 관리한다.
    *  getAuthorities()

     Collection<? extends   GrantedAuthority>
     계정이 갖고있는 권한 목록을 리턴한다.
     getPassword()
     계정의 비밀번호를 리턴한다.
     getUsername()
     계정의 이름을 리턴한다.
     isAccountNonExpired()
     계정이 만료되지 않았는 지 리턴한다. (true: 만료안됨)
     isAccountNonLocked()
     계정이 잠겨있지 않았는 지 리턴한다. (true: 잠기지 않음)
     isCredentialNonExpired()
     비밀번호가 만료되지 않았는 지 리턴한다. (true: 만료안됨)
     isEnabled()
     계정이 활성화(사용가능)인 지 리턴한다. (true: 활성화)
*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role)); // role 일단 전부 1로 통일
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void encryptPwd(String encPwd) {
        this.password = encPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
