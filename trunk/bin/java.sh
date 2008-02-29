#! /bin/sh

# Variables definitions
#file=`which $0`
#binFolder=`dirname $file`

#homeFolder=`pwd`
#cd $binFolder
#binFolder=`pwd`
#classFolder=$binFolder"/../classes"
#sourceFolder=$binFolder"/../sources"
#libFolder=$binFolder"/../webserver/WEB-INF/lib"
#cd $homeFolder

JAVA_OPTIONS="-Ddts.platform=weblogic -Dariba.sourcing.home=$homeFolder/.. -Dweblogic.system.home=$homeFolder/../config -Dweblogic.system.name=webserver"

#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
# Do not modified after this line !
#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


###################################
# Colors Functions                #
###################################


setAttribute() {
	if isNumeric $1; then ATTR=$1; else ATTR=`getAttribute $1`; fi
	printf "[%dm" $ATTR
}

setFGColor() {
	if isNumeric $1; then FG=$1;   else FG=`getColor $1`; fi
	FG=`expr 30 + $FG 2>/dev/null`;
	printf "[%dm" $FG
}

setBGColor() {
	if isNumeric $1; then BG=$1;   else BG=`getColor $1`; fi
	BG=`expr 40 + $BG 2>/dev/null`;
	printf "[%dm" $BG
}

textColor() {
	if [ "$1" != "" ]; then setAttribute $1; fi
	if [ "$2" != "" ]; then setFGColor $2; fi
	if [ "$3" != "" ]; then setBGColor $3; fi
	if [ "$4" != "" ]; then printf $4; fi
}

reset() {
	setAttribute RESET;
}

isNumeric() {
	test=`expr $1 + 0 2>/dev/null`
	if [ "$test" = "$1" ]; then return 0; else return 1; fi
}

getAttribute() {
	case $1 in 
		RESET)     echo 0; ;;
		BRIGHT)    echo 1; ;;
		DIM)       echo 2; ;;
		UNDERLINE) echo 3; ;;
		BLINK)     echo 4; ;;
		REVERSE)   echo 7; ;;
		HIDDEN)    echo 8; ;;
		*)         echo 8; ;;
	esac
}

getColor() {
	case $1 in 
		BLACK)   echo 0; ;;
		RED)     echo 1; ;;
		GREEN)   echo 2; ;;
		YELLOW)  echo 3; ;;
		BLUE)    echo 4; ;;
		MAGENTA) echo 5; ;;
		CYAN)    echo 6; ;;
		WHITE)   echo 7; ;;
		*)       echo 8; ;;
	esac
}

TEXT_COLOR=true

###################################
# Utilities Functions             #
###################################

pause() {
	if [ "$*" = "" ]; then
		printf "\n  Type ENTER to continue"
	else
		echo "$*"
	fi
	read WAIT
}

isItOk() {
	MESSAGE="Is It OK ? [YES/no] "
	if [ "$*" != "" ]; then MESSAGE="$* [YES/no] "; fi
	
	printf "$MESSAGE"
	read SURE
	
	if [ "$SURE" = "no" ] || [ "$SURE" = "n" ] || [ "$SURE" = "NO" ] || [ "$SURE" = "N" ]; then 
		return 1
	else
		return 0
	fi
}

comment() {
	if [ "$TEXT_COLOR" = "true" ]; then textColor BRIGHT WHITE BLACK; fi
	printf "\n-> $1"
	if [ "$TEXT_COLOR" = "true" ]; then reset; fi
	printf "\n"
}

getFileName() {
	var1=`dirname $1`
	result=`echo $1 | sed "s%$var1/%%g"`
}

getFileSize() {
	if [ "$1" != "" ]; then 
		ls -l $1 2>/dev/null | awk '{ print $5 }'
	fi
}

getFileDate() {
	if [ "$1" != "" ]; then 
		ls -l $1 2>/dev/null | awk '{ if(NF == 9) printf("%s %s %s\n",$6,$7,$8); else printf("%s %s\n",$6,$7); }'
	fi
}

###################################
# Initialisation Functions        #
###################################

init() {
        binFolder=`dirname $0`
        homeFolder=`pwd`
        cd $binFolder
	
	# Ask the user if the PATH(s) are OK 
	if [ -f ".java.properties" ]; then
		echo "If folders aren't good delete the file '.java.properties', it will be regenerated"
		. .java.properties
	else
		binFolder=`pwd`
		classFolder=$binFolder"/../classes"
		sourceFolder=$binFolder"/../sources"
		libFolder=$binFolder"/../webserver/WEB-INF/lib"

		isLibFolderOK
		isBinFolderOK
		isHomeFolderOK
		isClassFolderOK
		isSourceFolderOK
		(
			echo "libFolder=$libFolder"
			echo "binFolder=$binFolder"
			echo "homeFolder=$homeFolder"
			echo "classFolder=$classFolder"
			echo "sourceFolder=$sourceFolder"
		) > .java.properties
	fi

	# Initialisation of classpath
	if [ "$CLASSPATH" = "" ]; then
		CLASSPATH="$homeFolder":"$classFolder":.
	else 
		CLASSPATH=$CLASSPATH:"$homeFolder":"$classFolder":.
	fi
}

###################################
# Javas Functions                 #
###################################

makeClasspath() {
	if [ ! -f .classpath ]; then
		# make classpath
		for properties in `find $libFolder -name "*.properties"`
		do
			getFileName $properties
			echo "Adding \"$result\" to classpath"
			CLASSPATH=$CLASSPATH:"$properties"
		done
		# make classpath
		for lib in `find $libFolder -name "*.jar"`
		do
			getFileName $lib
			echo "Adding \"$result\" to classpath"
			CLASSPATH=$CLASSPATH:"$lib"
		done
		for lib in `find $libFolder -name "*.zip"`
		do
			getFileName $lib
			echo "Adding \"$result\" to classpath"
			CLASSPATH=$CLASSPATH:"$lib"
		done
		echo "$CLASSPATH" > .classpath
	else
		echo "To force CLASSPATH regeneration delete the file '.classpath'"
		CLASSPATH=`cat .classpath`
	fi
}

compil() {
	cd $sourceFolder
	if [ ! -d $classFolder ]; then
		echo "Folder \"$classFolder\" does not exists !"
		exit 0
	fi
	sources2Compile=`find . -name "*.java"`
	
	if [ "$1" = "-exclude" ]; then
		argv=`echo $* | sed "s/-exclude //g"`

		for j in `echo $sources2Compile`; 
		do
			tmp="none"
			for i in `echo $argv`; 
			do
				if [ "$tmp" != "" ]; then 
					tmp=`echo $j | grep -v $i`
				fi
			done
			temp="$temp $tmp"
		done
		sources2Compile=$temp
		
	elif [ "$1" != "" ]; then
		sources2Compile=$*
	fi

	# compilation of all java files
	for javaFile in `echo $sources2Compile`
	do
		# Get parameters of files
		classFile=`echo "$classFolder/$javaFile" | sed "s/\.java/\.class/"`
		classFileDate=`getFileDate $classFile`
		javaFileDate=`getFileDate $javaFile`
		
		#echo "Classe: $classFileDate - $classFile"
		#echo "Java  : $javaFileDate - $javaFile"
		
		if [ "$classFileDate" = "" ] || [ "$classFileDate" != "$javaFileDate" ]; then
			if [ "$TEXT_COLOR" = "true" ]; then textColor BRIGHT BLUE 8; fi
			echo "Compiling $javaFile"
			if [ "$TEXT_COLOR" = "true" ]; then textColor BRIGHT RED 8; fi
			result=`javac -classpath $CLASSPATH -d "$classFolder" $javaFile 2>&1`
			error=`echo "$result" | grep "error"`
			if [ "$error" = "" ]; then
				if [ "$result" != "" ]; then
					printf "$result\n"
				fi
				#Linux: touch --file=$classFile $javaFile
				touch -r $classFile $javaFile
			else 
				printf "Compilation failed with error; \n$result\n"
			fi
		else
			if [ "$TEXT_COLOR" = "true" ]; then textColor BRIGHT BLUE 8; fi
			echo "File $javaFile is up to date"
		fi	
		
		if [ "$TEXT_COLOR" = "true" ]; then reset; fi
	done
	cd $homeFolder
}

run() {
	cd $homeFolder
	java -classpath $CLASSPATH $JAVA_OPTIONS $*
	
}

makeJar() {
	cd $sourceFolder
	getFileName $classFolder
	for prop in `find . -name "*.properties" | grep -v "$result"`; do
	        cp $prop $classFolder/$prop
	done

	cd $classFolder;
	jar cvf $libFolder/$1 *
	cd $homeFolder
}

makeJavaApi() {
	cd $sourceFolder
	comment "Generating Java Doc in folder: $homeFolder/doc/API"
	if [ ! -d $homeFolder/javadoc ]; then mkdir $homeFolder/javadoc; fi
	javadoc -d $homeFolder/javadoc -private -author -use -version -doctitle "Java API" -classpath $CLASSPATH `find . -name "*.java"`
	cd $homeFolder
}

###################################
# Main Program                    #
###################################
isLibFolderOK() {
	if isItOk "Lib folder, where the librairies will be found: [$libFolder] is it ok ?"; then
		printf ""
	else
		printf "Enter correct folder: "
		read libFolder
		isLibFolderOK
	fi
}

isBinFolderOK() {
	if isItOk "Bin folder, where java.sh is: [$binFolder] is it ok ?"; then
		printf ""
	else
		printf "Enter correct folder: "
		read binFolder
		isBinFolderOK
	fi
}

isHomeFolderOK() {
	if isItOk "Home folder, where the executable is run: [$homeFolder] is it ok ?"; then
		printf ""
	else
		printf "Enter correct folder: "
		read homeFolder
		isHomeFolderOK
	fi
}

isClassFolderOK() {
	if isItOk "Class folder, where the classes have to be put: [$classFolder] is it ok ?"; then
		printf ""
	else
		printf "Enter correct folder: "
		read classFolder
		isClassFolderOK
	fi
}

isSourceFolderOK() {
	if isItOk "Java folder, where the java files are: [$sourceFolder] is it ok ?"; then
		printf ""
	else
		printf "Enter correct folder: "
		read sourceFolder
		isSourceFolderOK
	fi
}

comment "Initialisation ..."
init

case $1 in
	"-exec"|"-e"|"-run"|"-r")
		comment "Making classpath: "
		makeClasspath

		comment "Executing Java File:"
		run `echo $* | sed "s/-.* //g"`
	;;
	"-make"|"-m"|"-compil"|"-c")
		comment "Making classpath: "
		makeClasspath

		comment "Making Java Files:"
		if [ "$#" = "1" ]; then
			compil
		else
			compil `echo $* | sed "s/-. //g"`
		fi
	;;
	"-jar"|"-j"|"-archive"|"-a")
		makeClasspath
		ARCHIVE=application.jar
		if [ "$2" != "" ];then ARCHIVE=$2; fi
		comment "Making archive $libFolder/$ARCHIVE"
		makeJar $ARCHIVE
	;;
	"-javadoc"|"-doc"|"-api")
		makeClasspath
		makeJavaApi 
	;;
	*)
		if [ "$TEXT_COLOR" = "true" ]; then setFGColor BLUE; fi
		echo "This is a tool so as to simplify java developpement"
		echo "It can compil all the java files"
		echo ""
		if [ "$TEXT_COLOR" = "true" ]; then setFGColor RED; fi
		echo "Usage java.sh: -[exec,e,run,r] javaClasses  # Execute of one Java Class"
		echo "Usage java.sh: -[make,m,compil,c] javaFiles # Compil all javaFiles"
		echo "Usage java.sh: -[jar,j,archive,a] javaFiles # Create the archive containing"
		echo "                                            # all the files in the class folder"
		echo "Usage java.sh: -[javadoc,doc,api]           # Generate the javadoc"
		if [ "$TEXT_COLOR" = "true" ]; then setAttribute RESET; fi
	;;
esac

echo 
