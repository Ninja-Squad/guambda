package com.ninja_squad.guambda;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FluentStreamTest {

    private Person agnes;
    private Person cyril;
    private Person cedric;
    private Person jb;
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
    public void forEachWithIndexedBlockWorks() {
        List<String> indexedFirstNames = new ArrayList<>();
        FluentStream.from(persons).forEach((i, p) -> {
            indexedFirstNames.add((i + 1) + ". " + p.getFirstName());
        });
        assertEquals(Arrays.asList("1. Agnes", "2. Cyril", "3. Cedric", "4. JB"), indexedFirstNames);
    }
    
    @Test
    public void teeWithIndexedBlockWorks() {
        List<String> indexedFirstNames = new ArrayList<>();
        List<String> indexedLastNames = new ArrayList<>();
        FluentStream.from(persons).tee((i, p) -> {
            indexedLastNames.add((i + 1) + ". " + p.getLastName());
        }).forEach((i, p) -> {
            indexedFirstNames.add((i + 1) + ". " + p.getFirstName());
        });
        assertEquals(Arrays.asList("1. Crepet", "2. Lacote", "3. Exbrayat", "4. Nizet"), indexedLastNames);
        assertEquals(Arrays.asList("1. Agnes", "2. Cyril", "3. Cedric", "4. JB"), indexedFirstNames);
    }
    
    @Test
    public void filterWithIndexedFilterkWorks() {
        List<Person> evenPersons = FluentStream.from(persons).filter((i, p) -> i % 2 == 0).into(new ArrayList<Person>());
        List<Person> evenPersonsStartingWithA = FluentStream.from(persons).filter((i, p) -> i % 2 == 0 && p.getFirstName().startsWith("A")).into(new ArrayList<Person>());
        
        assertEquals(Arrays.asList(agnes, cedric), evenPersons);
        assertEquals(Arrays.asList(agnes), evenPersonsStartingWithA);
    }
    
    @Test
    public void mapWithIndexedMapperWorks() {
        List<String> indexedFirstNames = FluentStream.from(persons).map((i, p) -> (i + 1) + ". " + p.getFirstName()).into(new ArrayList<String>());
        assertEquals(Arrays.asList("1. Agnes", "2. Cyril", "3. Cedric", "4. JB"), indexedFirstNames);
    }
    
    @Test
    public void flatMapWithIndexedFlatMapperWorks() {
        List<Object> indicesAndFirstNames = FluentStream.from(persons).flatMap((Block<Object> sink, int i, Person p) -> {
            sink.accept(i + 1);
            sink.accept(p.getFirstName());
        }).into(new ArrayList<>());
        assertEquals(Arrays.asList(1, "Agnes", 2, "Cyril", 3, "Cedric", 4, "JB"), indicesAndFirstNames);
    }
    
    @Test
    public void uniqueIndexWorks() {
        Map<String, Person> personsByFirstName = FluentStream.from(persons).uniqueIndex((p) -> p.getFirstName());
        
        Map<String, Person> expected = new HashMap<>();
        for (Person p : persons) {
            expected.put(p.getFirstName(), p);
        }
        assertEquals(expected, personsByFirstName);
    }
    
    @Test
    public void toListWorks() {
        List<Person> copy = FluentStream.from(persons).toList();
        assertEquals(persons, copy);
    }
    
    @Test
    public void toSetWorks() {
        Set<Person> copy = FluentStream.from(persons).toSet();
        assertEquals(new HashSet<>(persons), copy);
    }
    
    @Test
    public void toSortedSetWorks() {
        Comparator<Person> comparator = (p1, p2) -> (p1.getFirstName().compareTo(p2.getFirstName()));
        SortedSet<Person> copy = FluentStream.from(persons).toSortedSet(comparator);
        
        SortedSet<Person> expected = new TreeSet<>(comparator);
        expected.addAll(persons);
        assertEquals(new ArrayList<>(expected), new ArrayList<>(copy));
    }
    
    @Test
    public void containsWorks() {
        Function<Person, String> toFirstName = (p) -> p.getFirstName();
        assertTrue(FluentStream.from(persons).map(toFirstName).contains("JB"));
        assertFalse(FluentStream.from(persons).map(toFirstName).contains("Claire"));
    }
    
    @Test
    public void firstMatchWorks() {
        assertSame(jb, FluentStream.from(persons).firstMatch((p) -> p.getFirstName().equals("JB")).get());
        assertFalse(FluentStream.from(persons).firstMatch((p) -> p.getFirstName().equals("Claire")).isPresent());
    }
    
    @Test
    public void sizeWorks() {
        assertEquals(4, FluentStream.from(persons).size());
    }
    
    @Test
    public void findLastWorks() {
        assertSame(jb, FluentStream.from(persons).findLast().get());
        assertFalse(FluentStream.from(persons).filter((p) -> p.getAge() > 80).findLast().isPresent());
    }
    
    @Test
    public void getWorks() {
        assertSame(jb, FluentStream.from(persons).get(3).get());
        assertFalse(FluentStream.from(persons).get(4).isPresent());
    }
    
    @Test
    public void multimapWorks() {
        SetMultimap<Character, Person> personsByFirstCharOfFirstName = 
            FluentStream.from(persons).multimap(HashMultimap.<Character, Person>create(), (p) -> p.getFirstName().charAt(0));
        assertEquals(Sets.newHashSet(cyril, cedric), personsByFirstCharOfFirstName.get('C'));
        assertEquals(Sets.newHashSet(jb), personsByFirstCharOfFirstName.get('J'));
        assertEquals(Sets.newHashSet(agnes), personsByFirstCharOfFirstName.get('A'));
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