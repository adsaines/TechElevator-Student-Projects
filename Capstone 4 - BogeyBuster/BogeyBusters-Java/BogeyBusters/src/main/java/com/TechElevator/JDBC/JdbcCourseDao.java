package com.TechElevator.JDBC;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.TechElevator.DAO.CourseDao;
import com.TechElevator.Pojos.Course;
import com.TechElevator.UserSecurity.PasswordHasher;

@Component
public class JdbcCourseDao implements CourseDao {
	
	private JdbcTemplate jdbcTemplate;
	@Autowired
	public JdbcCourseDao(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
	}

	@Override
	public void addCourse(Course course) {
		// Define queries
		String idQuery = "SELECT courseId FROM courses WHERE coursename = ?";
		String inputQuery = "INSERT INTO courses (courseName, par, zip, city, state, courseopen, courseclose) " + 
		"VALUES (?,?,?,?,?,?,?);";
		
		// input course
		jdbcTemplate.update(inputQuery,
							course.getCourseName(), 
							course.getPar(), 
							course.getZipCode(), 
							course.getCity(), 
							course.getState(), 
							LocalTime.parse(course.getOpenTime()),
							course.getCloseTime());
	}

	@Override
	public List<Course> findCoursesByName(String searchString) {
		// create query & list
		String query = "SELECT * FROM courses WHERE courseName LIKE ?;";
		List<Course> foundCourses = new ArrayList<Course>();
		
		// get rowset
		SqlRowSet courseInfo = jdbcTemplate.queryForRowSet(query, "%" + searchString + "%");
		
		// create list of courses
		while (courseInfo.next()) {
			foundCourses.add(createCourseFromDB(courseInfo));
		}
		
		return foundCourses;
	}
	
	private Course createCourseFromDB(SqlRowSet courseInfo) {
		Course newCourse = new Course();
		
		String time = courseInfo.getString("courseOpen");
		time = time.substring(0, 5);
		newCourse.setCity(courseInfo.getString("city"));
		newCourse.setId(courseInfo.getLong("courseId"));
		newCourse.setName(courseInfo.getString("courseName"));
		newCourse.setOpenTime(time);
		newCourse.setCloseTime(courseInfo.getString("courseClose"));
		newCourse.setPar(courseInfo.getInt("par"));
		newCourse.setState(courseInfo.getString("state"));
		newCourse.setZipCode(courseInfo.getInt("zip"));
		
		return newCourse;
	}

	@Override
	public List<Course> findCoursesByState(String state) {
		// create query & list
		String stateQuery = "SELECT * FROM courses WHERE state LIKE ?;";
		List<Course> foundCourses = new ArrayList<Course>();
		
		// get rowset
		SqlRowSet courseInfo = jdbcTemplate.queryForRowSet(stateQuery, "%" + state + "%");
		
		// create list of courses
		while (courseInfo.next()) {
			foundCourses.add(createCourseFromDB(courseInfo));
		}
		
		return foundCourses;
	}

	@Override
	public List<Course> findCoursesByCity(String city) {
		// create query & list
		String cityQuery = "SELECT * FROM courses WHERE city LIKE ?;";
		List<Course> foundCourses = new ArrayList<Course>();
		
		// get rowset
		SqlRowSet courseInfo = jdbcTemplate.queryForRowSet(cityQuery, "%" + city + "%");
		
		// create list of courses
		while (courseInfo.next()) {
			foundCourses.add(createCourseFromDB(courseInfo));
		}
		
		return foundCourses;
	}

	@Override
	public List<Course> findCoursesByZip(long zipcode) {
		// create query & list
		String zipQuery = "SELECT * FROM courses WHERE zip LIKE ?;";
		List<Course> foundCourses = new ArrayList<Course>();
		
		// get rowset
		SqlRowSet courseInfo = jdbcTemplate.queryForRowSet(zipQuery, "%" + zipcode + "%");
		
		// create list of courses
		while (courseInfo.next()) {
			foundCourses.add(createCourseFromDB(courseInfo));
		}
		
		return foundCourses;
	}
	
	@Override
	public List<Course> getAllCourses(){
		
		String allQuery = "SELECT * FROM courses;";
		List<Course> foundCourses = new ArrayList<Course>();
		
		SqlRowSet courseInfo = jdbcTemplate.queryForRowSet(allQuery);
		
		while (courseInfo.next()) {
			foundCourses.add(createCourseFromDB(courseInfo));
		}
		
		return foundCourses;
	}
	
}
