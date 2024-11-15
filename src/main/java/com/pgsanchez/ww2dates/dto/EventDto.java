package com.pgsanchez.ww2dates.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class EventDto implements Serializable{

	private int id;
	@NotBlank(message="{Validation.AddEventForm.NotBlank.Name}")
	private String name;
	private String description;
	@Min(value=1, message="{Validation.AddEventForm.Min.Importance}")
	@Max(value=3, message="{Validation.AddEventForm.Min.Importance}")
	private int importance = 1;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String imageName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate; // se inicializa a 1-0-1900 (el mes va de 0 a 11)
	
	//private MultipartFile image;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public EventDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*public Date getDate() throws ParseException {
        return dateFormat.parse(this.date);
    }

    public void setDate(Date date) {
        this.date = dateFormat.format(date);
    }
    
    public Date getEndDate() throws ParseException {
        return dateFormat.parse(this.endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = dateFormat.format(endDate);
    }*/
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDateInTimeFormat(long time) {
		this.date = new java.util.Date(time);
	}
	
	public long getDateInTimeFormat() {
		if (date != null)
			return date.getTime();
		else
			return -1;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public long getEndDateInTimeFormat() {
		if (endDate != null)
			return endDate.getTime();
		else
			return -1;
	}
	
	public void setEndDateInTimeFormat(long time) {
		this.endDate = new java.util.Date(time);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/*public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}*/
    
    
}
