package com.example.RowMatch.repository;

import com.example.RowMatch.Models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class userRepositoryTest {

    @Autowired
    private userRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfUserCoinsExists(){
        // given
        Integer coins = 1000;
        User user = new User(
                1000,
                1
        );
        underTest.save(user);

        //when
        boolean exists = underTest.selectExistsCoins(coins);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfUserCoinsDoesNotExists(){
        // given
        Integer coins = 1000;

        //when
        boolean exists = underTest.selectExistsCoins(coins);
        //then
        assertThat(exists).isFalse();
    }
}