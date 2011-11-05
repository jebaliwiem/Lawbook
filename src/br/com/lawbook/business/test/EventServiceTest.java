package br.com.lawbook.business.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.lawbook.business.EventService;
import br.com.lawbook.business.ProfileService;
import br.com.lawbook.model.Event;
import br.com.lawbook.model.Profile;

/**
 * @author Edilson Luiz Ales Junior
 * @version 05NOV2011-03
 * 
 */
public class EventServiceTest {
	
	private final static Logger LOG = Logger.getLogger("ProfileServiceTest");

	@BeforeClass
	public static void createEvents() {
		Event event;
		for (int i = 0; i < 5; i++) {
			event = new Event();
			event.setCreator(ProfileService.getInstance().getProfileByUserName("admin"));
			event.setTitle(i + " - Meeting");
			event.setContent("Meeting about documents " + i);
			event.setAllDay(false);
			event.setStartDate(getDate(i + "/11/2011 09:00"));
			event.setEndDate(getDate((i+1) + "/11/2011 08:00"));
			create(event);
		}
	}
	
	@Test
	public void update() {
		String newEventTitle = "New Meeting - " + Calendar.getInstance().getTimeInMillis();
		try {
			Event event = null;
			event = EventService.getInstance().getEventById(1L);
			assertFalse(event.getTitle().equals(newEventTitle));
			
			event.setTitle(newEventTitle);
			event.setStartDate(addDays(event));
			event.setEndDate(addDays(event));
			EventService.getInstance().update(event);
			
			event = null;
			event = EventService.getInstance().getEventById(1L);
			assertTrue(event.getTitle().equals(newEventTitle));
			
			LOG.info("Events was successfully updated");
		} catch (IllegalArgumentException e) {
			LOG.severe("Error on attributes, please check parameters: " + e.getMessage());
			fail();
		} catch (HibernateException e) {
			LOG.severe("Error updating event: " + e.getMessage());
			fail();
		}
	}
	
	@Test
	public void delete() {
		try {
			Profile creator = ProfileService.getInstance().getProfileByUserName("admin");
			Event event = EventService.getInstance().getProfileEvents(creator).get(0);
			Long eventId = event.getEventId();
			
			EventService.getInstance().delete(event);
			assertNull(EventService.getInstance().getEventById(eventId));
			LOG.info("Event was successfully deleted");
		} catch (IllegalArgumentException e) {
			LOG.severe("Error on attribute, please check parameter: " + e.getMessage());
			fail();
		} catch (HibernateException e) {
			LOG.severe("Error getting event by event id: " + e.getMessage());
			fail();
		}
	}
	
	@Test
	public void getEventById() {
		try {
			Profile creator = ProfileService.getInstance().getProfileByUserName("admin");
			Long eventId = EventService.getInstance().getProfileEvents(creator).get(0).getEventId();
			Event event = EventService.getInstance().getEventById(eventId);
			assertNotNull(event.getEventId());
			LOG.info("Event was fetched successfully");
		} catch (IllegalArgumentException e) {
			LOG.severe("Error on attribute, please check parameter: " + e.getMessage());
			fail();
		} catch (HibernateException e) {
			LOG.severe("Error getting event by event id: " + e.getMessage());
			fail();
		}
	}
	
	@Test
	public void populateSchedule() {
		List<Event> newEvents = null;
		try {
			Profile creator = ProfileService.getInstance().getProfileByUserName("admin");
			newEvents = EventService.getInstance().getProfileEvents(creator);
			assertNotNull(newEvents);
			LOG.info(creator.getFirstName() + "'s events were fetched successfully");
		} catch (IllegalArgumentException e) {
			LOG.severe("Error on attributes, please check parameters: " + e.getMessage());
			fail();
		} catch (HibernateException e) {
			LOG.severe("Error getting events from specified creator: " + e.getMessage());
			fail();
		}
	}
	
	private static void create(Event event) {
		try {
			EventService.getInstance().create(event);
			assertNotNull(event.getId());
			LOG.info("Event created successfully");
		} catch (IllegalArgumentException e) {
			LOG.severe("Error on attributes, please check parameters: " + e.getMessage());
			fail();
		} catch (HibernateException e) {
			LOG.severe("Error saving events: " + e.getMessage());
			fail();
		}
	}
	
	private static Date getDate(String dateString) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			c.setTime(df.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
			LOG.severe(e.getMessage());
			fail();
		}
		return c.getTime();
	}
	
	private Date addDays(Event event) {
		Calendar c = Calendar.getInstance();
		c.setTime(event.getStartDate());
		c.add(Calendar.DAY_OF_MONTH, 2);
		return c.getTime();
	}
	
}
