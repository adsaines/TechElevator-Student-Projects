package com.TechElevator.Controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TechElevator.DAO.CourseDao;
import com.TechElevator.Pojos.Course;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.RequestAuthProvider;
import com.TechElevator.UserSecurity.RequestResult;

@CrossOrigin
@RestController
@RequestMapping(path="/course")
public class CourseController {
	
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private AuthProvider authprovider;
	
	public CourseController(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@GetMapping(path="/searchByName/{coursename}")
	public List<Course> getCoursesByName(@PathVariable String coursename){
		return courseDao.findCoursesByName(coursename);
	}
	
	@GetMapping(path="/searchByCity/{city}")
	public List<Course> getCoursesByCity(@PathVariable String city) {
		return courseDao.findCoursesByCity(city);
	}

	@GetMapping(path="/searchByState/{state}")
	public List<Course> getCoursesByState(@PathVariable String state) {
		
		return courseDao.findCoursesByState(state);
	}
	
	@GetMapping(path="/searchByZip/{zipcode}")
	public List<Course> getCoursesByZip(@PathVariable int zipcode) {
		return courseDao.findCoursesByZip(zipcode);
	}
	
	@GetMapping(path="/getAll")
	public List<Course> getAllCourses() {
		return courseDao.getAllCourses();

	}
	
	@PostMapping(path="/add")
	public RequestResult addCourseToDatabase(@Valid @RequestBody Course course, BindingResult results) throws GeneralException {
		
		RequestResult reqRes = new RequestResult();
		
		if (results.hasErrors()) {
			// load binding errors and return them
			reqRes.setSuccess(false);
			for (ObjectError error : results.getAllErrors()) {
				reqRes.addError(error.getDefaultMessage());
			}
		} else if(courseDao.findCoursesByName(course.getCourseName()).size() > 0) {
				reqRes.setSuccess(false);
				reqRes.addError("This course already exists.");
		
		} else {
			// check if user is an admin
			boolean roleQualified = authprovider.userHasRole(new String[] {RequestAuthProvider.ADMIN});
			if (roleQualified) {
				reqRes.setSuccess(true);
				courseDao.addCourse(course);
			} else {
				reqRes.setSuccess(false);
				reqRes.addError("User role does not grant permission for that operation.");
			}
		}
		return reqRes;
	}
	
	// TODO request course addition, extra credit
}
