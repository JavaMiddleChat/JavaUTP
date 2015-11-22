Programming network
 Client v0.1 And Server v0.1
=============================
/**
 * Created by Yevhen Shcherbynskyi on 05.11.2015.
	Group 16c (s12983)
 */


INSTALLATION
------------

 You shall see the following files and directories:

      .settings/               
       bin/           	classes sourse files
       src/        	framework source files
      .classpath            
      .project             
       README           this file to get acquainted


QUICK START
-----------

Server and Client comes with a command line tool called "javac" that can create
a skeleton Server and Client application for you to start with.

On command line, type in the following commands:

Windows Install JDK 1.8 and after you mount it:
	Or download Web.

Linux 	Install JDK 1.8 and after you mount it:
	$ sudi apt-get install openjdk-8-jdk
	Or donwload Web.

----------------------------AFTER-----------------------------------
For Linux:
First: cd /home/ (And where you saved a Project)
	$ javac Server.java (After Client.java)	  (Linux)
        $ java Server  (After Client)             (Linux)
(if not working, find the error in the Internet)
        
For Windows:
First: path (and where you installed java JDK)/bin
       cd /(And where you saved a Project)
       
Second:
	javac Server.java (After Client.java)  (Windows)
        java Server  (After Client)            (Windows)



NOT FINALIZED
-----------
The client spplication:
	iv. After all the files are sent to the server, it prints a summary information
	including the number of files sent, how many files were not succesfully
	transmitted (there may be a problem with storage space or a file might have
	beed changed during transmission).

The server:
	iv. Checks, if the data were not modified during the transfer and if they were
	correctly stored on the disk and notifies the client about it.
-----------
Thank you for your attention. Bye