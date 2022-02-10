package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.Page;
import com.epam.esm.dto.UserDto;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
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
    public List<UserDto> findAll(Page page) {
        return dao.findAll(page).stream()
                .map(mapper::toUserDto)
                .toList();
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return dao.findById(id)
                .map(mapper::toUserDto);
    }
}
