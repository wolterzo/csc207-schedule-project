package csc207.schedules.EAZZ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TextualUserInterface
{
  /**
   * Prints a prompt, then reads and returns a line of input.
   * @param eyes
   * @param pen
   * @param prompt
   * @return
   * @throws IOException
   */
  public static String getInput(BufferedReader eyes, PrintWriter pen, String prompt)
    throws IOException
  {
    pen.println(prompt);
    return eyes.readLine();
  } // getInput()

  /**
   * Returns an array (results) of the two file paths needed to create a schedule.
   * results[0] is the file path of the file of potential dates.
   * results[1] is the file path of the file of special dates.
   * @param eyes
   * @param pen
   * @return
   */
  static String[] getFilePaths(BufferedReader eyes, PrintWriter pen)
  {
    String[] results = new String[2];
    while (results[0] == null)
      {
        try
          {
            results[0] =
                getInput(eyes, pen,
                         "Please enter the file path for the file of potential game days:");
          } // try
        catch (IOException e)
          {
            pen.println("Incorrect input, try again.");
          } // catch
      } // while
    while (results[1] == null)
      {
        try
          {
            results[1] =
                getInput(eyes, pen,
                         "Please enter the file path for the file of special dates:");
          } // try
        catch (IOException e)
          {
            pen.println("Incorrect input, try again.");
          } // catch
      } // while
    return results;
  } // getFilePaths

  public static void main(String[] args)
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));
    String[] filePaths = getFilePaths(eyes, pen);
    Schedule schedule = null;
    while (schedule == null)
      {
        try
          {
            schedule = new Schedule(filePaths[0], filePaths[1]);
          } // try
        catch (Exception e)
          {
            pen.println("Incorrect input, try again." + e.getMessage());
            filePaths = getFilePaths(eyes, pen);
          } // catch
      } // while
    schedule.scheduleGames();
    schedule.printSchedule(pen);
  } // main(String[])
} // TextualUserInterface