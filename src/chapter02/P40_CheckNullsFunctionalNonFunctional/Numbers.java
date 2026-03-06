package chapter02.P40_CheckNullsFunctionalNonFunctional;

import java.util.*;

public final class Numbers {

    private Numbers() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static int sumIntegers(List<Integer> integers) {

        if (Objects.isNull(integers)) {
            throw new IllegalArgumentException("The given list of integers cannot be null");
        }

        return integers.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue).sum();
    }

    public static boolean integersContainsNulls(List<Integer> integers) {

        if (Objects.isNull(integers)) {
            return false;
        }
        return integers.stream()
                .anyMatch(Objects::isNull);
    }

    public static List<Integer> evenIntegers(List<Integer> integers) {

        if (integers == null) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> evens = new ArrayList<>();
        for (Integer i : integers) {
            if (i != null && i % 2 == 0) {
                evens.add(i);
            }
        }

        return null;
    }
}