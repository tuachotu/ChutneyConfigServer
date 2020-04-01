# ChutneyConfigServer

Chutney is a Highly Available Config Server .
![photo](https://github.com/tuachotu/ChutneyConfigServer/blob/master/Chutney.jpg) 

## Config 
Modern software make various decision based on Config. This is specially true for web based software. Use cases like 
* FE  - hide/show features(boolean), incremental rollout, change style etc
* BE -  business logic(e.g supported states),  timeouts, endpoints, s3 bucket names,
Use cases are ever growing

To make this more complex some time FE depends on BE for such config anf BE depends on some data store.

Managing such setup has its own challenges 
* Configs are often stored locally
* Changing config-
  *  BE  requires production roll out
  *  FE may require a new app release or push a new web app

This makes process slow and error prone.


## Config Server
"Chutney" is a config server which provides following features
* Stores Config for its client (BE & FE) in key value pair
* Exposes GRPC interface to set/read configs. Can be used by FE/BE in same way.
* Can push config updates to clients
* Super Fast Reads- configs cached in memory,  access is only a grpc call away
* Read and Write are separated to avoid accidental modifications
* Very lightweight (MVP < 400 lines of code)
* Modular Data Store can be replaced others*

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


