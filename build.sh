#!/usr/bin/env sh
./mvnw clean install
pwd
unzip target/lmf-boot-prototype-0.0.1-SNAPSHOT.jar -d target/lmf-boot-prototype-0.0.1-SNAPSHOT
".:$(echo target/lmf-boot-prototype-SNAPSHOT/BOOT-INF/lib/*.jar | tr ' ' ':')"
native-image  --no-server \
              -H:ReflectionConfigurationFiles=graal.json \
              -Dio.netty.noUnsafe=true \
              -H:+ReportUnsupportedElementsAtRuntime \
              -Dfile.encoding=UTF-8 \
              -cp ".:$(echo target/lmf-boot-prototype-SNAPSHOT/BOOT-INF/lib/*.jar | tr ' ' ':')":target/lmf-boot-prototype-0.0.1-SNAPSHOT/BOOT-INF/classes \
              com.haulmont.joker.demo.GraalDemoApplication
