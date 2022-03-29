
/**
* MyCalendarTester
* @author Yutong Yao
* @version 1.0 02/12/22
*/

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * A CalendarTester carries out the calendar application per user request.
 */

public class MyCalendarTester {
  public static void main(String[] args) throws IOException {
    LocalDate today = LocalDate.now(); // capture today
    printCalendar(today);
    // read in events.txt
    MyCalendar calendar = new MyCalendar();
    Map<Event, String> recurDict = new HashMap<>();
    System.out.println("Loading events from file...");
    File toLoad = new File("/Users/yyt/Documents/SJSU/OOD/hw1/events.txt");
    loadCalendar(calendar, recurDict, toLoad);
    System.out.println("Loading finished.");
    Scanner sc = new Scanner(System.in);
    String input = "";
    navigate(sc, input, calendar, today, recurDict);
  }

  /**
   * Responds to user selected option.
   * 
   * @param sc - Scanner object, input - user input, calendar - MyCalendar loaded
   *           with events, today - today's date
   * @throws FileNotFoundException
   * 
   */
  private static void navigate(Scanner sc, String input, MyCalendar calendar, LocalDate today,
      Map<Event, String> recurDict) throws FileNotFoundException {

    while (!input.equals("Q")) {

      printMain();
      input = sc.nextLine();

      switch (input) {

        case "V":
          System.out.println("[D]ay view or [M]view ?");
          input = sc.nextLine();
          LocalDate temp = today;
          if (input.equals("D")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMMM dd yyyy");
            System.out.println(" " + formatter.format(today));
            calendar.printByDay(today);
            System.out.println(" ");
            System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
            input = sc.nextLine();
            if (input.equals("G")) {
              navigate(sc, input, calendar, today, recurDict);
            }
            while (input.equals("P")) {
              temp = temp.minusDays(1);
              System.out.println(" " + formatter.format(temp));
              calendar.printByDay(temp);
              System.out.println(" ");
              System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
              input = sc.nextLine();
            }
            while (input.equals("N")) {
              temp = temp.plusDays(1);
              System.out.println(" " + formatter.format(temp));
              calendar.printByDay(temp);
              System.out.println(" ");
              System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
              input = sc.nextLine();
            }

          } else {
            calendar.printByMonth(today);
            System.out.println(" ");
            System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
            input = sc.nextLine();
            if (input.equals("G")) {
              navigate(sc, input, calendar, today, recurDict);
            }
            while (input.equals("P")) {
              temp = temp.minusMonths(1);
              calendar.printByMonth(temp);
              System.out.println(" ");
              System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
              input = sc.nextLine();
            }
            while (input.equals("N")) {
              temp = temp.minusMonths(1);
              calendar.printByMonth(temp);
              System.out.println(" ");
              System.out.println("[P]revious or [N]ext or [G]o back to main menu ? ");
              input = sc.nextLine();
            }

          }
          break;

        case "C":
          System.out.println("Please enter the event title as a string:");
          String name = sc.nextLine();
          System.out.println("Please enter the event date as MM/DD/YYYY:");
          LocalDate date = toDate2(sc.nextLine());
          System.out.println("Please enter the event start time as on a 24 hour clock, xx:xx");
          LocalTime start = LocalTime.parse(sc.nextLine());
          System.out.println("Please enter the event end time as on a 24 hour clock, xx:xx");
          LocalTime end = LocalTime.parse(sc.nextLine());
          TimeInterval ti = new TimeInterval(start, end);
          Event e = new Event(name, ti, date, false);
          calendar.addEvent(e);
          break;

        case "G":
          System.out.println("Please enter the date to go to as MM/DD/YYYY:");
          LocalDate gotodate = toDate2(sc.nextLine());
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMMM dd yyyy");
          System.out.println(" " + formatter.format(gotodate));
          calendar.printByDay(gotodate);
          break;

        case "E":
          System.out.println("ONE TIME EVENTS");
          calendar.printSTEventList();
          System.out.println(" ");
          System.out.println("RECURRING EVENTS");
          calendar.printRCEventList(recurDict);
          System.out.println(" ");
          break;

        case "D":
          System.out.println("Type of event(s) to delete:");
          System.out.println("[S]elected  [A]ll [DR]");
          String todelete = sc.nextLine();
          switch (todelete) {
            case "S":
              System.out.println("Enter the date (dd/mm/yyyy):");
              LocalDate dd = toDate2(sc.nextLine());
              formatter = DateTimeFormatter.ofPattern("EEE, MMMM dd yyyy");
              System.out.println(" " + formatter.format(dd));
              calendar.printByDay(dd);
              System.out.println("Enter the name of the event to delete:");
              String n = sc.nextLine();
              calendar.removeSingleEvent(dd, n);
              break;

            case "A":
              System.out.println("Enter the date (dd/mm/yyyy):");
              LocalDate d = toDate2(sc.nextLine());
              calendar.removeSingleEvent(d);
              break;

            case "DR":
              System.out.println("Enter the name of a RECURRING event:");
              String recurname = sc.nextLine();
              calendar.removeRecurrEvent(recurname);
              break;
          }

      }
    }

    sc.close();
    System.out.println("Good Bye.");
    writeFile(calendar, recurDict);

  }

  /**
   * Loads calendar with events.
   * 
   * @param startCalendar - calendar object, dict - hashmap to store recurring
   *                      events
   * @throws FileNotFoundException if file not found
   */

  private static void loadCalendar(MyCalendar startCalendar, Map<Event, String> dict, File f)
      throws FileNotFoundException {
    File file = f;
    Scanner sc = new Scanner(file);
    while (sc.hasNextLine()) {
      String name = sc.nextLine();
      Scanner info = new Scanner(sc.nextLine());
      String first = info.next();
      if (Character.isDigit(first.charAt(0))) {
        LocalDate date = toDate(first);
        LocalTime start = LocalTime.parse(info.next());
        LocalTime end = LocalTime.parse(info.next());
        TimeInterval interval = new TimeInterval(start, end);
        Event event = new Event(name, interval, date, false);
        startCalendar.addEvent(event);
      } else {
        String st = info.next();
        LocalTime start = LocalTime.parse(st);
        String ed = info.next();
        LocalTime end = LocalTime.parse(ed);
        TimeInterval interval = new TimeInterval(start, end);
        String fd = info.next();
        LocalDate firstdate = toDate(fd);
        String ld = info.next();
        LocalDate enddate = toDate(ld);

        Event tmp = new Event(name, interval, firstdate, true);
        dict.put(tmp, name + " " + first + " " + st + " " + ed + " " + fd + " " + ld);
        ArrayList<Integer> recurval = new ArrayList<>();
        for (int d = 0; d < first.length(); d++) {
          recurval.add(toDay(first.substring(d, d + 1)));
        }
        for (int i = 0; i < recurval.size(); i++) {
          if (i != 0) {
            firstdate = firstdate.plusDays(recurval.get(i) - recurval.get(i - 1));
          }
          for (LocalDate datetemp = firstdate; datetemp.isBefore(enddate); datetemp = datetemp.plusDays(7)) {
            Event recur = new Event(name, interval, datetemp, true);
            startCalendar.addEvent(recur);
          }

        }

      }

    }

    sc.close();
  }

  /**
   * Converts string "MM/dd/yy" to date.
   * 
   * @return a date
   */
  private static LocalDate toDate(String s) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    formatter = formatter.withLocale(Locale.US);
    LocalDate date = LocalDate.parse(s, formatter);
    return date;

  }

  /**
   * Converts string "MM/dd/yyyy" to date.
   * 
   * @return a date
   */
  private static LocalDate toDate2(String s) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    formatter = formatter.withLocale(Locale.US);
    LocalDate date = LocalDate.parse(s, formatter);
    return date;

  }

  /**
   * Writes current events to file output.txt.
   * 
   * @param calendar, recurDict - recurring event dictionary,
   * @throws FileNotFoundException
   * 
   */
  private static void writeFile(MyCalendar calendar, Map<Event, String> recurDict) throws FileNotFoundException {

    File file = new File("/Users/yyt/Documents/SJSU/OOD/hw1/output.txt");

    PrintStream stream = new PrintStream(file);
    System.out.println("Saving file ...");
    System.setOut(stream);

    calendar.printSTEventList();
    calendar.printRCEventList(recurDict);
  }

  /**
   * Converts a day of week to string.
   * 
   * @return a value representing day. 1 - Monday
   */

  private static Integer toDay(String s) {
    int day = 0;
    switch (s) {
      case "M":
        day = 1;
        break;
      case "T":
        day = 2;
        break;
      case "W":
        day = 3;
        break;
      case "R":
        day = 4;
        break;
      case "F":
        day = 5;
        break;
      case "A":
        day = 6;
        break;
      case "S":
        day = 7;
        break;
    }
    return day;
  }

  /**
   * Shows the main menue.
   * 
   * 
   */
  private static void printMain() {
    System.out.println("Select one of the following main menu options:");
    System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
  }

  /**
   * Shows the default calendar.
   * 
   * 
   */
  public static void printCalendar(LocalDate c) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    System.out.println(" " + formatter.format(c));
    System.out.print("Su Mo Tu We Th Fr Sa");

    int dayNum = c.lengthOfMonth();
    int startInWeek = c.withDayOfMonth(1).getDayOfWeek().getValue(); // 2
    int curDay = c.getDayOfMonth();

    int day = 1; // day value to print
    for (int i = 1; i <= startInWeek + dayNum; i++) {

      if (i % 7 == 1) {
        System.out.println();
      }

      if (i < startInWeek + 1) {
        System.out.print("  ");

      } else {
        if ((day != curDay) && (day / 10 == 0)) {
          System.out.print(" " + day);
        } else if ((day != curDay) && (day / 10 != 0)) {
          System.out.print(day);
        } else if ((day == curDay) && (day / 10 == 0)) {
          System.out.print(" {" + day + "}");
        } else {
          System.out.print("{" + day + "}");
        }
        day++;
      }
      System.out.print(" ");
    }
    System.out.println(" ");
  }

}
