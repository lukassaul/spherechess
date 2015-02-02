SphereChess 

Now Open source 

By Jared Tuveson and Lukas Saul

Status: The software version of this project was mostly abandoned years ago.  
Physical spherical chess boards were made and games have been played.  
The status of this software is that the board works and movement of pieces work, however no rules are implemented. 
The next stages of this project will be to implement rules and an AI opponent.  

Please acknowlege Jared Tuveson and Lukas Saul as the authors of this program if you use it in your project. 
A patent in this repo is on file with the USPTO.  



SPHERECHESS README FILE


INSTALLATION: 

  This program runs with java virtual machine versions 1.2 and higher.  In the game
directory must be the following files:

Schess.jar     (the program)
*20.3ds        (the pieces: bishop,pawn,king,queen,rook, and knight)
3N.jpg         (the color map for "black" squares)  
2N.jpg         (the color map for "white" squares)
white.jpg      (the color map for "white" pieces)
black.jpg      (the color map for "black" pieces)
readme.txt     (this file)

The color map files must exist for the program to run, but they can be modified to custom 
craft your board and pieces.

If you don't have java installed, you will need to also have the following directory:

optional:
jre directory  (the java virtual machine, if you don't have it installed elsewhere)


These files will self-extract automatically into the folder "spherechess" in a 
destination you choose when you run the self-extracting archive.


To avoid this large directory install the latest JVM from sun:
http://www.java.com/en/download/windows_automatic.jsp

or visit http://java.sun.com  for other platforms.



TO RUN THE GAME:

1)  (with java - jre already on your machine) -  run Schess.jar

    If this doesn't work you can run it from a command line: java -jar Schess.jar

    Or run the batch file schess_small.bat.
    
2)  (with java - jre included in spherechess download) - run the batch file
	
    schess_large.bat  
    
    Or from the commandline:   \jre\bin\java-jar Schess.jar
    
    
  To run the game on UNIX machines or Macintosh you must have java installed.  If properly
installed you need only run the file Schess.jar.  See java.sun.com for more details.
    
    
    


ABOUT:


Patent pending: Jared Tuveson, Lukas Saul
 
  JSphereChess Beta 8.03     
       
       

 This software is licensed for test purposes only, all rights reseverd

  Developed in java using idx3d tiny graphics engine:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 idx3d Kernel 3.1.001 [Build 29.05.2000]
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 (c)1999 by Peter Walser, all rights reserved.
 http://www2.active.ch/~proxima/idx3d
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
For more information contact lukassaul@usa.net
       
   Existing game controls are sluggish.  To move a piece,    
click the piece until a dialog comes up saying it have been     
selected.  Than click the destination square.       
       
  Clicking a square when no piece is selected will allow
placing other pieces on the board or removing pieces.    
       
       
  If the system cannot determine which piece or square you are
trying to select, try rotating the board a little and clicking
the piece or square from another angle.       
       
  Good luck!!  Thanks for trying JSphereChess        
  
  

  
RULES:  
  
  
     The rules of chess are not implemented in this simple board.
  The board allows pieces to be placed anywhere at any time,  
  just like an ordinary chessboard.   
                    
    Therefore players must agree on rules before starting the game.
  The standard spherechess rules go something like this: 
  
        
     White goes first, each side can move just as in ordinary chess.
  The piece movements are the same as well.        
  There are the same number of starting pieces and squares
  
  It gets interesting near the 3-sided squares however.   
  All pieces (except knights) must stop as they pass through these squares. 
                    
    Pawns must move toward the enemy pole along the two fronts.     
  When a pawn reaches the 'middle' of its rank, it must turn to continue
  Only the middle two pawns in a front can start with a double move. 
  Such a double move allows the usual "en passant"..   
  and there is an additional "en passant" move if a pawn skips  
  a space via a capture (rare!!).   
                    
     A pawn can be replaced by any other piece when it reaches one of the  
  six squares surrounding the enemy's 2 pole spots (starting K&Q positions).
                    
    If a pawn enters a 3 sided square (via capture only) it must  
  capture to exit.  
      
    Other variations in these rules are possible and encouraged!              
                    
                    
                    
                  
