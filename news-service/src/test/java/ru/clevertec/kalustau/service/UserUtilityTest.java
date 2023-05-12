package ru.clevertec.kalustau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.kalustau.client.controller.UserFeignClient;
import ru.clevertec.kalustau.client.dto.User;

import ru.clevertec.kalustau.service.impl.UserUtility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getUserAdmin;
import static ru.clevertec.kalustau.util.TestData.getUserJournalist;
import static ru.clevertec.kalustau.util.TestData.getUserSubscriber;


@ExtendWith(MockitoExtension.class)
class UserUtilityTest {

    @InjectMocks
    private UserUtility userUtility;

    @Mock
    private UserFeignClient userFeignClient;

    @Test
    void checkGetUserByToken() {
        User user = getUserAdmin();
        ResponseEntity<User> responseEntity = new ResponseEntity<>(user, HttpStatus.OK);

        doReturn(responseEntity)
                .when(userFeignClient).getUserByToken(TEST_TOKEN);

        User result = userUtility.getUserByToken(TEST_TOKEN);

        verify(userFeignClient).getUserByToken(anyString());
        assertThat(result).isEqualTo(user);

    }

    @Test
    void checkIsUserAdminShouldReturnTrue() {
        User user = getUserAdmin();
        assertThat(UserUtility.isUserAdmin(user)).isTrue();
    }

    @Test
    void checkIsUserAdminShouldReturnFalse() {
        User user = getUserJournalist();
        assertThat(UserUtility.isUserAdmin(user)).isFalse();
    }

    @Test
    void checkIsUserJournalistShouldReturnTrue() {
        User user = getUserJournalist();
        assertThat(UserUtility.isUserJournalist(user)).isTrue();
    }

    @Test
    void checkIsUserJournalistShouldReturnFalse() {
        User user = getUserAdmin();
        assertThat(UserUtility.isUserJournalist(user)).isFalse();
    }

    @Test
    void checkIsUserSubscriberShouldReturnTrue() {
        User user = getUserSubscriber();
        assertThat(UserUtility.isUserSubscriber(user)).isTrue();
    }

    @Test
    void checkIsUserSubscriberShouldReturnFalse() {
        User user = getUserJournalist();
        assertThat(UserUtility.isUserSubscriber(user)).isFalse();
    }

    @Test
    void checkIsUserAdminHasRightsToModification() {
        User user = getUserAdmin();
        String username = "some_user";
        assertThat(UserUtility.isUserHasRightsToModification(user, username)).isTrue();
    }

    @Test
    void checkIsUserJournalistHasNoRightsToModification() {
        User user = getUserJournalist();
        String username = "some_user";
        assertThat(UserUtility.isUserHasRightsToModification(user, username)).isFalse();
    }
}