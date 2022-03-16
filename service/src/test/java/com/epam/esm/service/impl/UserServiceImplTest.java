package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDetailsDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserDao dao;

    @Mock
    private UserMapper mapper;

    private final User carl = User.builder().id(5L).email("carl").firstName("Carl").lastName("Fives").build();
    private final UserDto carlDto = new UserDto(5L, "carl", "Card", "Fives");

    @Test
    @Tag("findAll")
    void shouldDelegateToDaoFindAllAndResultToMapper() {
        Long id = 3L;
        String username = "nick";
        String lastName = "Third";
        String firstName = "Nick";
        User nick = User.builder().id(id).email(username).firstName(firstName).lastName(lastName).build();
        UserDto nickDto = new UserDto(id, username, firstName, lastName);
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

    @Test
    @Tag("loadUserByUsername")
    void shouldReturnUserDetailsByExistingUsername() {
        String username = carl.getEmail();
        UserDetailsDto expected = new UserDetailsDto(carl.getId(), carl.getEmail(), carl.getPassword(), carl.getFirstName(), carl.getLastName(), carl.getRole());
        doReturn(Optional.of(carl))
                .when(dao)
                .findByEmail(username);
        doReturn(expected)
                .when(mapper)
                .toUserDetailsDto(carl);

        UserDetails actual = service.loadUserByUsername(username);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("loadUserByUsername")
    void shouldThrowUsernameNotFoundExceptionIfNoSuchUsername() {
        String username = "noSuchUsername";
        doReturn(Optional.empty())
                .when(dao)
                .findByEmail(username);

        assertThatThrownBy(() -> service.loadUserByUsername(username))
                .isExactlyInstanceOf(UsernameNotFoundException.class);
    }
}
