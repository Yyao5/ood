
/**
* MyCalendar Class
* @author Yutong Yao
* @version 1.0 02/12/22
*/

/**
 * A calendar holds events that can be added or deleted and found by date.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
// import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MyCalendar {
  private Map<LocalDate, ArrayList<Event>> calendar = new HashMap<>();
  private HashSet<LocalDate> recurEventDate = new HashSet<LocalDate>();

  /**
   * Constructs a calendar without any event added.
   */
  public MyCalendar() {
  }

  /**
   * Returns event list on a specific date.
   * 
   * @param the date of interest
   * @return an arraylist of events on a specfic date
   */
  public ArrayList<Event> get(LocalDate date) {
    return calendar.get(date);
  }

  /**
   * Sort the event list.
   * 
   * @param List of events
   */
  private void sortEvents(List<Event> el) {
    Collections.sort(el);
  }

  /**
   * Print single time events.
   * 
   */
  public void printSTEventList() {
    ArrayList<Event> allST = new ArrayList<Event>();
    for (LocalDate key : calendar.keySet()) {
      for (int i = 0; i < calendar.get(key).size(); i++) {
        if (calendar.get(key).get(i).isRecurring()) {
          continue;
        }
        allST.add(calendar.get(key).get(i));
      }
    }
    sortEvents(allST);
    for (int i = 0; i < allST.size(); i++) {
      Event e = allST.get(i);
      System.out.println(e.getEventDate() + " " + e.getEventTitle() + ": " + e.getEventInterval().toString());
    }
  }

  /**
   * Print recurring events.
   * 
   * @para recurDict - recurring event dictionary
   */
  public void printRCEventList(Map<Event, String> recurDict) {
    List<Event> sortedRecur = new ArrayList<Event>();
    sortedRecur = recurDict.keySet().stream().collect(Collectors.toList());
    sortEvents(sortedRecur);

    for (Event key : sortedRecur) {
      if (existRecur(key)) {
        System.out.println(recurDict.get(key));
      }
    }
  }

  /**
   * Check if a recurring event still existed in the calendar.
   * 
   * @para r - event of interest
   * @return boolean true if exists
   */
  private boolean existRecur(Event r) {
    LocalDate d = r.getEventDate();
    for (int i = 0; i < calendar.get(d).size(); i++) {
      if (calendar.get(d).get(i).getEventTitle().equals(r.getEventTitle())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Print all events scheduled on a specific date.
   * 
   * @para d - date of interest
   * 
   */
  public void printByDay(LocalDate d) {
    if (calendar.get(d) == null) {
      System.out.println("No Event is scheduled for today.");
    } else {

      sortEvents(calendar.get(d));
      for (int i = 0; i < calendar.get(d).size(); i++) {
        Event e = calendar.get(d).get(i);
        System.out.println(e.getEventTitle() + ": " + e.getEventInterval().toString());
      }
    }
  }

  /**
   * Print monthly view of event on calendar.
   * 
   * @para month - month of interest to view
   * 
   */
  public void printByMonth(LocalDate month) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    System.out.println(" " + formatter.format(month));
    System.out.print("Su Mo Tu We Th Fr Sa");

    int dayNum = month.lengthOfMonth();
    int startInWeek = month.withDayOfMonth(1).getDayOfWeek().getValue(); // 2
    HashSet<Integer> eventDays = new HashSet<>();
    for (LocalDate key : calendar.keySet()) {
      if (key.getMonthValue() == month.getMonthValue()) {
        Long count = ChronoUnit.DAYS.between(month.withDayOfMonth(1), key.plusDays(1));
        eventDays.add(count.intValue());
      }
    }

    int day = 1; // day value to print
    for (int i = 1; i <= startInWeek + dayNum; i++) {

      if (i % 7 == 1) {
        System.out.println();
      }

      if (i < startInWeek + 1) {
        System.out.print("  ");

      } else {
        if (eventDays.contains(day - 1) && (day / 10 == 0)) {
          System.out.print(" {" + day + "}");
        } else if (eventDays.contains(day - 1)) {
          System.out.print("{" + day + "}");
        } else if (day / 10 == 0) {
          System.out.print(" " + day);
        } else if (day / 10 != 0) {
          System.out.print(day);
        }
        day++;
      }
      System.out.print(" ");
    }
    System.out.println(" ");

  }

  /**
   * Add event to calendar.
   * 
   * @para newEvent - event to add
   * 
   */
  public void addEvent(Event newEvent) {
    boolean flag = false;

    if (calendar.get(newEvent.getEventDate()) == null) {
      calendar.put(newEvent.getEventDate(), new ArrayList<Event>());
      calendar.get(newEvent.getEventDate()).add(newEvent);

    } else {
      ArrayList<Event> existedEvents = calendar.get(newEvent.getEventDate());
      for (int i = 0; i < existedEvents.size(); i++) {

        if (newEvent.getEventInterval().overlapWith(existedEvents.get(i).getEventInterval())) {
          flag = true;
          System.out.println(newEvent.getEventTitle() + newEvent.getEventInterval() + " conflict with "
              + existedEvents.get(i).getEventTitle()
              + existedEvents.get(i).getEventInterval());
          break;
        }
      }
      if (!flag) {
        calendar.get(newEvent.getEventDate()).add(newEvent);

        if (newEvent.isRecurring()) {
          recurEventDate.add(newEvent.getEventDate());
        }
        // System.out.println(newEvent.getEventTitle() + " is successfully created!");
      }

    }

  }

  /**
   * Delete a single time event.
   * 
   * @para selectedDate - date of event to delete, selectedEventName - event title
   *       to delete
   * 
   */
  public void removeSingleEvent(LocalDate selectedDate, String selectedEventName) {
    boolean flag = false;
    for (int i = 0; i < calendar.get(selectedDate).size(); i++) {
      if (calendar.get(selectedDate).get(i).getEventTitle().equals(selectedEventName)
          && (!calendar.get(selectedDate).get(i).isRecurring())) {
        calendar.get(selectedDate).remove(i);
        flag = true;
      }

    }
    if (!flag) {
      System.out.println("No such event with this title on this date.");
    } else {
      System.out.println("Event is removed.");
    }
  }

  /**
   * User overloading to delete single time events on a day.
   * 
   * @para selectedDate - date of event to delete, selectedEventName - event title
   *       to delete
   * 
   */
  public void removeSingleEvent(LocalDate selectedDate) {
    calendar.remove(selectedDate);
    System.out.println("Events on " + selectedDate + " are all removed.");
  }

  /**
   * Delete a recurring event.
   * 
   * @para selectedEventName - event title to delete
   * 
   */
  public void removeRecurrEvent(String selectedEventName) {
    for (LocalDate d : recurEventDate) {
      ArrayList<Event> eventList = calendar.get(d);
      for (int i = 0; i < eventList.size(); i++) {
        if (calendar.get(d).get(i).getEventTitle().equals(selectedEventName)) {
          calendar.get(d).remove(i);
        }
      }
      // recurEventDate.remove(d);
    }
    System.out.println("This recurring event is successfully removed!");
  }
}
