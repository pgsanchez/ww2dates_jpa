package com.pgsanchez.ww2dates.controller;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
		//newEvent.setName("Juan Nadie");
		model.addAttribute("newEvent", newEvent);
		return "addEvent";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddNewEvent(@Valid @ModelAttribute("newEvent") EventDto newEvent, BindingResult result, @ModelAttribute("imageFile") MultipartFile imageFile, HttpServletRequest request) {
	//public String processAddNewEvent(@Valid @ModelAttribute("newEvent") EventDto newEvent, BindingResult result, HttpServletRequest request) {	
 
		if(result.hasErrors()) {
			return "addEvent";
		}
		
		/*if (imageFile.isEmpty())
			System.out.println("Imagen sin nombre");
		if (newEvent.getImageName() == null)
			System.out.println("Nombre Imagen a null");

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
		}*/
		
		// Handle the imageFile separately (e.g., save the file, validate its size/type)
	    // Validate the imageFile
	    if (!imageFile.isEmpty()) {
	        // Example: Check the file size (e.g., max 2MB)
	        long maxFileSize = 2 * 1024 * 1024; // 2 MB
	        if (imageFile.getSize() > maxFileSize) {
	            result.rejectValue("imageFile", "error.imageFile", "File size exceeds the 2MB limit.");
	            return "addEvent";
	        }

	        // Example: Check the file type (e.g., only accept JPEG and PNG)
	        String contentType = imageFile.getContentType();
	        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
	            result.rejectValue("imageFile", "error.imageFile", "Only JPEG and PNG files are accepted.");
	            return "addEvent";
	        }

	        // Save the image file to a specific directory
	        try {
	            String uploadDirectory = "/path/to/upload/directory"; // Replace with actual path
	            String fileName = imageFile.getOriginalFilename();
	            Path path = Paths.get(uploadDirectory + File.separator + fileName);
	            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            result.rejectValue("imageFile", "error.imageFile", "Failed to save the file. Please try again.");
	            return "addEvent";
	        }
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
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String getUpdateEventForm(@ModelAttribute("newEvent") EventDto newEvent, @ModelAttribute("imageFile") MultipartFile imageFile, BindingResult result, HttpServletRequest request) {

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
	
	@RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = fileStorageService.loadAsResource(filename);
		return ResponseEntity.ok().body(file);
	}

	private String getFileExtension(String filename) {
	    if (filename == null) {
	        return null;
	    }
	    int dotIndex = filename.lastIndexOf(".");
	    if (dotIndex >= 0) {
	        return filename.substring(dotIndex + 1);
	    }
	    return "";
	}

	private EventDto convertToDto(Event event) {
		EventDto eventDto = new EventDto();
		
		eventDto.setId(event.getId());
		eventDto.setName(event.getName());
		eventDto.setDescription(event.getDescription());
		
		eventDto.setImportance(event.getImportance());
		eventDto.setLatitude(event.getLatitude());
		eventDto.setLongitude(event.getLongitude());
		
		eventDto.setImageName(event.getImageName());
		eventDto.setDate(event.getDate());
		eventDto.setEndDate(event.getEndDate());
		
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
		event.setDate(eventDto.getDate());
		event.setEndDate(eventDto.getEndDate());
		
	    return event;
	}

	
	/*@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}*/
	
	/*@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationExceptions(MethodArgumentNotValidException ex) {
	    
	    return "addEvent";
	}*/
	
}// de la clase
