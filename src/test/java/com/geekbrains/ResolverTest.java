package com.geekbrains;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResolverTest {

    private static Resolver resolver;

    @BeforeAll
    static void beforeAll() {
        resolver = new Resolver();
    }

    @Test
    @DisplayName("Базовый сценарий")
    void baseTest() {
        int min = resolver.getMin(new int[]{4, 2, 1, 5, 2, 6});
        assertThat(min).isEqualTo(1);
    }

    @Test
    @DisplayName("Пустой массив или пустая ссылка")
    void badDataTest() {
        assertThatThrownBy(() -> resolver.getMin(new int[]{}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array could not be empty");

        assertThatThrownBy(() -> resolver.getMin(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array could not be empty");
    }

    @ParameterizedTest(name = "{index}). Expected: {1}")
    @MethodSource("testData")
    void testWithParams(int[] array, int expected) {
        assertThat(resolver.getMin(array))
                .isEqualTo(expected);
    }

    @Timeout(value = 1)
    @RepeatedTest(value = 30)
    void performanceTest() {
        int[] array = new int[4000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1000000);
        }
        assertThat(resolver.sort(array))
                    .isSorted();
    }

    Stream<Arguments> testData() {
        int caseSize = 10;
        List<Arguments> arguments = new ArrayList<>();
        for (int i = 0; i < caseSize; i++) {
            int arraySize = (int) (Math.random() * 10000) + 1;
            int[] array = new int[arraySize];
            for (int j = 0; j < arraySize; j++) {
                array[j] = (int) (Math.random() * 1000000);
            }
            int expected = IntStream.of(array)
                    .min()
                    .orElse(0);
            arguments.add(Arguments.of(array, expected));
        }
        return arguments.stream();
    }

}
