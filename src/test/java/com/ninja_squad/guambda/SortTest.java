package com.ninja_squad.guambda;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SortTest {
    Person agnes;
    Person cyril;
    Person cedric;
    Person jb;
    private List<Person> persons;

    @Before
    public void prepare() {
        agnes = new Person("Agnes", "Crepet", 34);
        cyril = new Person("Cyril", "Lacote", 35);
        cedric = new Person("Cedric", "Exbrayat", 26);
        jb = new Person("JB", "Nizet", 37);
        persons = Arrays.asList(agnes, cyril, cedric, jb);
    }
    
    @Test
    public void sortByWorks() {
        List<Person> list = new ArrayList<>(persons);
        
        Collections.sort(list, Sort.by((Person p) -> p.getLastName()));
        assertEquals(Arrays.asList(agnes, cedric, cyril, jb), list);
    }

    @Test
    public void sortByWithComparatorWorks() {
        List<Person> list = new ArrayList<>(persons);
        
        Collections.sort(list, Sort.by((Person p) -> p.getLastName(), Collections.reverseOrder()));
        
        assertEquals(Arrays.asList(jb, cyril, cedric, agnes), list);
    }
    
    @Test
    public void thenByWorks() {
        List<Person> list = new ArrayList<>(persons);
        
        Collections.sort(list, Sort.by((Person p) -> p.getLastName().charAt(p.getLastName().length() - 1))
                                   .thenBy(p -> p.getAge()));
        assertEquals(Arrays.asList(cyril, cedric, agnes, jb), list);
    }
    
    @Test
    public void thenByWithComparatorWorks() {
        List<Person> list = new ArrayList<>(persons);
        
        Collections.sort(list, Sort.by((Person p) -> p.getLastName().charAt(p.getLastName().length() - 1))
                                   .thenBy(p -> p.getAge(), Collections.reverseOrder()));
        assertEquals(Arrays.asList(cyril, jb, agnes, cedric), list);
    }
    
    private static final class Person {
        private final String firstName;
        private final String lastName;
        private final int age;
        
        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        
        public String getFirstName() {
            return this.firstName;
        }
        
        public String getLastName() {
            return this.lastName;
        }

        public int getAge() {
            return this.age;
        }
    }
}