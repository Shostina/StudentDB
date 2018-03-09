import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {
    private List<String> forAll(Function<Student, String> f, final List<Student> students) {
        return forAllStream(f, students).collect(Collectors.toList());
    }

    private Stream<String> forAllStream(Function<Student, String> f, final List<Student> students) {
        return students.stream().map(f);
    }

    /**
     * Returns student {@link Student#getFirstName() first names}.
     */
    public List<String> getFirstNames(final List<Student> students) {
        return forAll(Student::getFirstName, students);
    }

    /**
     * Returns student {@link Student#getLastName() last names}.
     */
    public List<String> getLastNames(final List<Student> students) {

        return forAll(Student::getLastName, students);
    }

    /**
     * Returns student {@link Student#getGroup() groups}.
     */
    public List<String> getGroups(final List<Student> students) {

        return forAll(Student::getGroup, students);
    }

    /**
     * Returns student {@link Student#getGroup() groups}.
     */
    public List<String> getFullNames(final List<Student> students) {
        return forAll(s -> (s.getFirstName() + " " + s.getLastName()), students);
    }

    /**
     * Returns distinct student {@link Student#getFirstName() first names} in alphabetical order.
     */
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        //return (students.stream().map(s -> (s.getFirstName())).sorted().collect(Collectors.toCollection(TreeSet::new)));
        return forAllStream(Student::getFirstName, students)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Returns name of the student with minimal {@link Student#getId() id}.
     */
    public String getMinStudentFirstName(final List<Student> students) {
        return students.stream()
                .min(Student::compareTo)
                .map(Student::getFirstName)
                .orElse("");
    }

    /**
     * Returns list of students sorted by {@link Student#getId() id}.
     */
    public List<Student> sortStudentsById(Collection<Student> students) {
        return students.stream().sorted(Student::compareTo).collect(Collectors.toList());
    }

    /**
     * Returns list of students sorted by name
     * (students are ordered by {@link Student#getLastName() lastName},
     * students with equal last names are ordered by {@link Student#getFirstName() firstName},
     * students having equal both last and first names are ordered by {@link Student#getId() id}.
     */
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsByNameS(students.stream());
    }

    private List<Student> sortStudentsByNameS(Stream<Student> students) {
        return students
                .sorted(Comparator.comparing(Student::getLastName)
                        .thenComparing(Student::getFirstName)
                        .thenComparing(Student::getId))
                .collect(Collectors.toList());
    }

    private List<Student> findAll(Function<Student, String> f, Collection<Student> students, String name) {
        return sortStudentsByNameS(students.stream().filter(s -> f.apply(s).equals(name)));
    }

    /**
     * Returns list of students having specified first name. Students are ordered by name.
     */
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findAll(Student::getFirstName, students, name);
    }

    /**
     * Returns list of students having specified last name. Students are ordered by name.
     */
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findAll(Student::getLastName, students, name);
    }

    /**
     * Returns list of students having specified groups. Students are ordered by name.
     */
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return findAll(Student::getGroup, students, group);
    }

    /**
     * Returns map of group's student last names mapped to minimal first name.
     */
    public Map<String, String> findStudentNamesByGroup(final Collection<Student> students, final String group) {
        return students.stream()
                .filter(person -> person.getGroup().equals(group))
                .collect(Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(Comparator.naturalOrder())));
    }
}