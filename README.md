# C0-java
c0 编译器, 具体文法请参见 [here](https://c0.karenia.cc/c0/c0.html)

# Environment
* JDK15 (need to enable preview feature)

推荐使用[sdkman](https://sdkman.io/)来管理、下载相关的SDK

# Usage
``` sh
git clone https://github.com/lizhijian-cn/c0-java.git
cd c0-java

./gradlew build
java --enable-preview -jar build/libs/c0-java.jar example/fn-main.c0 -o example/fn-main.o0

./navm example/fn-main.o0
# the sum from 1 to 50 equals to:
# 5050
```
其中虚拟机[navm](https://github.com/BUAA-SE-Compiling/natrium)是助教提供的

# TODO
* control flow check
* support DaclNode
* location
* error handler

# Grade
57/57 passed

![](https://raw.githubusercontent.com/lizhijian-cn/static/master/img/20201213024243.png)
