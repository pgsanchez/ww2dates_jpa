package com.pgsanchez.ww2dates.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pgsanchez.ww2dates.dao.EventDao;
import com.pgsanchez.ww2dates.model.Event;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	EventDao eventDao;
	
	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public List<Event> getAllEvents() {
		
		return eventDao.getAllEvents();
	}

	@Override
	public List<Event> getEventsByImportance(int importance) {

		return eventDao.getEventsByImportance(importance);
	}

	@Override
	public Event getEventByName(String name) {

		return eventDao.getEventByName(name);
	}

	@Override
	public Event getEventById(int id) {

		return eventDao.getEventById(id);
	}

	@Override
	public void addEvent(Event newEvent, MultipartFile imageFile) {

		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String filename = fileStorageService.store(imageFile);

				newEvent.setImageName(filename);
				
			} catch (Exception e) {
				throw new RuntimeException("Image file saving failed",e);
			}
		}
		
		eventDao.addEvent(newEvent);
	}

	@Override
	public void updateEvent(Event event, MultipartFile imageFile) {
		
		if (imageFile != null) {

			deleteImage(event.getId());
			
			try {
				String filename = fileStorageService.store(imageFile);

				event.setImageName(filename);
				
			} catch (Exception e) {
				throw new RuntimeException("Image file saving failed",e);
			}
		}
		
		eventDao.updateEvent(event);
	}

	@Override
	public void deleteEvent(Event event) {

		eventDao.deleteEvent(event);
	}

	@Override
	public void deleteEvent(int eventId) {

		// Borrar la imagen asociada
		deleteImage(eventId);
		// Borrar los datos del evento
		eventDao.deleteEvent(eventId);
	}

	public boolean deleteImage(int eventId) {
		boolean deleted = false;
		Event event = eventDao.getEventById(eventId);
		if ((event != null) && (event.getImageName()!=null)) {
			deleted = fileStorageService.delete(event.getImageName());
		}
		
		return deleted;
	}
}
