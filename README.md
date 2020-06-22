# jmh-test

# How to run
https://openjdk.java.net/projects/code-tools/jmh/

# build
mvn clean install

# run help
java -jar target/benchmarks.jar -h

# list benchmarks
java -jar target/benchmarks.jar -l

# list benchmarks with the regex filter
java -jar target/benchmarks.jar -lp <regex>

# run benchmark blah_ABC_01, hello_ABC_world with 1 fork, 2 warmup iter, 3 measure iter, 6 threads
java -jar target/benchmarks.jar ABC -f 1 -wi 2 -i 3 -t 6
