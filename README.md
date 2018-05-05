# AI_geneticCarsJAVA
Artificial intelligence project for car animation.

# Abstract
This project aims at integrating demonstration about neural network.  
Some source code was original found freely on web with some video too.  
The JAVA technology was in use and I decided to make this work my way.  


Cars are learnt to go on several circuits. Then a good selected car is put 
on an unknown circuit to demonstrate how the neural network works.

The architecture of the neural network is built around two neurons that calculate 
commands for steering wheel and engine of the car. Each neuron is given input 
values from sensors (the distances from the outside of the circuit and the current car position).

The learning strategy is to experience population of cars with initial random
 values for the weights and threshold for the neurons. 
Depending on completion achieved on the circuit, cars are scored. Until at least one car succeeded
 in completing circuits, the experience is redone with modification of the weights and the thresholds
  thanks to a genetic algorithm that takes into account the score of the cars.

_It is interesting to see this, I think._
_Cyril FENARD._

# Key words

## Topics

**Perceptron** **Weights** **Threshold** **Genetic mutation** **Learning** **Neural network**

## Software

**JAVA** **Maven** **Eclipse** **SonarQube**


# Check development environment

	$ uname -smov
	CYGWIN_NT-10.0 2018-02-02 15:16 x86_64 Cygwin

	$ cat /cygdrive/c/eclipse/.eclipseproduct | grep version
	version=4.5.2

	$ echo ${JAVA_HOME}
	C:\Program Files\Java\jdk1.8.0_161

	$ file $(which java)
	/cygdrive/c/ProgramData/Oracle/Java/javapath/java:
	symbolic link to /cygdrive/c/Program Files/Java/jre1.8.0_161/bin/java.exe


	$ "${JAVA_HOME}"/bin/javac.exe
	Usage: javac <options> <source files>
	where possible options include:
	-g:none                    Generate no debugging info
	-verbose                   Output messages about what the compiler is doing

	$ ./mvn --version
	Apache Maven 3.5.3 (3383c37e1f9e9b3bc3df5050c29c8aff9f295297; 2018-02-24T20:49:05+01:00)
	Maven home: E:\PERSO\PRO\DEVS\TOOLS\apache-maven-3.5.3
	Java version: 1.8.0_161, vendor: Oracle Corporation
	Java home: C:\Program Files\Java\jdk1.8.0_161\jre
	Default locale: fr_FR, platform encoding: Cp1252
	OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"


Check in `pom.xml` the JDK versions to be used

	<properties>
		<maven.compiler.source>1.7</maven.compiler.source>
		<maven.compiler.target>1.7</maven.compiler.target>
	</properties>
  

# Created original modules from source code from the Web

 * package com.newgameplus.frameworkdemo.misc
 * package com.newgameplus.framework.genetic
 * package com.newgameplus.framework.neural
 * exemple.java


# Wrote missing modules

 * package com.newgameplus.framework.debug
 * package com.newgameplus.framework.draw
 * package com.newgameplus.framework.misc
 * package com.newgameplus.frameworkdemo.draw
 * package com.newgameplus.frameworkdemo.gui
 * package com.cf.framework.trackdata
 * package com.cf.framework.coreapp
 * package com.cf.framework.applearning
 * package com.cf.framework.apprace
 * package com.cf.framework.appcurve


# Some technical reminders

 * creating maven module

        $ mvn archetype:generate -DgroupId=com.cf.framework.applearning -DartifactId=AppLearning \
        -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

        $ mvn verify -pl AppLearning

 * preparing projects for Eclipse

        $ mvn eclipse:eclipse -pl AppLearning

 * configuring final jar executable

        $ grep artifactId ./pom.xml 

        <artifactId>neuralDemo</artifactId>
        <artifactId>junit</artifactId>
        <artifactId>maven-compiler-plugin</artifactId>
        <artifactId>maven-assembly-plugin</artifactId>
        <artifactId>maven-resources-plugin</artifactId>
        <artifactId>maven-jar-plugin</artifactId>


        $ find . -type f -name "jar.xml" -print
	
        ./src/main/assembly/jar.xml


        $ find . -type f -name pom.xml -print -exec grep directory {} \;

        ./Draw/pom.xml
        <directory>src/main/resources/images</directory>


        $ find . -type f -name pom.xml -print -exec grep mainClass {} \;

        ./AppCurve/pom.xml
        <mainClass>com.cf.framework.appcurve.AppCurve</mainClass>
        ./AppLearning/pom.xml
        <mainClass>com.cf.framework.applearning.AppLearning</mainClass>
        ./AppRace/pom.xml
        <mainClass>com.cf.framework.apprace.AppRace</mainClass>


  * building the application

        $ mvn clean compile test package

  * inspecting the jar of application
 
        $ "${JAVA_HOME}"/bin/jar.exe tvf ./target/AppLearning-1.0-SNAPSHOT-executable.jar

  * running the application

        $ "${JAVA_HOME}"/bin/javaw.exe -jar ./target/AppLearning-1.0-SNAPSHOT-executable.jar
