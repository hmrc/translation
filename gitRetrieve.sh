#!/bin/bash

if [ $# -lt 3 ]
  then
    echo "Not enough arguments supplied"
    echo "Usage: git-retrieve.sh [project name] [git clone ref] [git commit ref]"
    echo "example: git-retrieve.sh tcr-frontend git@github.tools.tax.service.gov.uk:HMRC/tcr-frontend.git 8g80f22"
    exit 1
fi

projectDir=$1
gitCloneRef=$2
gitCommitRef=$3

now=$(date +"%T")

currentEnglishMessages="current_messages"
currentWelshMessages="current_messages.cy"
oldEnglishMessages="old_messages"
oldWelshMessages="old_messages.cy"
tempDir='translation_temp'

translationDir=${PWD}
cd ..
hmrcPath=${PWD}
tempPath=$hmrcPath/$tempDir
projectPath=$tempPath/$projectDir

echo hmrcPath is $hmrcPath
echo projectPath is $projectPath
echo tempPath is $tempPath
echo projecDir is $projectDir
echo translationDir is $translationDir

cd $hmrcPath

echo Creating $tempDir ...
if [ -e $tempDir ]
then
    mv $tempDir $tempDir.$now
fi

mkdir $tempDir
cd $tempDir

echo Cloning $gitCloneRef may take a moment ...
git clone $gitCloneRef
cd $projectDir

echo Copying current English messages ...
if [ -e $translationDir/$currentEnglishMessages ]
then
    cp $translationDir/$currentEnglishMessages $translationDir/$currentEnglishMessages.$now
fi
cp $projectPath/conf/messages    $translationDir/$currentEnglishMessages


echo Copying current Welsh messages ...
if [ -e $translationDir/$currentWelshMessages ]
then
    cp $translationDir/$currentWelshMessages $translationDir/$currentWelshMessages.$now
fi
cp $projectPath/conf/messages.cy $translationDir/$currentWelshMessages


echo Checking out previous build $gitCommitRef ...
git checkout $gitCommitRef


echo Copying previous English messages ...
if [ -e $translationDir/$oldEnglishMessages ]
then
    cp $translationDir/$oldEnglishMessages $translationDir/$oldEnglishMessages.$now
fi
cp $projectPath/conf/messages    $translationDir/$oldEnglishMessages


echo Copying previous Welsh messages ...
if [ -e $translationDir/$oldWelshMessages ]
then
    cp $translationDir/$oldWelshMessages $translationDir/$oldWelshMessages.$now
fi
cp $projectPath/conf/messages.cy $translationDir/$oldWelshMessages


echo Tidying up ...
rm -rf $hmrcPath/$tempDir
