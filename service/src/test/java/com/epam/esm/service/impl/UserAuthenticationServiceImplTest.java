package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CreateUserDto;
import com.epam.esm.dto.LoginUserDto;
import com.epam.esm.dto.UserDetailsDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.UserMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceImplTest {

    @InjectMocks
    private UserAuthenticationServiceImpl service;

    @Mock
    private UserDao dao;

    @Mock
    private UserMapper mapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @Tag("signUp")
    void shouldSignUpNewUser() {
        CreateUserDto createUserDto = new CreateUserDto("carl", "pass", "Carl", "Henson");
        String encodedPassword = "encodedPassword";
        Long id = 50L;
        doReturn(encodedPassword)
                .when(passwordEncoder)
                .encode(createUserDto.password());
        User mappedUser = User.builder()
                .id(id)
                .username(createUserDto.username())
                .password(createUserDto.password())
                .firstName(createUserDto.firstName())
                .lastName(createUserDto.lastName())
                .build();
        doReturn(mappedUser)
                .when(mapper)
                .toUser(createUserDto);
        User user = User.builder()
                .id(id)
                .username(createUserDto.username())
                .password(encodedPassword)
                .firstName(createUserDto.firstName())
                .lastName(createUserDto.lastName())
                .role(Role.CLIENT)
                .build();
        UserDto expected = new UserDto(id, user.getUsername(), user.getFirstName(), user.getLastName());
        doReturn(expected)
                .when(mapper)
                .toUserDto(user);

        UserDto actual = service.signUp(createUserDto);

        verify(dao).create(user);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Tag("login")
    void shouldAuthenticateUser() {
        LoginUserDto loginUserDto = new LoginUserDto("nick", "pass");
        UserDetailsDto expected = new UserDetailsDto(4L, "nick", "encodedPassword", "Nick", "Henson", Role.CLIENT);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUserDto.username(), loginUserDto.password());
        Authentication authResult = new UsernamePasswordAuthenticationToken(expected, null);
        doReturn(authResult)
                .when(authenticationManager)
                .authenticate(authentication);

        UserDetails actual = service.login(loginUserDto);

        assertThat(actual).isEqualTo(expected);
    }
}
