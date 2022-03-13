package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.entity.Role;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserDao dao;

    public UserServiceImpl(UserMapper mapper, UserDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return dao.findAll(pageable)
                .map(mapper::toUserDto);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return dao.findById(id)
                .map(mapper::toUserDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dao.findByUsername(username)
                .map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("No such user with the username: " + username));
    }

    private UserDetails buildUserDetails(User user) {
        List<Role> authorities = List.of(user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
