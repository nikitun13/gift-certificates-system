package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.UserMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserDao dao;

    @Mock
    private UserMapper mapper;

    private User carl = new User(5L, "carl");
    private UserDto carlDto = new UserDto(5L, "carl");

    @Test
    @Tag("findAll")
    void shouldDelegateToDaoFindAllAndResultToMapper() {
        User nick = new User(3L, "nick");
        UserDto nickDto = new UserDto(3L, "nick");
        Pageable pageable = PageRequest.of(0, 20);
        List<User> users = List.of(carl, nick);
        Page<User> page = new PageImpl<>(users, pageable, users.size());
        doReturn(page)
                .when(dao)
                .findAll(pageable);
        List<UserDto> userDtoList = List.of(carlDto, nickDto);
        Page<UserDto> expected = new PageImpl<>(userDtoList, pageable, userDtoList.size());
        doReturn(carlDto)
                .when(mapper)
                .toUserDto(carl);
        doReturn(nickDto)
                .when(mapper)
                .toUserDto(nick);

        Page<UserDto> actual = service.findAll(pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("findById")
    void shouldDelegateFindByIdToDaoAndResultToMapper() {
        Long id = carl.getId();
        doReturn(Optional.of(carl))
                .when(dao)
                .findById(id);
        doReturn(carlDto)
                .when(mapper)
                .toUserDto(carl);
        Optional<UserDto> expected = Optional.of(carlDto);

        Optional<UserDto> actual = service.findById(id);

        assertThat(actual).isEqualTo(expected);
    }
}
