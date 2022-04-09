#Assignment

One part of a current project is responsible for processing the XML data received from a partner. In the attachment you can find a sample file containing some statistics from a football match. We would like you to write a program that:

Can be executed with command line: java -cp ... xxx.jar. It should accept two parameters:
type of statistic to check
path to a file to process
When executed, the program will read the XML from path 1b and will write to output top 5 players with statistic 1a (from any team; desc). Players should be written with format: <POSITION_IN_RANKING>. <FIRST NAME> <LAST NAME> - <STATISTIC_VALUE>
Will also sum the value for that statistic for each team. Team should be written with format: <TEAM_SIDE>; <TEAM_NAME> - <SUM_OF_STATISTIC_VALUES>
Please provide a README file (in github format), explaining:
how to build the application - this should be a single command
how to run the application - this should be a single command