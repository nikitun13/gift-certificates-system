package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.LoginUserDto;
import com.epam.esm.dto.OktaUserDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.entity.Role;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserMapper mapper;
    private final UserDao dao;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationServiceImpl(UserMapper mapper,
                                         UserDao dao,
                                         AuthenticationManager authenticationManager,
                                         PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.dao = dao;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto signUp(CreateUserDto createUserDto) {
        User user = mapper.toUser(createUserDto);
        String rawPassword = user.getPassword();
        Role role = Role.CLIENT;
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        dao.create(user);
        return mapper.toUserDto(user);
    }

    @Override
    public UserDetailsDto signUp(OktaUserDto oktaUserDto) {
        User user = mapper.toUser(oktaUserDto);
        Role role = Role.CLIENT;
        user.setRole(role);
        dao.create(user);
        return mapper.toUserDetailsDto(user);
    }

    @Override
    public UserDetails login(LoginUserDto loginUserDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUserDto.email(), loginUserDto.password());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        return (UserDetails) authenticate.getPrincipal();
    }
}
