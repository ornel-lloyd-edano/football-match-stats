# Assignment

One part of a current project is responsible for processing the XML data received from a partner. In the attachment you can find a sample file containing some statistics from a football match. We would like you to write a program that:

Can be executed with command line: java -cp ... xxx.jar. It should accept two parameters:
type of statistic to check
path to a file to process
When executed, the program will read the XML from path 1b and will write to output top 5 players with statistic 1a (from any team; desc). Players should be written with format: <POSITION_IN_RANKING>. <FIRST NAME> <LAST NAME> - <STATISTIC_VALUE>
Will also sum the value for that statistic for each team. Team should be written with format: <TEAM_SIDE>; <TEAM_NAME> - <SUM_OF_STATISTIC_VALUES>
Please provide a README file (in github format), explaining:
how to build the application - this should be a single command
how to run the application - this should be a single command

# How To Build
1. Open console and navigate to the SBT project folder (default: football-match-stats)
2. Type and execute: `sbt clean assembly`
3. Artifact is saved in `/project folder/target/scala-2.13/`

# How To Run
1. Open console and navigate to the folder where the jar was created (default location: /football-match-stats/target/scala-2.13/)
2. Type and execute: `java -jar <insert name of jar file> <insert name of stat>  <insert xml file>`

   (ex. `java -jar football-match-stats-assembly-0.1.0-SNAPSHOT.jar accurate_pass classes/1-F9-2192085691-srml-8-2017-f919230-matchresults.xml`)
