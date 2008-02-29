                +----------------------+
                | Common Java Librarie |
                +----------------------+
 
Common Java Librarie is a simple tool library... 
 
   Pre-requisites
  ================

You need JDK 1.6 or upper.

   Compiling
  ===========

You just need to run :
	$ cd bin
	$ ant MakeJar
	
If you do not have ANT :
	$ mkdir classes
	$ cd src
	$ javac -classpath $CLASSPATH -d ../classes net/**/*.java
	$ cd ../classes
	$ jar cvf dromard.common-${version}.jar com
                                                                                                                                                                      
   Javadoc API
  =============

You just need to run :
	$ mkdir api
	$ cd src
    $ javadoc -classpath $CLASSPATH -d ../api net/**/*.java
                                                                                                                                                                      
                                                                                                                                                                      
   History
  =========
Version 1.9:        [07/09/2007]
 - Enhancement:
 					o Optimise
 					o Resolve some little bugs
 					o Add Web package
 					o Add JSON package
 					o Add templating package
 					o Add visitable package
 					o Add treenode package

Version 1.8:        [03/05/2007]
 - Use of JDK 1.6

Version 1.7: 
 - Enhancement:		[15/04/2007] 
 					o Re structure packages
 					o Add JUnit runner
 					o Add XML/HTML element object
 					o Add ant task for JNLP generation
 					o Add usefull Swing components (JForm, JCachedPanel, JMemoryMonitor)
 					o Add Filthy Rich Client component (JCustomBar, JCustomHeader, JTaskPane, ShadowBorder)

Version 1.6: 
 - Enhancement:		[01/12/2005] 
 					o Manage  LDAP (tested only with PasswordChecker)

Version 1.5: 
 - Enhancement:		[02/12/2005] 
 					o Add function in FileHelper.
 					o Optimise Ordered...
 					o Resolve an error in StringHelper.countOccurence()

Version 1.4: 
 - Enhancement:		[24/06/2005] Consolidation and increase of version.
 - Enhancement:		[22/06/2005] Add LDAP classes.
 
Version 1.3.1: 
 - Enhancement:		[02/12/2004] Add methods SystemTool.console.buildInframeLine(text)
 - Bug fixing:		[21/01/2005] Resolve a bug in the Sql2csv class that rename the file 
 					(if already existed) with a ':' character that is not allowed in 
 					windows file system. (The bug was that the file was not renamed !). 
 					Sus we add a test to verify that the file was correctly renamed, and 
 					if not, it throw an error.

Version 1.3: 22/11/2004
 - Bug fixing:		[22/11/04] Resolve a bug in the 'replace' methods.
 					When the word to replace is at the end of the string, it's skipped by the split() method. 
 - Bug fixing:		[22/10/04] In Sql2Csv class : 
					 - Solve a little bug with buffer utilisation
					 - Remove Spaces in the backup fileName of the SQL result file
 - Bug fixing:		[21/10/04] Change Sql2Csv Class
					 - Solve little bug on indexes
					 - Solve bug of StringBuffer capacity
					 - Solve bug of max number of records exceed
					(Use Statement with Forward type and readonly type)
					Add of little tool class
 - Bug fixing:		[11/10/04] In ResourceFileBundle
					Solve bug that does not find the file if the file name string is in the PATH of the file
 - Bug fixing:		[06/10/04] Resolve a bug that take in account quote even if there was not well place
					Resolve a bug that doeas not take in account an end of field when it encounter " , with 
					a space between seconf quote end next token
 - Enhancement:		[29/09/04] DateDifference: Add a missing getter
 - Bug fixing:		[27/09/04] Remove string.split() which is not compatible with JDK 1.3.1
 - Bug fixing:		[24/09/04] Solve a big bug in the getTheMostOccurences() method
					In io.FileHelper: Add method append2File(FileName, String)
					In util.CsvLineTokenizer:
					     In getTheMostOccurences()
					     With 1.3.1 JDK the string.soplit() function doesn't exist.
					     So develop it differently
 - Enhancement: 	[20/09/04] DateDifference.class: 
					 - Add a little class that able to make difference between 2 dates.
					 - Make a test methods.
					 - Make a little modification in variable initialization.
 - Bug fixing:		[10/09/04] In CSVLineTokenizer :
					If the last token is the last character it was not count !!
					There was a bug when first quote is after first token
 - Enhancement: 	[08/09/04] Changes in CSVLineTokenizer:
					 - Now the system found token and quotes by him self
					 - Add getters for quote and token
					 - Add comments
					 - Change constructors
 - Enhancement: 	[06/09/04] Format code, Use bundle instead of hard coded messages, Manage debug mode
 - Bug fixing:		[06/09/04] Resolve bugs :
					 - Resolve bug while checking values in external files (the last columns was never checked)
					 - In debug mode the application wasn't starting !!
					 - Resolve indexOutOfBoundException when there's no second quotes
 - bug fixing:		[01/09/04] Manage error while parsing file
					 Resolve bug with user's preference.properties
					 Ooops there was ma mistake in CSVLineTokenizer.getNextToken()
					 
Version 1.2: 30/08/2004
 - Enhancement:		Add escape caracters management in CVSLineTokenizer
 - Enhancement:		In CVSLineTokenizer add inteligency so as to find the quote and token used.

Version 1.1: 27/07/2004
 - Enhancement:		In file ResourceFileBundle.java : 
 					Add methods getInputStream(), remove methods load(File) & 
 					load(URL) and change reload() to apply changes.



Gabriel DROMARD
gabriel.dromard@libertysurf.fr