package model;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a database event (using implementation from
 * <a href="https://github.students.cs.ubc.ca/CPSC210/AlarmSystem">...</a>)
 */
public class Event {
    private static final int HASH_CONSTANT = 13;    // hashcode hash constant
    private final Date dateLogged;                        // date of this event
    private final String description;                     // description of this event

    /**
     * EFFECTS: creates an event with the given description and the current date/time stamp
     */
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    /**
     * EFFECTS: returns the date of this event (includes time)
     */
    public Date getDate() {
        return dateLogged;
    }

    /**
     * EFFECTS: returns the description of this event
     */
    public String getDescription() {
        return description;
    }

    /**
     * EFFECTS: returns true if given object has same date and description as this event
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged) && this.description.equals(otherEvent.description));
    }

    // EFFECTS: override default Event hashcode
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    // EFFECTS: override default Event toString to return String of date and description
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
