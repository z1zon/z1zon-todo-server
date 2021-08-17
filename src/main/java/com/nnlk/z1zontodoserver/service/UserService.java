package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.user.UserCreateDto;
import com.nnlk.z1zontodoserver.dto.user.UserLoginDto;
import com.nnlk.z1zontodoserver.jwt.TokenProvider;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    @Transactional
    public void register(UserCreateDto userCreatseDto) {
        User user = userCreatseDto.toEntity();
        user.encryptPwd(getSHA256Pwd(userCreatseDto.getPassword()));
        userRepository.save(user);
    }

    public String login(UserLoginDto userLoginDto){
        String userName = userLoginDto.getName();
        String encPwd = getSHA256Pwd(userLoginDto.getPassword());
        User user = (User) loadUserByUsername(userName);
        if(!encPwd.equals(user.getPassword())){
            throw new IllegalArgumentException();
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName,encPwd);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(userName);

        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByName(name)
                .orElseThrow(
                        () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")
                );
    }
    public String getSHA256Pwd(String rawPwd){
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
