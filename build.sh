#!/usr/bin/env bash
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -P controller-standalone -P designer-standalone
mkdir target
zip target/RITController.zip RITController/target/*.jar 
cp ./RITDesigner/target/*.zip target/
