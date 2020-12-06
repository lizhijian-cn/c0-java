# C0-java
c0 编译器. c0 文法请参见 [here](https://c0.karenia.cc/c0/c0.html)

# Environment
* JDK15 (need to enable preview feature)
* Gradle 6.7.1

# Usage
```shell script
gradle build
java --enable-preview -jar build/libs/c0-java.jar $input -o $asm
```

可以使用助教提供的虚拟机 [vm](https://github.com/BUAA-SE-Compiling/natrium)
```shell script
git clone git@github.com:BUAA-SE-Compiling/natrium.git
cd natrium
cargo build
cd target/debug/
export PATH=$PATH:$PWD
```
现在可以运行编译器生成的二进制文件了
```
navm $asm
```

# TODO
* control flow check
* support DaclNode

# Grade
57/57 passed
