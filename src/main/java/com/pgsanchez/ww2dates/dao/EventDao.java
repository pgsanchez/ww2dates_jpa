package com.pgsanchez.ww2dates.dao;

import java.util.List;

import com.pgsanchez.ww2dates.model.Event;

public interface EventDao {

	/* En esta interface se definirán los métodos necesarios para
	acceder a la BD a por los datos de Eventos
	*/
	
	List <Event> getAllEvents();
	List <Event> getEventsByImportance(int importance);
	Event getEventByName(String name);
	Event getEventById(int id);
	void addEvent(Event event);
	void updateEvent(Event event);
	void deleteEvent(Event event);
	void deleteEvent(int id);
		
}
