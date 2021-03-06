package csc207.schedules.EAZZ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Schedule
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+
  /**
   * Collection of schools in the conference
   */
  ArrayList<School> schools = new ArrayList<School>();
  /**
   * Collection of gamedays in the season
   */
  ArrayList<GameDay> gameDays = new ArrayList<GameDay>();

  /**
   * Median school distances
   */
  static int MEDIAN_DISTANCE = 270;

  /**
   * Distance used for filtering schools by distance
   */
  int max_distance = MEDIAN_DISTANCE;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
   * Creates a new schedule object given three file paths, one for dates, 
   * one for special dates, and one for constraints. 
   * @param dateFilePath
   * @param specDateFilePath
   * @throws Exception
   */
  public Schedule(String dateFilePath, String specDateFilePath,
                  String constraintsFilePath) throws Exception
  {
    readInput(dateFilePath, specDateFilePath, constraintsFilePath);
  } // Schedule(String, String)

  /**
   * Creates a blank schedule object.
   */
  public Schedule()
  {
  } // Schedule()

  // +----------+-------------------------------------------------
  // | Mutators |
  // +----------+ 
  /**
   * Schedules games given the schools and dates in the schedule and sets
   * matches within gameDays.
   */
  public void scheduleGames()
  {
    Random random = new Random();
    int numGames = 0;
    for (GameDay gameDay : this.gameDays)
      { 
        while (!gameDay.mustPlay.isEmpty())
          {
            //pick a random index to select team from mustPlay
            int randomIndex = random.nextInt(gameDay.mustPlay.size());

            //get school at randomIndex as home team
            School home = gameDay.mustPlay.get(randomIndex);
            School away = awayChooser(home, gameDay);
            if (away != null)
              {
                //add the match to the game day.
                gameDay.matches.add(new Match(home, away));
                //remove home from gameDay
                gameDay.removeSchool(home);
                //remove away from gameDay
                gameDay.removeSchool(away);

                home.updateHistory(away);
                away.updateHistory(home);
              } // if not null
            else
              {
                break;
              } // else
          } // while mustPLay
        while (!gameDay.canPlay.isEmpty())
          {
            //pick a random index to select team from mustPlay
            int randomIndex = random.nextInt(gameDay.canPlay.size());

            //get school at randomIndex as home team
            School home = gameDay.canPlay.get(randomIndex);

            School away = awayChooser(home, gameDay);
            if (away != null)
              {
                //add the match to the game day.
                gameDay.matches.add(new Match(home, away));
                //remove home from gameDay
                gameDay.removeSchool(home);
                //remove away from gameDay
                gameDay.removeSchool(away);

                home.updateHistory(away);
                away.updateHistory(home);
              } // if not null
            else
              {
                break;
              } // else
          } // while
      }//for
  } // scheduleGames()

  /**
   * adds the school to the list of schools.
   * @param school
   */
  public void addSchool(School school)
  {
    this.schools.add(school);
  } // addSchool(School)

  /**
   * Given a home school and a certain GameDay, it returns either a team that
   * home can play on that day, or null if there is no team that meets the 
   * constraints.
   * @param home
   * @param gameDay
   * @return
   */
  public School awayChooser(School home, GameDay gameDay)
  {
    ArrayList<School> possible = new ArrayList<School>();
    //*May want to only add one of the arrays to possible*//
    possible.addAll(home.haveNotPlayed);
    possible.addAll(home.havePlayedOnce);
    if (!gameDay.isWeekend())
      {
        possible = allWithinDistance(possible, home.distances);
      } // if
    if (gameDay.mustPlay.isEmpty())
      {
        possible.retainAll(gameDay.canPlay);
      } // if
    else
      {
        possible.retainAll(gameDay.mustPlay);
        if (possible.isEmpty())
          {
            possible.addAll(home.haveNotPlayed);
            possible.addAll(home.havePlayedOnce);
            if (!gameDay.isWeekend())
              {
                possible = allWithinDistance(possible, home.distances);
              } // if
            possible.retainAll(gameDay.canPlay);
          } // if there are no other mustPlay teams
      } // else
    if (possible.isEmpty())
      {
        return null;
      } // if
    else
      {
        Random random = new Random();
        int r = random.nextInt(possible.size());
        return possible.get(r);
      } // else
  } // awayChooser(School, GameDay)


  /**
   * Takes a list of schools and a hashtable of distances and returns an 
   * ArrayList of all the schools that are less than the MEDIAN_DISTANCE.
   * @param list
   * @param distances
   * @return
   */
  public ArrayList<School>
    allWithinDistance(ArrayList<School> list,
                      Hashtable<String, Integer> distances)
  {
    ArrayList<School> newList = new ArrayList<School>();
    for (School school : list)
      {
        if (distances.get(school.key()) < this.MEDIAN_DISTANCE)
          {
            newList.add(school);
          } // if
      } // for
    return newList;
  } // allWithinDistance

  // +-----------------+-------------------------------------------------
  // | Local Utilities |
  // +-----------------+
  /**
   * Reads input given three filepaths.
   * @param dateFilePath
   * @param specDateFilePath
   * @param constraintsFilePath
   * @throws Exception
   */
  void readInput(String dateFilePath, String specDateFilePath,
                 String constraintsFilePath)
    throws Exception
  {
    File dates = new File(dateFilePath);
    File specDates = new File(specDateFilePath);
    File constraints = new File(constraintsFilePath);
    String result;
    BufferedReader br = new BufferedReader(new FileReader(specDates));
    result = br.readLine();
    while ((result = br.readLine()) != null)
      {
        loadSpecDates(result);
      } // while
    BufferedReader br2 = new BufferedReader(new FileReader(dates));
    while ((result = br2.readLine()) != null)
      {
        loadDates(result);
      } // while
    // get constraints arrays
    BufferedReader br3 = new BufferedReader(new FileReader(constraints));
    String schoolKeys = br3.readLine();
    String playOnce1 = br3.readLine();
    String playOnce2 = br3.readLine();
    loadConstraints(schoolKeys, playOnce1, playOnce2);
    br.close();
    br2.close();
    br3.close();
  } // readInput(String, String)

  /**
   * Sets information in the object based on information in the special dates
   * file.
   * @param result
   * @throws Exception
   */
  void loadSpecDates(String result)
    throws Exception
  {
    String[] tmp = result.split("\\s+");
    GameDay gameDay = new GameDay(tmp[0]);
    for (int i = 1; i < tmp.length; i++)
      {
        gameDay.setAvailability(tmp[i], this.schools.get(i - 1));
      } // for
    this.gameDays.add(gameDay);
  } // loadSpecDates(String)

  /**
   * Sets information in the object based on information in the dates file.
   * @param result
   * @throws Exception 
   */
  void loadDates(String result)
    throws Exception
  {
    GameDay gameDay = new GameDay(result);
    gameDay.setAvailability("M", this.schools);
    this.gameDays.add(gameDay);
  } // loadDates(String)

  /**
   * Sets information in the object based on the information in the 
   * constraints file. 
   * @param keys
   * @param playOnce1
   * @param playOnce2
   * @throws Exception
   */
  void loadConstraints(String keys, String playOnce1, String playOnce2)
    throws Exception
  {
    String[] schoolKeys = keys.split("\\s+");
    String[] playOnceKeys1 = playOnce1.split("\\s+");
    String[] playOnceKeys2 = playOnce2.split("\\s+");

    for (int i = 0; i < schoolKeys.length; i++)
      {
        String schoolKey = schoolKeys[i];
        School school = School.getByKey(this.schools, schoolKey);
        school.updateHistory(School.getByKey(this.schools, playOnceKeys1[i]));
        school.updateHistory(School.getByKey(this.schools, playOnceKeys2[i]));
      } // for
  } // loadConstraints(Strinng, ArrayList<String>)

  /**
   * Prints the schedule
   * @param pen
   */
  @SuppressWarnings("static-access")
  public int printSchedule(PrintWriter pen)
  {
    int numGames = 0;
    for (GameDay day : this.gameDays)
      {
        // pen.println(day.date.MONTH + "/" + day.date.DATE + "/" + day.date.YEAR);
        pen.println(day.date.getTime());
        ArrayList<Match> matches = day.matches;
        for (Match m : matches)
          {
            pen.print(" " + m.home.key() + " vs. " + m.away.key() + " "
                      + m.home.distances.get(m.away.key()) + " mi.");
            pen.println();
            numGames++;
          } // for
      } // for gameDays
    pen.println("Number of games: " + numGames);
    return numGames;
  } // printSchedule(PrintWriter)

  void printInitialStateSchols()
  {
    //go through all the schools and print out havenotPlayed playedOnce playedTwice
    int length = this.schools.size();
    System.out.println();
    System.out.println("-------------------------------");
    for (int i = 0; i < length; i++)
      {
        School temp = this.schools.get(i);

        System.out.println("School info for " + temp.name);

        System.out.println("haveNotPlayed ...");

        for (School school : temp.haveNotPlayed)
          {
            System.out.println(school.name);
          }//for
        System.out.println();
        System.out.println("havePlayedOnce ...");
        for (School school : temp.havePlayedOnce)
          {
            System.out.println(school.name);
          }//for
        System.out.println();
        System.out.println("havePlayedTwice ...");
        for (School school : temp.havePlayedTwice)
          {
            System.out.println(school.name);
          }//for
        System.out.println();
        System.out.println("-------------------------------");
      }//for
  }//printInitialState

  void printInitialStateGameDay()
  {
    //go through all the schools and print out mustPlay, canPlay,cannotPlay, matches
    int length = this.gameDays.size();
    System.out.println();
    System.out.println("-------------------------------");
    for (int i = 0; i < length; i++)
      {
        GameDay temp = this.gameDays.get(i);

        System.out.println("GameDay  info for " + temp.date.getTime());

        System.out.println("mustPlay ...");

        for (School school : temp.mustPlay)
          {
            System.out.println(school.name);
          }//for
        System.out.println();
        System.out.println("canPlay ...");
        for (School school : temp.canPlay)
          {
            System.out.println(school.name);
          }//for
        System.out.println();
        System.out.println("cannotPlay ...");
        for (School school : temp.cannotPlay)
          {
            System.out.println(school.name);
          }//for

        ArrayList<Match> matches = temp.matches;
        for (Match m : matches)
          {
            System.out.println(" " + m.home.key() + " vs. " + m.away.key()
                               + " " + m.home.distances.get(m.away.key())
                               + " mi.");
            System.out.println();
          } // for

        System.out.println();
        System.out.println("-------------------------------");

      }//for
  }//printInitialState
} // Schedule  

