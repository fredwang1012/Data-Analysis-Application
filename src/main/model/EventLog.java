package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of database events (using implementation from
 * <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">...</a>)
 */
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;                 // singleton event log to store events of running the program
    private Collection<Event> events;               // list of events to store database events

    /**
     * EFFECTS: create a new events list (cannot be called outside of this class)
     */
    private EventLog() {
        events = new ArrayList<>();
    }

    /**
     * MODIFIES: this
     * EFFECTS: gets instance of EventLog or creates it if it doesn't already exist
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    /**
     * MODIFIES: this
     * EFFECTS: adds an event to the event log.
     */
    public void logEvent(Event e) {
        events.add(e);
    }

    /**
     * MODIFIES: this
     * EFFECTS: clears the event log and logs the event.
     */
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: returns the iterator for Event
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
