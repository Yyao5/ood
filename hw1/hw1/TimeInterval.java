
/**
* TimeInterval Class
* @author Yutong Yao
* @version 1.0 02/12/22
*/

import java.time.LocalTime;

public class TimeInterval {
  private LocalTime start;
  private LocalTime end;

  /**
   * Constructs a time interval class with startTime and endTime.
   */
  public TimeInterval(LocalTime startTime, LocalTime endTime) {
    start = startTime;
    end = endTime;
  }

  /**
   * Convert a TimeInterval to string.
   * 
   * @return string with format startTime - endTime
   */
  public String toString() {
    return start + " - " + end;
  }

  /**
   * Checks if a TimeInterval overlaps / conflicts with another.
   * 
   * @param otherInterval - TimeInterval to compare
   * @return true if the TimeInterval overlaps
   */
  public boolean overlapWith(TimeInterval otherInterveral) {

    if (start.isAfter(otherInterveral.end) || start.equals(otherInterveral.end) || end.isBefore(otherInterveral.start)
        || end.equals(otherInterveral.start)) {
      return false;

    } else {
      return true;
    }

  }

  /**
   * Get the start time of an interval.
   * 
   * @return start time
   */
  public LocalTime getStart() {
    return start;
  }

  /**
   * Get the end time of an interval.
   * 
   * @return end time
   */
  public LocalTime getEnd() {
    return end;
  }
}
