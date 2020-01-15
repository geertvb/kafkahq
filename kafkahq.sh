
# See https://webgate.ec.europa.eu/CITnet/stash/scm/~bastege/kafkahq-protobuf.git

java -Dmicronaut.config.files=./application.yml \
  -cp "../kafkahq-protobuf/target/kafkahq-protobuf-1.0.0-SNAPSHOT.jar:build/libs/kafkahq-0.1-all.jar" \
  org.kafkahq.App