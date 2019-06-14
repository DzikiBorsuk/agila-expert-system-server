echo [AGILA] - Publishing...

./gradlew build

echo [AGILA] - BUILD SUCCESSFUL!

echo [AGILA] - Uploading jar to the server...

scp build/libs/tu-kl-cps-agila-0.1.0.jar  admin@131.246.211.149:~/agila.jar

echo [AGILA] - Moving jar...

ssh admin@131.246.211.149 'sudo -S mv agila.jar /var/agila/'

echo [AGILA] - Restarting service...

ssh admin@131.246.211.149 'sudo -S service agila restart'

echo [AGILA] - PUBLISH COMPLETE!
