package com.example.experiment.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMap {

    public static void main(String[] args) {
        String[][] nested = new String[][]{{"Apple", "Cherry"}, {"Mango", "Orange"}, {"Grape", "BlueBerry"}};

        List<Stream<String>> collectByMap = Arrays.stream(nested)
                .map(Arrays::stream)
                .collect(Collectors.toList());

        Stream<Stream<String>> streamStream = Arrays.stream(nested)
                .map(Arrays::stream);

        List<List<String>> names = new ArrayList<>(List.of(
                List.of("a", "b"),
                List.of("c", "d")
        ));
        List<Person> persons = names.stream()
                .flatMap(Collection::stream)
                .map(Person::new)
                .toList();

        persons.stream()
                .forEach(p -> System.out.println(p.name));

        List<List<Integer>> numbers = new ArrayList<>(List.of(
                List.of(3, 10),
                List.of(20, 1)
        ));

        List<Integer> numberList = numbers.stream()
                .flatMap(Collection::stream)
                .filter(n -> n > 5)
                .collect(Collectors.toList());

        numberList.forEach(System.out::println);
    }
}

class Person {
    String name;

    public Person(String name) {
        this.name = name;
    }
}
