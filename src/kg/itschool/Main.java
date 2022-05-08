package kg.itschool;

import kg.itschool.model.Course;
import kg.itschool.model.CourseFormat;

import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        CourseFormat bootcampFormat = new CourseFormat();

        bootcampFormat.setFormat("BOOTCAMP");
        bootcampFormat.setId(1L);
        bootcampFormat.setCourseDurationWeeks(12);
        bootcampFormat.setLessonDuration(LocalTime.of(3,0));
        bootcampFormat.setOnline(false);
        bootcampFormat.setLessonsPerWeek(5);

        Course javaCourse = new Course();
        javaCourse.setId(1L);
        javaCourse.setName("Java");
        javaCourse.setPrice(15000);
        javaCourse.setCourseFormat(bootcampFormat);

        Course pythonCourse = new Course();
        pythonCourse.setId(2L);
        pythonCourse.setName("Python");
        pythonCourse.setPrice(15000);
        pythonCourse.setCourseFormat(bootcampFormat);
    }
}
