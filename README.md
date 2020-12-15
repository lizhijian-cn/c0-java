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
```

可以使用助教提供的虚拟机 [vm](https://github.com/BUAA-SE-Compiling/natrium/releases)
```sh
wget https://github.com/BUAA-SE-Compiling/natrium/releases/download/v0.1.3/navm-linux-amd64-musl
mv navm-linux-amd64-musl navm
sudo chmod +x navm

# 现在可以运行编译器生成的二进制文件了
./navm example/fn-main.o0

# the sum 1 to 50 equals to:
# 5050
```


# TODO
* control flow check
* support DaclNode
* location
* error handler

# Grade
57/57 passed

![](https://raw.githubusercontent.com/lizhijian-cn/static/master/img/20201213024243.png)
