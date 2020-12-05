# Environment
* JDK15 (need enable preview feature)
* Gradle 6.7.1

# Usage
```shell script
gradle build
java --enable-preview -jar build/libs/c0-java.jar $input -o $asm
```

you may need a [vm](https://github.com/BUAA-SE-Compiling/natrium) to run a `.o0` file
```shell script
git clone git@github.com:BUAA-SE-Compiling/natrium.git
cd natrium
cargo build
cd target/debug/
export PATH=$PATH:$PWD
```
now you can use navm to run assembly file
```
navm $asm
```

# TODO
* control flow check
* support DaclNode

# Grade
57/57 passed