# C0-java
c0 编译器, 具体文法请参见 [here](https://c0.karenia.cc/c0/c0.html)

# Environment
* JDK15 (need to enable preview feature)
* Gradle 6.7.1

# Usage
```sh
gradle build
java --enable-preview -jar build/libs/c0-java.jar $input -o $asm
```

可以使用助教提供的虚拟机 [vm](https://github.com/BUAA-SE-Compiling/natrium/releases)
```sh
wget https://github.com/BUAA-SE-Compiling/natrium/releases/download/v0.1.3/navm-linux-amd64-musl
mv navm-linux-amd64-musl navm
sudo chmod +x navm

# 现在可以运行编译器生成的二进制文件了
./navm $asm
```

# TODO
* control flow check
* support DaclNode
* location
* error handler

# Grade
57/57 passed
![](https://raw.githubusercontent.com/lizhijian-cn/static/master/img/20201213024243.png)
