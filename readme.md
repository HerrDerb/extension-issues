Execute steps for error

1. `./gradlew clean publishToMavenLocal`
2. `MY_PROPS__THIS__SUB_ID=1 ./gradlew :application:quarkusDev`

When removing BadConfig an republish, GoodConfig with the additional wrapper will work.