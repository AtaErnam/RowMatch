package com.example.RowMatch.repository;

import com.example.RowMatch.Models.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class teamRepositoryTest {

    @Autowired
    private teamRepository underTest;

    @AfterEach
    void tearDown() {underTest.deleteAll();}

    @Test
    void itShouldCheckIfTeamNameExists1(){
        // given
        String name = "TeamNull";
        Team testTeam = new Team(
                "TeamNull",
                new ArrayList<>()
        );
        underTest.save(testTeam);

        //when
        boolean exists = underTest.selectExistsName(name);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfTeamNameDoesNotExists1(){
        // given
        String name = "TeamNull";

        //when
        boolean exists = underTest.selectExistsName(name);
        //then
        assertThat(exists).isFalse();
    }


    @Test
    void itShouldCheckIfTeamNameExists() {
        //given
        String name = "TeamNull";
        Team testTeam = new Team(
                "TeamNull",
                new ArrayList<>()
        );
        underTest.save(testTeam);

        //when
        Boolean exists = underTest.findByName(name).isPresent();
        //then
        assertThat(exists).isEqualTo(true);
    }

    @Test
    void itShouldCheckIfTeamNameDoesNotExists(){
        // given
        String name = "TeamNull1";

        //when
        Boolean exists = underTest.findByName(name).isPresent();
        //then
        assertThat(exists).isEqualTo(false);
    }
}