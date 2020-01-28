
mvn install:install-file \
  -Dfile=./build/libs/kafkahq-0.1.jar \
  -DgroupId=eu.europa.ec.digit.kafka \
  -DartifactId=kafkahq-main \
  -Dpackaging=jar \
  -Dversion=1.0.0-SNAPSHOT

mvn install:install-file \
  -Dfile=./build/libs/kafkahq-0.1-all.jar \
  -DgroupId=eu.europa.ec.digit.kafka \
  -DartifactId=kafkahq-main \
  -Dpackaging=jar \
  -Dversion=1.0.0-SNAPSHOT \
  -Dclassifier=all
