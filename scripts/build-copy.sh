PJ=$1 # a | cs
BUILD=$2

PJ_HOME=..
BULD_LIBS=build/libs
TARGET_DIR=../build #/Volumes/INNER-YJKIM/채널AI/운영배포 # ==> change path to copy the completed build file.
TODAY=$(date +"%Y%m%d")
TODAY_TARGET_DIR=$TARGET_DIR/$TODAY/

if [ "$PJ" == "-h" ] || [ "$PJ" == "--help" ]; then
  echo "These are deploy-dev.sh commands:
  first command option : target project
      a, all         all project (swinglab-platform)
      sp, swinglab-platform     swinglab-platform

  second command option : execute caims-build-copy.sh
      -b, --build    execute 'gradle build' before copy."
  exit;
fi


# build
if [ "$BUILD" == "-b" ] || [ "$BUILD" == "--build" ]; then

  echo "build start!!"
  cd ../

  if [ "$PJ" == "a" ] || [ "$PJ" == "all" ]; then
      ./gradlew build
  elif [ "$PJ" == "sp" ] || [ "$PJ" == "swinglab-platform" ]; then
      ./gradlew build
  fi

  cd scripts
fi

echo ""
echo "copy to $TODAY_TARGET_DIR  from $PJ_HOME/.../$BULD_LIBS"

# mkdir dir
if [ ! -d $TODAY_TARGET_DIR ]; then
	echo "mkdir $TODAY_TARGET_DIR" 
	mkdir -p $TODAY_TARGET_DIR
fi

if [ "$PJ" == "a" ] || [ "$PJ" == "all" ]; then
  # common-service
  echo ""
  echo "swinglab-platform.jar copy start!!"
  cp $PJ_HOME/$BULD_LIBS/swinglab-platform.jar $TODAY_TARGET_DIR

elif [ "$PJ" == "sp" ] || [ "$PJ" == "swinglab-platform" ]; then
  # common-service
  echo ""
  echo "swinglab-platform.jar copy start!!"
  cp $PJ_HOME/$BULD_LIBS/swinglab-platform.jar $TODAY_TARGET_DIR

fi