@rem Change the working directory to the location of this file so that relative paths will work
cd /D "%~dp0"

@rem Make sure the environment variables are up-to-date. This is useful if the user installed python a moment ago.
call ./RefreshEnv.cmd

@rem Install or update rlbot and related python packages.
call ./gradlew.bat --no-daemon updateRLBot

@rem Launch the framework without the GUI.
python -c "from rlbot import runner; runner.main();"

pause
