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
  
  static int MEDIAN_DISTANCE = 217;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
   * Creates a new schedule object given two file paths, one for dates, 
   * and one for special dates. 
   * @param dateFilePath
   * @param specDateFilePath
   * @throws Exception
   */
  public Schedule(String dateFilePath, String specDateFilePath)
                                                               throws Exception
  {
    readInput(dateFilePath, specDateFilePath);
  } // Schedule(String, String)

  public Schedule()
  {
    
  }
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
    for(GameDay gameDay: this.gameDays){
     
      while(gameDay.mustPlay.size() != 0){
        
        //pick a random index to select team from mustPlay
        int randomIndex = random.nextInt(gameDay.mustPlay.size());
        
        //get school at randomIndex as home team
        School home = gameDay.mustPlay.get(randomIndex);
        gameDay.mustPlay.remove(randomIndex);
        
        //get school for away
        
        School away = awayChooser(gameDay.mustPlay,gameDay.canPlay,
                                  home.distances);
        
        //set up match
        gameDay.matches.add(new Match(home,away));      
      }//while
      while(gameDay.canPlay.size() > 0){
      int randIndex = random.nextInt(gameDay.canPlay.size());
      
      //get school at random Index as home team
      
      School home = gameDay.canPlay.get(randIndex);
      gameDay.canPlay.remove(randIndex);
      
      //get away school
      
      School away = awayChooser(gameDay.canPlay,home.distances);
     
      //set up match
      gameDay.matches.add(new Match(home,away));
      
      // when there is only one school left in canPlay
        if(gameDay.canPlay.size() == 1)
          break;
      }//while
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

  // +-----------------+-------------------------------------------------
  // | Local Utilities |
  // +-----------------+
  /**
   * Reads input given two filepaths.
   * @param dateFilePath
   * @param specDateFilePath
   * @throws Exception
   */
  void readInput(String dateFilePath, String specDateFilePath)
    throws Exception
  {
    File dates = new File(dateFilePath);
    File specDates = new File(specDateFilePath);
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
    br.close();
    br2.close();
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
   * Prints the schedule
   * @param pen
   */
  @SuppressWarnings("static-access")
  public void printSchedule(PrintWriter pen)
  {
    //STUB
    for (GameDay day : this.gameDays)
      {
        pen.print(day.date.MONTH + "/" + day.date.DATE + "/" + day.date.YEAR);
        ArrayList<Match> matches = day.matches;
        for(Match m : matches)
          {
            pen.print(" " + m.home.key() + " vs. " + m.away.key());
          } // for
      } // for gameDays

    pen.println("*SCHEDULE GOES HERE*");
  } // printSchedule(PrintWriter)
  
  public School awayChooser(ArrayList<School> mustPlay,
                            ArrayList<School> canPlay,
                            Hashtable<String, Integer> distances){
     Random random = new Random();        
    while((mustPlay.size() > 0) && anyWithinDistance(mustPlay,distances)){
        int r = random.nextInt(mustPlay.size());
        
        /*
         School temp = mustPlay.get(r)
         
         traverse home.distances for key watching temp.key and check value
         against median
         
         if it works
         mustPlay.remove(r)
         return temp;
         
         
         
         
     while(canplay.size > 0 && anyWithinDistance(canPlay, distances){
     
     r = random.nextInt(canPlay.size)
     
     School temp = canPlay.get(r);
     
     tranvers home.distance for temp key and check that value againt
     the median.
     
     if it works
     
     canPlay.remove(r)
     return temp
     }
         */
   
    }//awayChooser
    return null;
  }
  
  public School awayChooser(ArrayList<School> canPlay,
                            Hashtable<String, Integer> distances){
                              return null;
                              
                            }//awayChooser
  
  public boolean anyWithinDistance(ArrayList<School> mustPlay,
                                 Hashtable<String, Integer> distances){
                                   return false;
  }//anyWithDistance
                    
} // Schedule  

