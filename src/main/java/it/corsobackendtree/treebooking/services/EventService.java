package it.corsobackendtree.treebooking.services;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import it.corsobackendtree.treebooking.views.EventView;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    public EventView getEventView(EventDAO eventDAO, UserDAO user){
        return new EventView(eventDAO.getId(), eventDAO.getOwned(user), eventDAO.getJoined(user),
                eventDAO.getName(),eventDAO.getDatetime(), eventDAO.getPlace(), eventDAO.getCapacity());
    }
}
