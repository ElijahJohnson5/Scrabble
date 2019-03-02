# CS-351 Elijah Johnson Scrabble Project
## Command Line Solver
Compiled with Java 10
* How to Use
  * Download the command line solver jar
  * Make sure you have a dictionary file to use that has words separated by blank lines
  * Run the command `java -jar CommandLineSolver.jar [Dictionary File]`
  * It will now prompt you for the size of the board on the first line then the rest of the board (see board configuration for more info)
  * An easier way is to have the board in a file and redirect it to stdin `java -jar CommandLineSolver.jar [Dictionary file] < [Test Cases]`
  * There are example test cases in [test.txt](/master/resources/test.txt) and a scrabble dictionary in [sowpods.txt](/master/resources/sowpods.txt) for use
  * To Run with these test cases you would run (assuming they are all in the same directory) `java -jar CommandLineSolver.jar sowpods.txt < test.txt`

#### Known Bugs
There are no known bugs with the command line solver this far

#### Board Configuration
The board configurations that are supported are
```
15
3. .. .. .2 .. .. .. 3. .. .. .. .2 .. .. 3.
.. 2. .. .. .. .3 .. .. .. .3 .. .. .. 2. ..
.. .. 2. .. .. .. .2 .. .2 .. .. .. 2. .. ..
.2 .. .. 2. .. .. .. .2 .. .. .. 2. .. .. .2
.. .. .. .. 2. .. .. .. .. .. 2. .. .. .. ..
.. .3 .. .. .. .3 .. .. .. .3 .. .. .. .3 ..
.. .. .2 .. .. .. .2 .. .2 .. .. .. .2 .. ..
3. .. .. .2 .. .. .. 2. .. .. .. .2 .. .. 3.
.. .. .2 .. .. .. .2 .. .2 .. .. .. .2 .. ..
.. .3 .. .. .. .3 .. .. .. .3 .. .. .. .3 ..
.. .. .. .. 2. .. .. .. .. .. 2. .. .. .. ..
.2 .. .. 2. .. .. .. .2 .. .. .. 2. .. .. .2
.. .. 2. .. .. .. .2 .. .2 .. .. .. 2. .. ..
.. 2. .. .. .. .3 .. .. .. .3 .. .. .. 2. ..
3. .. .. .2 .. .. .. 3. .. .. .. .2 .. .. 3.
```
or
```
15
31 11 11 12 11 11 11 31 11 11 11 12 11 11 31
11 21 11 11 11 13 11 11 11 13 11 11 11 21 11
11 11 21 11 11 11 12 11 12 11 11 11 21 11 11
12 11 11 21 11 11 11 12 11 11 11 21 11 11 12
11 11 11 11 21 11 11 11 11 11 21 11 11 11 11
11 13 11 11 11 13 11 11 11 13 11 11 11 13 11
11 11 12 11 11 11 12 11 12 11 11 11 12 11 11
31 11 11 12 11 11 11 21 11 11 11 12 11 11 31
11 11 12 11 11 11 12 11 12 11 11 11 12 11 11
11 13 11 11 11 13 11 11 11 13 11 11 11 13 11
11 11 11 11 21 11 11 11 11 11 21 11 11 11 11
12 11 11 21 11 11 11 12 11 11 11 21 11 11 12
11 11 21 11 11 11 12 11 12 11 11 11 21 11 11
11 21 11 11 11 13 11 11 11 13 11 11 11 21 11
31 11 11 12 11 11 11 31 11 11 11 12 11 11 31
```
Where the first number represents the word multiplier of the square, and the second number represents the letter multiplier. 
These board configurations need to be followed by a string of characters representing the current tray. 
The board can also contain any number of characters in any spots.  
Example full test case
```
15
3. .. .. .2 .. .. .. 3. .. .. .. .2 .. .. 3.
.. 2. .. .. .. .3 .. .. .. .3 .. .. .. 2. ..
.. .. 2. .. .. .. .2 .. .2 .. .. .. 2. .. ..
.2 .. .. 2. .. .. .. .2 .. .. .. 2. .. .. .2
.. .. .. .. 2. .. .. .. .. .. 2. .. .. .. ..
.. .3 .. .. .. .3 .. .. .. .3 .. .. .. .3 ..
.. .. .2 .. .. .. .2 .. .2 .. .. .. .2 .. ..
3. .. .. .2 ..  c  a  t .. .. .. .2 .. .. 3.
.. .. .2 .. .. .. .2 .. .2 .. .. .. .2 .. ..
.. .3 .. .. .. .3 .. .. .. .3 .. .. .. .3 ..
.. .. .. .. 2. .. .. .. .. .. 2. .. .. .. ..
.2 .. .. 2. .. .. .. .2 .. .. .. 2. .. .. .2
.. .. 2. .. .. .. .2 .. .2 .. .. .. 2. .. ..
.. 2. .. .. .. .3 .. .. .. .3 .. .. .. 2. ..
3. .. .. .2 .. .. .. 3. .. .. .. .2 .. .. 3.
dgos*ie
```
### Unfinished Features
There is no unfinished features for the command line solver

## GUI Version
Compiled with java 10
* How to Use
  * Download the Scrabble.jar file
  * Run the command `java -jar Scrabble.jar`
  * Play the game
  
### Program Entry Point
The entry point for the gui version is the ScrabbleGui.main function

#### Known Bugs
There are no known bugs with the gui version

#### Unfinished Features
* Showing who won the game
* Starting a new game without restarting the jar