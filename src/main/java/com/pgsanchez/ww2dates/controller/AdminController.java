package com.pgsanchez.ww2dates.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pgsanchez.ww2dates.dto.EventDto;
import com.pgsanchez.ww2dates.model.Event;
import com.pgsanchez.ww2dates.service.EventService;
import com.pgsanchez.ww2dates.service.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AdminController {

	final String imagesPath = "..\\resources\\images\\";
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@RequestMapping(value="/events", method=RequestMethod.GET)
	public String AllEventsView(Model model) {

		model.addAttribute("events", eventService.getAllEvents());

		return "events";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddNewEventForm(Model model) {
		EventDto newEvent = new EventDto();
		model.addAttribute("newEvent", newEvent);
		return "addEvent";
	}
	
	/* 
	 * El orden de los parámetros en esta llamada es importante. El newEvent es el que se valida, así que el parámetro siguiente tiene que ser el BindingResult
	 * Si se pone primero el MultipartFile (antes del BindingResult), da un error de validación, porque intentará validar el MultipartFile.
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddNewEvent(@Valid @ModelAttribute("newEvent") EventDto newEvent, BindingResult result, @ModelAttribute("imageFile") MultipartFile imageFile, HttpServletRequest request) {

		// Se validan los datos del formulario
		if(result.hasErrors()) {
			return "addEvent";
		}
		
		// La imagen se trata por separado.
	    if (!imageFile.isEmpty()) {
	    	
	    	// Se valida la imagen: tipo de archivo. Solo se permiten formatos jpeg, jpg y png.
	        String contentType = imageFile.getContentType();
	        if (!"image/jpeg".equals(contentType) && !"image/jpg".equals(contentType) && !"image/png".equals(contentType)) {
	            result.rejectValue("imageName", "Validation.AddEventForm.imageFile", "Solo se permiten imágenes JPG, JPEG y PNG");
	            return "addEvent";
	        }
	    }
	    
	    // La fecha de fin no puede ser anterior a la fecha de inicio
	    if ((newEvent.getEndDate() != null) && (newEvent.getEndDate().before(newEvent.getDate()))) {
	    	result.rejectValue("endDate", "Validation.AddEventForm.endDate", "La fecha de fin no puede ser anterior a la fecha de inicio");
            return "addEvent";
	    }
	    

	    // Si las validaciones han ido bien:
		if (newEvent.getId() == 0) {
			
			Event event = null;
			try {
				// ahora mismo no hay necesidad de un DTO; se podría quitar para simplificar
				event = convertToEntity(newEvent);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			eventService.addEvent(event, imageFile);
		}

		return "redirect:/events";
	}
	
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String getUpdateEventForm(@RequestParam(value="id", required = true, defaultValue = "0") String idStr, Model model) {
		Event newEvent = new Event();
		if (Integer.parseInt(idStr) != 0)
		{
			newEvent = eventService.getEventById(Integer.parseInt(idStr));
			EventDto newEventDto = convertToDto(newEvent);
			model.addAttribute("newEvent", newEventDto);
			return "updateEvent";
		} else
			return "redirect:/events";
	}
	
	/* 
	 * El orden de los parámetros en esta llamada es importante. El newEvent es el que se valida, así que el parámetro siguiente tiene que ser el BindingResult
	 * Si se pone primero el MultipartFile (antes del BindingResult), da un error de validación, porque intentará validar el MultipartFile.
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String getUpdateEventForm(@Valid @ModelAttribute("newEvent") EventDto newEvent, BindingResult result, @ModelAttribute("imageFile") MultipartFile imageFile, HttpServletRequest request) {

		// Se validan los datos del formulario
		if(result.hasErrors()) {
			return "updateEvent";
		}
		
		// La imagen se trata por separado.
	    if (!imageFile.isEmpty()) {
	    	
	    	// Se valida la imagen: tipo de archivo. Solo se permiten formatos jpeg, jpg y png.
	        String contentType = imageFile.getContentType();
	        if (!"image/jpeg".equals(contentType) && !"image/jpg".equals(contentType) && !"image/png".equals(contentType)) {
	            result.rejectValue("imageName", "Validation.AddEventForm.imageFile", "Solo se permiten imágenes JPG, JPEG y PNG");
	            return "updateEvent";
	        }
	    }
	    
	 // La fecha de fin no puede ser anterior a la fecha de inicio
	    if ((newEvent.getEndDate() != null) && (newEvent.getEndDate().before(newEvent.getDate()))) {
	    	result.rejectValue("endDate", "Validation.AddEventForm.endDate", "La fecha de fin no puede ser anterior a la fecha de inicio");
            return "updateEvent";
	    }
		
		Event event = null;
		
		try {
			// ahora mismo no hay necesidad de un DTO; se podría quitar para simplificar
			event = convertToEntity(newEvent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Si la imagen no ha cambiado, solo hay que modificar los datos. Se manda el imageFile a null
		if (imageFile.isEmpty()) {
			eventService.updateEvent(event, null);
		} else {
			eventService.updateEvent(event, imageFile);
		}
		
		return "redirect:/events";
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String processAddNewEvent(@RequestParam(value="id", required = true, defaultValue = "0") String idStr, Event newEvent) {
		if (Integer.parseInt(idStr) != 0) {

			// Borrar el evento de la BD
			eventService.deleteEvent(Integer.parseInt(idStr));
		}
		return "redirect:/events";
	}
	
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public String processShowImage(@RequestParam(value="imageName", required = true) String imageName, Model model) {
		model.addAttribute("imageName", imageName);
		return "image";
	}
	
	@RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = fileStorageService.loadAsResource(filename);

		return ResponseEntity.ok().body(file);
	}

	/*private String getFileExtension(String filename) {
	    if (filename == null) {
	        return null;
	    }
	    int dotIndex = filename.lastIndexOf(".");
	    if (dotIndex >= 0) {
	        return filename.substring(dotIndex + 1);
	    }
	    return "";
	}*/

	private EventDto convertToDto(Event event) {
		EventDto eventDto = new EventDto();
		
		eventDto.setId(event.getId());
		eventDto.setName(event.getName());
		eventDto.setDescription(event.getDescription());
		
		eventDto.setImportance(event.getImportance());
		eventDto.setLatitude(event.getLatitude());
		eventDto.setLongitude(event.getLongitude());
		
		eventDto.setImageName(event.getImageName());
		// El dto utiliza java.util.date, pero el modelo Event utiliza java.sql.date por la BD. Hay que convertirlo.
		if (event.getDate() != null)
			eventDto.setDateInTimeFormat(event.getDateInTimeFormat());
		
		if (event.getEndDate() != null)
			eventDto.setEndDateInTimeFormat(event.getEndDateInTimeFormat());
		
	    return eventDto;
	}
	
	private Event convertToEntity(EventDto eventDto) throws ParseException {
		Event event = new Event();
		
		event.setId(eventDto.getId());
		event.setName(eventDto.getName());
		event.setDescription(eventDto.getDescription());
		
		event.setImportance(eventDto.getImportance());
		event.setLatitude(eventDto.getLatitude());
		event.setLongitude(eventDto.getLongitude());
		
		event.setImageName(eventDto.getImageName());
		// El dto utiliza java.util.date, pero el modelo Event utiliza java.sql.date por la BD. Hay que convertirlo.
		if (eventDto.getDate() != null)
			event.setDateInTimeFormat(eventDto.getDateInTimeFormat());
		
		if (eventDto.getEndDate() != null)
			event.setEndDateInTimeFormat(eventDto.getEndDateInTimeFormat());
		
	    return event;
	}
	
}// de la clase
