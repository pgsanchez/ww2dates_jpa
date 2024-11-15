package com.pgsanchez.ww2dates.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.pgsanchez.ww2dates.model.Event;
import com.pgsanchez.ww2dates.service.EventService;

@Controller
public class HomeController {

	@Autowired
	private EventService eventService;
	
	@RequestMapping("/index")
	public String index(Model model) {
		/*ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    String listJSON="";
		try {
			listJSON = objectMapper.writeValueAsString(eventService.getAllEvents());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
		Gson gson = new Gson();
		String listJSON = gson.toJson(eventService.getAllEvents());
		//listJSON = listJSON.replaceAll("\"", "\\\'");
		//System.out.println(listJSON);
		model.addAttribute("eventListJSON", listJSON);
		return "index";
	}
	
	@RequestMapping(value = "/infoEvent", method = RequestMethod.GET)
	public String getInfoEventForm(@RequestParam(value="id", required = true, defaultValue = "0") String idStr, Model model) {
		Event newEvent = new Event();
		if (Integer.parseInt(idStr) != 0)
		{
			newEvent = eventService.getEventById(Integer.parseInt(idStr));
			model.addAttribute("newEvent", newEvent);
			return "infoEvent";
		} else
			return "redirect:/index";
	}
	
}