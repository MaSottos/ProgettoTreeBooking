package it.corsobackendtree.treebooking.views;

import java.time.LocalDateTime;

public class EventView {
	private String name;
	private Integer capacity;
	private String place;
	private LocalDateTime date;

	public EventView(String name, Integer capacity, String place, LocalDateTime date) {
		this.name = name;
		this.capacity = capacity;
		this.place = place;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public String getPlace() {
		return place;
	}

	public LocalDateTime getDate() {
		return date;
	}
}
