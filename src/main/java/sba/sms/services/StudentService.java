package sba.sms.services;

import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class StudentService implements StudentI {
    private static final CourseService courseService = new CourseService();

    @Override
    public void createStudent(Student student) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (HibernateException hex) {
            if (transaction != null){ transaction.rollback();}
            hex.printStackTrace();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {

        Student student = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            student = session.get(Student.class,email);

        } catch (HibernateException  hex) {
            hex.printStackTrace();
        }
        return student;
    }


    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            TypedQuery<Student> query =
                    session.createNamedQuery("Student.getAllStudents", Student.class);
            students = query.getResultList();
        } catch (HibernateException exception) {
            exception.printStackTrace();
        }
        return students;
    }
    @Override
    public void registerStudentToCourse(String email, int courseId) {

        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();
            Student student = getStudentByEmail(email);
            student.addCourse(courseService.getCourseById(courseId));
            session.merge(student);
            transaction.commit();
        } catch (HibernateException exception) {
            if (transaction != null) transaction.rollback();
            exception.printStackTrace();
        }
    }
    @Override
    public List<Course> getStudentCourses(String email) {
        List<Course> courses = new ArrayList<>();
        final String FIND_STUDENT_COURSE =
                "select c from Course c where :student member of c.students";
        Student student = getStudentByEmail(email);
        try ( Session session = HibernateUtil.getSessionFactory().openSession();){

            TypedQuery<Course> query = session.createQuery(FIND_STUDENT_COURSE, Course.class);
            query.setParameter("student", student);
            courses = query.getResultList();

        } catch (HibernateException exception) {
            exception.printStackTrace();
        }
        return courses;
    }
    @Override
    public boolean validateStudent(String email, String password) {

        return getStudentByEmail(email) != null &&
                getStudentByEmail(email).getPassword().equals(password);
    }
}
