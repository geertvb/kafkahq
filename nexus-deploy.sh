
mvn deploy:deploy-file \
  -Durl=https://webgate.ec.europa.eu/CITnet/nexus/repository/digit-snapshots/ \
  -DrepositoryId=digit-snapshots \
  -Dfile=./build/libs/kafkahq-0.1.jar \
  -DgroupId=eu.europa.ec.digit.kafka \
  -DartifactId=kafkahq-main \
  -Dversion=1.0.0-SNAPSHOT
