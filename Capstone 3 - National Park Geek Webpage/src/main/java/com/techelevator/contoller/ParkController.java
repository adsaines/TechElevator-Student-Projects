package com.techelevator.contoller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.DAO.ParkAndWeatherDAO;
import com.techelevator.DAO.SurveyDAO;
import com.techelevator.objects.DayOfWeather;
import com.techelevator.objects.Survey;

@Controller
public class ParkController {
	
	@Autowired
	private ParkAndWeatherDAO parkdao;
	
	@Autowired
	private SurveyDAO surveydao;
	
	// get and post for survey
	@RequestMapping(path="/EnterSurvey", method=RequestMethod.POST)
	public String directToSurveyResults(@Valid @ModelAttribute Survey survey, BindingResult result, 
										RedirectAttributes redir, 
										HttpSession session) {
		
		redir.addFlashAttribute("survey", survey);
		
		if (result.hasErrors()) {
			// if there are errors in the customer survey then redirect back to the survey page and highlight errors
			redir.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "survey", result);
			return "redirect:/DailySurvey";
		}
		
		surveydao.inputSurvey(survey);
		
		return "redirect:/SurveyResults";
	}
	
	@RequestMapping(path="/DailySurvey", method=RequestMethod.GET)
	public String displaySurvey(ModelMap map) {
		
		// if there are no surveys in the current session then make a new one, otherwise run with the one that is there
		if (map.containsAttribute("survey") == false) {
			map.put("survey", new Survey());
		}
		
		map.put("states", Survey.stateAbbreviations);
		map.put("activityLevel", Survey.activityLevelList);
		map.put("parkCodeList", parkdao.getAllParkCodesWithNames());
		
		return "DailySurvey";
	}
	
	// get for survey results
	@RequestMapping(path="/SurveyResults", method=RequestMethod.GET)
	public String displaySurveyResults(ModelMap map) {
		
		map.put("results", surveydao.getNumberOfSurveys());
		map.put("parkCodeList", parkdao.getAllParkCodesWithNames());
		
		return "SurveyResults";
	}
	
	// get for one site object, include the temperature toggle on this page (session object)
	@RequestMapping(path="/SiteDetails", method=RequestMethod.GET)
	public String displaySiteDetails(HttpSession session, ModelMap map, 
									@RequestParam String parkCode) {
									
		
		List<DayOfWeather> forecast = parkdao.getWeatherForPark(parkCode);
		List<String> weatherSuggestion = null;
				
		// run through the forecast for the park
		String tempScale = (String) session.getAttribute("tempScale");

		for(DayOfWeather day : forecast) {
			// get the weather suggestions for today
			if (day.getForecastDay() ==1) {
				weatherSuggestion = day.getWeatherSuggestions();
			}
			day.setTempScale(tempScale);
		}
		
		map.put("park", parkdao.getParkInformation(parkCode));
		map.put("fiveDayForecast", forecast);
		map.put("suggestionList", weatherSuggestion);
		map.put("parkCodeList", parkdao.getAllParkCodesWithNames());
		
		return "SiteDetails";
	}
	
	
	@RequestMapping(path="/changeTempScale", method=RequestMethod.GET)
	public String changeTempScale(HttpSession session,
								@RequestParam String parkCode,
								ModelMap modelMap){
		String tempScale = (String) session.getAttribute("tempScale");
		if(tempScale.contentEquals(DayOfWeather.FAHREINHEIT)) {
			tempScale = DayOfWeather.CELSIUS;
		}
		else if(tempScale.contentEquals(DayOfWeather.CELSIUS)) {
			tempScale = DayOfWeather.FAHREINHEIT;
		}
		
		session.setAttribute("tempScale", tempScale);
		modelMap.put("parkCode", parkCode);
		return "redirect:/SiteDetails";
	}
	
	
	
	// homepage is always a get, default to here
	@RequestMapping(path= {"/", "/HomePage"}, method=RequestMethod.GET)
	public String defaultPage(ModelMap map, HttpSession session) {
		if(session.getAttribute("tempScale") == null) {
		session.setAttribute("tempScale", DayOfWeather.FAHREINHEIT);
		}
		map.put("parkList", parkdao.getAllParks());
		map.put("parkCodeList", parkdao.getAllParkCodesWithNames());
		
		return "HomePage";
	}
	
	
	
}
