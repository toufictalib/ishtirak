# ishtirak

- Apply the below command to create third party jars in maven
mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\coderazzi.jar -DgroupId=com.ishtirak  -DartifactId=coderazzi -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\commons-io-2.4.jar -DgroupId=com.ishtirak  -DartifactId=commons-io -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\glazedlists-1.8.0_java15.jar -DgroupId=com.ishtirak  -DartifactId=glazedlists -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\jfxrt.jar -DgroupId=com.ishtirak  -DartifactId=jfxrt -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\jgoodies-common-1.7.0.jar -DgroupId=com.ishtirak  -DartifactId=jgoodies-common -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\jgoodies-forms-1.4.0.jar -DgroupId=com.ishtirak  -DartifactId=jgoodies-forms -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\QSC\eclipse-workspace\ishtirak\src\main\resources\libs\jide-oss-3.4.0.jar -DgroupId=com.ishtirak  -DartifactId=jide-oss -Dversion=1.0.0 -Dpackaging=jar

#Build
- mvn clean install