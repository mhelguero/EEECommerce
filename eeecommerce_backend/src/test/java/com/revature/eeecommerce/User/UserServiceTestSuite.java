package com.revature.eeecommerce.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTestSuite{
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserService sut;

    private static User newUser = new User(6, "Naruto", "Uzumaki", "393 Hokage Drive", "naruto@mail.com", "narutopw", User.userType.CUSTOMER);

    @Test
    public void testFindAll(){
        List<User> users = Arrays.asList(newUser);
        when(mockUserRepository.findAll())
                .thenReturn(users);
        List<User> result = sut.findAll();
        assertEquals(1, result.size());
        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    public void testFindByEmailAndPassword() {
        when(mockUserRepository.findByEmailAndPassword(newUser.getEmail(), newUser.getPassword()))
                .thenReturn(Optional.of(newUser));

        final User[] result = new User[1];
        assertDoesNotThrow(() -> {
            result[0] = sut.findByEmailAndPassword(newUser.getEmail(), newUser.getPassword());
        });


        assertEquals(newUser, result[0]);

        verify(mockUserRepository, times(1)).findByEmailAndPassword(newUser.getEmail(), newUser.getPassword());

    }

    @Test
    public void testCreate(){
        when(mockUserRepository.save(newUser)).thenReturn(newUser);
        User result = sut.create(newUser);
       assertEquals(newUser, result);
        verify(mockUserRepository, times(1)).save(newUser);
    }

    @Test
    public void testFindById(){
        when(mockUserRepository.findByUserId(6)).thenReturn(Optional.of(newUser));

        User result = sut.findById(6);

        assertEquals(newUser, result);
        verify(mockUserRepository, times(1)).findByUserId(6);
    }

    @Test
    public void testUpdateUser(){
        when(mockUserRepository.save(newUser)).thenReturn(newUser);

        assertTrue(sut.update(newUser));
        verify(mockUserRepository, times(1)).save(newUser);
    }

    @Test
    public void testDeleteUser(){
        assertTrue(sut.delete(newUser.getUserId()));
        verify(mockUserRepository, times(1)).deleteById(newUser.getUserId());
    }
}

