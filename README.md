

### How to Compile

 * Open Intellij and open (as project) following file
`build.sbt`

 * In IntelliJ open sbt shell, run following command

 ```
 clean - to clean project
 compile - to compile proto and app code
 run to run
 ```


### Required Component

 **Zookeeper**

 Install
 ```
 brew install zookeeper
 ```

 Run

 ```
zkServer start
```


#### Tool to make ZK easy to use in development cycle

**Exhibitor**

 You have to build it. Instruction here - https://github.com/soabase/exhibitor/wiki/Building-Exhibitor


