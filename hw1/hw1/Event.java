
/**
* Event Class
* @author Yutong Yao
* @version 1.0 02/12/22
*/

/**
 * A event has a title, a date, a time interval and a recurring label.
 */

import java.time.LocalDate;

public class Event implements Comparable<Object> {
  private String title;
  private TimeInterval interval;
  private LocalDate date;
  private boolean recurring;

  /**
   * Constructs an event with eventName, eventInterval, eventDate, and isRecurring
   * label.
   */

  public Event(String eventName, TimeInterval eventInterval, LocalDate eventDate, boolean isRecurring) {
    title = eventName;
    interval = eventInterval;
    date = eventDate;
    recurring = isRecurring;
  }

  /**
   * Get event title.
   * 
   * 
   */
  public String getEventTitle() {
    return title;
  }

  /**
   * Get event interval.
   * 
   * 
   */
  // return event time
  public TimeInterval getEventInterval() {
    return interval;
  }

  /**
   * Get event date.
   * 
   * 
   */
  public LocalDate getEventDate() {
    return date;
  }

  /**
   * Get event is recurring or not.
   * 
   * @return true if the event is recurring
   */
  // return event recurring or not
  public boolean isRecurring() {
    return recurring;
  }

  /**
   * Use override to define the order of events.
   * 
   * @param o1 - the other event to compare
   * @return -1 if this event is first, 1 if second, 0 if same
   */
  @Override
  public int compareTo(Object o1) {
    Event other = (Event) o1;
    if (date.isBefore(other.getEventDate())) {
      return -1;
    } else if (date.isAfter(other.getEventDate())) {
      return 1;
    } else {
      if (interval.getStart().isAfter(other.getEventInterval().getStart())) {
        return 1;
      } else if (interval.getStart().isBefore(other.getEventInterval().getStart())) {
        return -1;
      } else {
        return 0;
      }
    }
  }

}
