package it.corsobackendtree.treebooking.views;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventView {
	private UUID eventid;
	private Boolean owned;
	private Boolean joined;
	private String name;
	private LocalDateTime date;
	private String place;
	private Integer capacity;

	public EventView(UUID eventid, Boolean owned, Boolean joined,
					 String name, LocalDateTime date, String place, Integer capacity) {
		this.eventid = eventid;
		this.owned = owned;
		this.joined = joined;
		this.name = name;
		this.date = date;
		this.place = place;
		this.capacity = capacity;
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

	public UUID getEventid() {
		return eventid;
	}

	public Boolean getOwned() {
		return owned;
	}

	public Boolean getJoined() {
		return joined;
	}

	@Override
	public String toString() {
		return "EventView{" +
				"eventid=" + eventid +
				", owned=" + owned +
				", joined=" + joined +
				", name='" + name + '\'' +
				", date=" + date +
				", place='" + place + '\'' +
				", capacity=" + capacity +
				'}';
	}
}
