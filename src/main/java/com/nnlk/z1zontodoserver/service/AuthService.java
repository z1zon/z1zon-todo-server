package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.user.request.UserLoginDto;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.jwt.TokenProvider;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@AllArgsConstructor
@Service
@Slf4j
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    @Transactional
    public void register(UserUpsertRequestDto userUpsertRequestDto) {
        validateDuplicateUser(userUpsertRequestDto.getEmail());

        User user = userUpsertRequestDto.toEntity();
        user.encryptPwd(getSHA256Pwd(userUpsertRequestDto.getPassword()));
        log.debug("   ---> user {}",user);
        userRepository.save(user);
    }

    public String login(UserLoginDto userLoginDto) {
        String email = userLoginDto.getEmail();
        String encPwd = getSHA256Pwd(userLoginDto.getPassword());
        User user = (User) loadUserByUsername(email);
        if (!encPwd.equals(user.getPassword())) {
            throw new IllegalArgumentException();
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, encPwd);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(email);

        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            if(log.isErrorEnabled()){
                log.error("   ---> 유저 이름을 찾을 수 없습니다.{}",user);
            }
            throw  new UsernameNotFoundException("유저 이름을 찾을 수 없습니다.");
        }
        return user;
    }

    private void validateDuplicateUser(String email){
        User user = userRepository.findByEmail(email);
        if(user != null){
            if(log.isErrorEnabled()){
                log.error("   ---> 이미 사용중인 이메일 입니다.{}",user);
            }
            throw new DuplicateKeyException("이미 사용중인 이메일 입니다.");
        }
    }

    public String getSHA256Pwd(String rawPwd) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(rawPwd.getBytes());
        String hashedPwd = String.format("%064x", new BigInteger(1, md.digest()));
        return hashedPwd;
    }


}
