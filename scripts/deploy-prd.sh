PJ=$1 # a | cs
BUILD=$2 # -b | --build

PJ_HOME=..
BULD_LIBS=build/libs
TARGET_DIR=../build #/Volumes/INNER-YJKIM/채널AI/운영배포 # ==> change path to copy the completed build file.
TODAY=$(date +"%Y%m%d")
TODAY_TARGET_DIR=$TARGET_DIR/$TODAY
DEV_USER=root
DEV_DEPOLY_DIR=13.125.190.65:/opt/swinglab-platform/upload/
KEY=~/Desktop/project/01\ swinglab/swing-lab-key.pem

if [ "$PJ" == "-h" ] || [ "$PJ" == "--help" ]; then
  echo "These are deploy-dev.sh commands:
  first command option : target project
      a, all         all project (swinglab-platform)
      sp, swinglab-platform     swinglab-platform

  second command option : execute caims-build-copy.sh
      -b, --build    execute 'gradle build' before copy."
  exit;
fi


# build and copy
if [ "$BUILD" == "-b" ] || [ "$BUILD" == "--build" ]; then
    echo "build and copy start!!"
    ./build-copy.sh $PJ -b
else : then
    echo "copy start!!"
    ./build-copy.sh $PJ
fi

if [ "$PJ" == "a" ] || [ "$PJ" == "all" ]; then
  sudo scp -i ~/Desktop/project/01\ swinglab/swing-lab-key.pem $TODAY_TARGET_DIR/swinglab-platform.jar $DEV_USER@$DEV_DEPOLY_DIR
elif [ "$PJ" == "sp" ] || [ "$PJ" == "swinglab-platform" ]; then
  sudo scp -i ~/Desktop/project/01\ swinglab/swing-lab-key.pem $TODAY_TARGET_DIR/swinglab-platform.jar $DEV_USER@$DEV_DEPOLY_DIR
fi
