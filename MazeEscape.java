import java.util.Scanner;

public class MazeEscape
{
 //instance variables
  private Scanner input = new Scanner(System.in);
  private static int rows = 10;
  private static int columns = 9;
  private String[][] mazeArray = new String[rows][columns];
  private static int count;
  private String wallMarker = " * ";
  private String emptySpaceMarker = "   ";
  private int userInitialPosition_X;
  private int userInitialPosition_Y;
  private int userPosition_X;
  private int userPosition_Y;
  private String initialPositionMarker = " o ";
  private String previousPositionMarker = " # "; 
  private String deadEndPositionMarker = " X ";
  private int exitPosition_X = 2;
  private int exitPosition_Y = 1;
  private static int runCount = 0; // strictly for use in setUserStartingPosition method only
  private static boolean isExit;
  private static boolean noEscape;
  private static int displayCounter = 0; // strictly for one-time use in exitCheck's final if statement only
  
  // constructor that manually constructs a maze ----------------------------------------------------------------------------------
  public MazeEscape()
  {
    //initialize the mazeArray with spaces
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < columns; j++)
      {
        mazeArray[i][j] = emptySpaceMarker;
      }
    }
    
    // Attach labels
    for (count = 1; count < rows; count++)
    {
      mazeArray[count][0] = " " + String.valueOf(count) + " ";
    }
    
    for (count = 1; count < columns; count++)
    {
      mazeArray[0][count] = " " + String.valueOf(count) + " ";
    }
    
    // form left and right walls
    for (count = 1; count < rows; count++)
    {
      mazeArray[count][1] = wallMarker; // moves down the first column
      mazeArray[count][columns-1] = wallMarker; // moves down the last column
    }
    // form top and bottom walls
    // Open one "hole" in top wall for the exit
    for (count = 1; count < columns; count++)
    {
      mazeArray[rows-1][count] = wallMarker; // moves across last row
      mazeArray[1][count] = wallMarker; // moves across top row
      mazeArray[exitPosition_Y][exitPosition_X] = emptySpaceMarker; // Creates a hole on the top row for the exit
    }
    
    // Insert * to form walls elsewhere
    // Row 2 inbetween
    mazeArray[2][columns-3] = wallMarker;
    
    // Row 3 inbetween
    for(count = 3; count < columns - 2; count++)
    {
      mazeArray[3][count] = wallMarker;
    }
    
    // Row 4 inbetween
    mazeArray[4][3] = wallMarker;
    mazeArray[4][5] = wallMarker;
    
    // Row 5 inbetween
    mazeArray[5][3] = wallMarker;
    for (count = 5; count < columns - 1; count++)
    {
      mazeArray[5][count] = wallMarker;
    }
    
    // Row 6 inbetween
    mazeArray[6][5] = wallMarker;
    
    // Row 7 inbetween
    mazeArray[7][2] = wallMarker;
    for (count = 3; count <= 7; count = count + 2)
    {
      mazeArray[7][count] = wallMarker;
    }
    
    // Row 8 inbetween
    mazeArray[8][columns-2] = wallMarker;
  }
  
  // Displays the mazeArray
  public void getMazeArray()
  {
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < columns; j++)
      {
        System.out.print(mazeArray[i][j]);
      }
      System.out.println();
    }
  }
  
  // User inputs where they want to start --------------------------------------------------------------------------------------
  public void setUserStartingPosition()
  {
    System.out.println("Where would you like to start in the maze?");

    System.out.println("What row would you like to start in?");
    userInitialPosition_Y = input.nextInt();
    // User can't start in the top or bottom row, since they're walls
    while (userInitialPosition_Y == 1 || userInitialPosition_Y == 9)
    {
      System.out.println("Invalid starting row.");
      System.out.println("What row would you like to start in?");
      userInitialPosition_Y = input.nextInt();
    }
    // User can't start in the left or right columns, since they're walls
    System.out.println("What column would you like to start in?");
    userInitialPosition_X = input.nextInt();
    while (userInitialPosition_X == 1 || userInitialPosition_X == 8)
    {
      System.out.println("Invalid starting column.");   
      System.out.println("What column would you like to start in?");
      userInitialPosition_X = input.nextInt();      
    }
    
    // If the (x,y) of the user is a wall, run the method again for valid input
    if (mazeArray[userInitialPosition_Y][userInitialPosition_X].equals(wallMarker))
    {
      System.out.println("Invalid starting position.");
      System.out.println("Please try again.");
      setUserStartingPosition();
    }
    
    // Mark the user's starting position
    // Set user's position to the starting position
    // for the recursion process
    mazeArray[userInitialPosition_Y][userInitialPosition_X] = initialPositionMarker;
    userPosition_X = userInitialPosition_X;
    userPosition_Y = userInitialPosition_Y;    
    
    // After breaking through the input check, method may run more than once
    // runCount condition will limit to only one display
    if (runCount == 0)
    {
      getMazeArray();
    }
    runCount++;
  }
  
  // Recursive method that checks for a way out of the maze--------------------------------------------------------------------------------------------------
  public void exitCheck()
  { 
    // check for empty space above current position
    if (mazeArray[userPosition_Y - 1][userPosition_X].equals(emptySpaceMarker))
    {
      if (!mazeArray[userPosition_Y][userPosition_X].equals(initialPositionMarker))
      {
        // place a marker at the last location to prevent backtracking
        mazeArray[userPosition_Y][userPosition_X] = previousPositionMarker;
      }
      // moves the user's position upwards
      userPosition_Y -= 1;
    } else if (mazeArray[userPosition_Y][userPosition_X + 1].equals(emptySpaceMarker)) // check to the right
    {
      if (!mazeArray[userPosition_Y][userPosition_X].equals(initialPositionMarker))
      {
        mazeArray[userPosition_Y][userPosition_X] = previousPositionMarker;
      }
      // moves the user's position right
      userPosition_X += 1;
    } else if (mazeArray[userPosition_Y + 1][userPosition_X].equals(emptySpaceMarker)) // check below
    {
       if (!mazeArray[userPosition_Y][userPosition_X].equals(initialPositionMarker))
      {
        mazeArray[userPosition_Y][userPosition_X] = previousPositionMarker;
      }
      // moves the user's position downwards
      userPosition_Y += 1;
    } else if (mazeArray[userPosition_Y][userPosition_X - 1].equals(emptySpaceMarker)) // check to the left
    {
      if (!mazeArray[userPosition_Y][userPosition_X].equals(initialPositionMarker))
      {
        mazeArray[userPosition_Y][userPosition_X] = previousPositionMarker;
      }
      // moves the user's position left
      userPosition_X -= 1; 
    } else // if all conditions fail, then a dead end has been reached and the user needs to backtrack-----------------------------------------------------------------------------------
    {
      // backtrack up?
      if (!mazeArray[userPosition_Y - 1][userPosition_X].equals(deadEndPositionMarker) && !mazeArray[userPosition_Y - 1][userPosition_X].equals(wallMarker))
      {
        mazeArray[userPosition_Y][userPosition_X] = deadEndPositionMarker;
        // moves user up
        userPosition_Y -= 1;
      } else if (!mazeArray[userPosition_Y][userPosition_X + 1].equals(deadEndPositionMarker) && !mazeArray[userPosition_Y][userPosition_X + 1].equals(wallMarker)) // backtrack right
       {
            mazeArray[userPosition_Y][userPosition_X] = deadEndPositionMarker;
          // moves user right
          userPosition_X += 1;
      } else if (!mazeArray[userPosition_Y + 1][userPosition_X].equals(deadEndPositionMarker) && !mazeArray[userPosition_Y + 1][userPosition_X].equals(wallMarker)) // backtrack below
       {
         mazeArray[userPosition_Y][userPosition_X] = deadEndPositionMarker;
        // moves user down
        userPosition_Y += 1;
      } else if (!mazeArray[userPosition_Y][userPosition_X - 1].equals(deadEndPositionMarker) && !mazeArray[userPosition_Y][userPosition_X - 1].equals(wallMarker)) // backtrack left
       {
         mazeArray[userPosition_Y][userPosition_X] = deadEndPositionMarker;
        // moves user left
        userPosition_X -= 1; 
      } else
      {
        // In the case that no exit can be reached
        noEscape = true;
      }
    }
    //-------------------------------------------------------------------------------------------------------------------------------
    // If the user isn't at the exit and there is an escape path, run the method again
    if ((!(userPosition_X == exitPosition_X) || !(userPosition_Y == exitPosition_Y)) && !noEscape)
    {
          exitCheck();
          isExit = false;
    } else if (userPosition_X == exitPosition_X && userPosition_Y == exitPosition_Y) // if the user is at the exit
    {
     isExit = true; 
    }
   
    // displayCounter allows only one display instead of multiply displays due to recursion
   if (displayCounter == 0)
   {
     displayCounter++;
     // Once at the exit or there is no escape, program ends
     if (isExit)
     {
       System.out.println("A way to escape the maze has been found.");
     } else if (noEscape)
     {
       System.out.println("There is no way to escape this maze."); 
     }
   }
  }
  
  // Displays a legend to define the symbols used for the user
  public void displayLegend()
  {
    System.out.println("Legend: ");
    System.out.println(initialPositionMarker + " ----------- Starting position");
    System.out.println(deadEndPositionMarker + " ----------- Dead End Path");
    System.out.println(previousPositionMarker + " ----------- Escape Path");
  }
}
