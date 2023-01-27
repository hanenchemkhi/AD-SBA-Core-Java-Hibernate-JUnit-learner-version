package sba.sms.services;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class CourseService implements CourseI {
    @Override
    public void createCourse(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        } catch (HibernateException exception) {
            if (tx != null) tx.rollback();
            exception.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int courseId) {

        Course course = new Course();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Course> query = session.createQuery("from Course where id = :id", Course.class);
            query.setParameter("id", courseId);
            course = query.getSingleResult();
            course = session.get(Course.class, courseId);
        } catch (HibernateException exception) {
            exception.printStackTrace();
        }
        return course;
    }


    @Override
    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Course> query = session.createQuery("from Course ", Course.class);
            courses = query.getResultList();
        } catch (HibernateException exception) {
            exception.printStackTrace();
        }
        return courses;
    }

}
