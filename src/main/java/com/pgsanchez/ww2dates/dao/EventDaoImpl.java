package com.pgsanchez.ww2dates.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pgsanchez.ww2dates.model.Event;

@Repository
public class EventDaoImpl implements EventDao{

	@Autowired
	EventJpa eventJpa;

	@Override
	public List<Event> getAllEvents() {
		
		return eventJpa.findAll();
	}

	@Override
	public List<Event> getEventsByImportance(int importance) {

		eventJpa.findByImportance(importance);
		return null;
	}

	@Override
	public Event getEventByName(String name) {

		return eventJpa.findByName(name);
	}

	@Override
	public Event getEventById(int id) {

		return eventJpa.findById(id);
	}

	@Override
	public void addEvent(Event event) {

		eventJpa.save(event);
		
	}

	@Override
	public void updateEvent(Event event) {

		eventJpa.save(event);
	}

	@Override
	public void deleteEvent(Event event) {

		eventJpa.delete(event);
	}

	@Override
	public void deleteEvent(int id) {

		eventJpa.deleteById(id);
	}
}
