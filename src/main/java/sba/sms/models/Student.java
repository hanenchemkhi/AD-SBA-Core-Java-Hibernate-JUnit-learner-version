package sba.sms.models;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter@Getter@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "student")
@NamedQueries({
        @NamedQuery(name = "Student.getAllStudents", query = "from Student"),
        @NamedQuery(name = "Student.getByEmail", query = "from Student where id = :id")
})
public class Student {
    @NonNull
    @Id
    @Column(length = 50,name = "email", unique = true)
    String email;
    @NonNull
    @Column(length = 50, nullable = false, name = "name")
    String name;
    @NonNull
    @Column(length = 50,name = "password")
    String password;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
    Set<Course> courses = new HashSet<>();

    public void addCourse(Course course){
        courses.add(course);
        course.getStudents().add(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}
