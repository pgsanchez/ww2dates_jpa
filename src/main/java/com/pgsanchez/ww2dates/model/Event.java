package com.pgsanchez.ww2dates.model;

import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "events")
public class Event {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "importance")
	private int importance = 1;
	@Column(name = "latitude")
	private double latitude = 0.0;
	@Column(name = "longitude")
	private double longitude = 0.0;
	@Column(name = "image")
	private String imageName;
	@Column(name = "date")
	private Date date;
	@Column(name = "end_date")
	private Date endDate; // se inicializa a 1-0-1900 (el mes va de 0 a 11)
	
	
	public Event() {
	}


	public Event(String name, String description, int importance, double latitude, double longitude, String imageName,
			Date date, Date endDate) {
		super();
		this.name = name;
		this.description = description;
		this.importance = importance;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageName = imageName;
		this.date = date;
		this.endDate = endDate;
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


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDateInTimeFormat(long time) {
		this.date = new Date(time);
	}
	
	public long getDateInTimeFormat() {
		if (this.date != null)
			return date.getTime();
		else
			return -1;
	}
	
	public void setEndDateInTimeFormat(long time) {
			this.endDate = new Date(time);
	}
	
	public long getEndDateInTimeFormat() {
		if (this.endDate != null)
			return endDate.getTime();
		else
			return -1;
	}
}
