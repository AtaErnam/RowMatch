package com.example.RowMatch.service;


import com.example.RowMatch.Models.User;
import com.example.RowMatch.repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class userServiceTest {
    @Mock
    private userRepository userRepository;
    private userService underTest;

    @BeforeEach
    void setUp(){
        underTest = new userService(userRepository);
    }

    @Test
    void createUserRequest() {
        // given
        User user = new User(
                1000,
                1
        );
        // when
        underTest.createUserRequest();

        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    public void testUpdateLevelRequest_UserExists() {
        // given
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setLevel(2);
        existingUser.setCoins(5000);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // when
        String result = String.valueOf(underTest.updateLevelRequest(userId));

        // then
        verify(userRepository).save(existingUser);
        assertEquals("User{id=1, coins=15000, level=3}", result);
        assertEquals(3, existingUser.getLevel());
        assertEquals(15000, existingUser.getCoins());
    }

    @Test
    public void testUpdateLevelRequest_UserDoesNotExist() {
        // given
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> underTest.updateLevelRequest(userId));

        // then
        assertEquals("User doesnt exist for id = 1!", thrown.getMessage());
    }

    @Test
    void canSaveUser() {
        // given
        User user = new User(
                1000,
                1
        );
        // when
        underTest.saveUser(user);

        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }


    @Test
    void getUsers() {
        //when
        underTest.getUsers();
        //then
        verify(userRepository).findAll();
    }

    @Test
    void throwExceptionWhenUserIdNotFound() {
        //given
        int id = anyInt();
        //when
        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getUserbyId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("User doesnt exist for id = %s!", id));

        verify(userRepository).findById(id);
    }

    @Test
    void canDeleteTeam() {
        int testId=42;

        // perform the call
        underTest.deleteUser(testId);

        // verify the mocks
        verify(userRepository, times(1)).deleteById(eq(testId));
    }

    @Test
    public void testUpdateUser_whenUserExists_returnsUpdatedUser() {
        // arrange
        int userId = anyInt();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setLevel(2);
        existingUser.setCoins(5000);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setCoins(100);
        updatedUser.setLevel(3);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // act
        User result = underTest.updateUser(updatedUser);

        // assert
        assertEquals(updatedUser, result);
    }

    @Test()
    public void testUpdateUser_whenUserDoesNotExist_throwsEntityNotFoundException() {
        // arrange
        User updatedUser = new User();
        updatedUser.setId(2);
        updatedUser.setCoins(200);
        updatedUser.setLevel(4);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        // when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> underTest.updateUser(updatedUser));

        // then
        assertEquals("User doesnt exist for id = 2!", thrown.getMessage());
    }
}