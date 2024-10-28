package com.pgsanchez.ww2dates.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pgsanchez.ww2dates.model.Event;

public interface EventService {
	List <Event> getAllEvents();
	List <Event> getEventsByImportance(int importance);
	Event getEventByName(String name);
	Event getEventById(int id);
	void addEvent(Event newEvent, MultipartFile imageFile);
	void updateEvent(Event event, MultipartFile imageFile);
	void deleteEvent(Event event);
	void deleteEvent(int eventId);
}
