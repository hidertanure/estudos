package br.com.hider.valetine.model;

import java.util.Date;

public class Event extends Entity<Event> {
	
	private Long id;
	
	private Date eventDate;
	
	private String name;
	
	private String description;
	
	public Event(){
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
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
	
}
