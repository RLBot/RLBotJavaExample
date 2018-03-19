# RLBotJavaExample
An example bot implemented in Java

## Usage Instructions:

These instructions should get dramatically simpler once we get our packages in jcenter.
For now, this is what you need to do:

1. Setup the RLBot framework - https://github.com/RLBot/RLBot (v4 branch for now)
2. Create a new folder in the framework at /agents/protoBotJava.
3. Copy the contents of the src/main/python folder the protoBotJava folder.
4. Copy src/main/resources/port.cfg into the protoBotJava folder.
5. In the framework directory, find rlbot.cfg and modify one of the lines to point to ./agents/protoBotJava/protoBotJava.cfg
6. On the command line in this directory, execute `gradlew.bat run`
7. On a different command line, in the framework directory, execute `python runner.py`
