package io.inugami.commons.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UnitTestDataTest {

    @Test
    void getRandomUid_nominal() {
        assertThat(UnitTestData.getRandomUid()).isNotEqualTo(UnitTestData.getRandomUid());
        for (int i = 10; i >= 0; i--) {
            log.info("{}", UnitTestData.getRandomUid());
        }
    }

    @Test
    void getRandomWord_nominal() {
        assertThat(UnitTestData.getRandomWord()).isNotNull();
    }

    @Test
    void getRandomCategory_nominal() {
        assertThat(UnitTestData.getRandomCategory()).isNotNull();
    }

    @Test
    void getRandomLabel_nominal() {
        assertThat(UnitTestData.getRandomLabel()).isNotNull();
    }

    @Test
    void getRandomPhrase_nominal() {
        assertThat(UnitTestData.getRandomPhrase()).isNotNull();
    }

    @Test
    void getRandomPhrase_withMinMax() {
        final String[] value = UnitTestData.getRandomPhrase(2, 5, true).split(" ");
        assertThat(value).isNotNull();
        assertThat(value.length).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }

    @Test
    void getRandomSection_nominal() {
        assertThat(UnitTestData.getRandomSection()).isNotNull();
    }

    @Test
    void getRandomSection_withMinMax() {
        assertThat(UnitTestData.getRandomSection(2, 5, 5)).isNotNull();
    }

    @Test
    void getRandomDouble_nominal() {
        final double value = UnitTestData.getRandomDouble(2, 5);
        assertThat(value).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }

    @Test
    void getRandomBetween_nominal() {
        final double value = UnitTestData.getRandomBetween(2, 5);
        assertThat(value).isGreaterThanOrEqualTo(2).isLessThanOrEqualTo(5);
    }
}