# PcapSniffer with Java

Small app to capture traffic in a local network, in order to learn the usage of PCAP4J library in Java. This will be implemented in a future project as soon I learn the concepts.

This project has been developed for Linux, but it should work in Windows and Mac OS X too (depends on the libcap library installed in your system).

## Project structure

The project is divided in two main parts:

* **src/main/java**: Contains the main code of the project.
* **target**: Contains the compiled code of the project (uber-pcapsniffer-1.0-SNAPSHOT.jar).

### Needed libraries

The app uses the following libraries:

* **PCAP4J**: To capture packets.
* **SLF4J**: To log the packets captured.

## External needed libraries

The app uses the following C/C++ libraries in linux:

* **libpcap**: To capture packets (debian based OS: `sudo apt-get install libpcap-dev`).

or

* **libpcap-devel**: To capture packets (fedora based OS: `sudo dnf install libpcap-devel`).

For Mac OS X:

* **libpcap**: To capture packets (brew: `brew install libpcap`).

For Windows:

* **WinPcap**: To capture packets (https://www.winpcap.org/install/default.htm).

Check the [PCAP4J documentation](https://www.pcap4j.org/) for more information.


## How to build

To build the project, you need to have installed Java 8 or higher. Then, you can build the project with the following command:

```bash
mvn clean package
```

### PcapSniffer

To use the app, you need to have installed Java 8 or higher. Then, you can run the app with the following command:

```bash
sudo java -jar test-pcapsniffer-0.1-SNAPSHOT.jar
```

You might need to run the app as root, because it needs to access the network interface.

## How to contribute

If you want to contribute to the project, you can do it in the following ways:

* **Reporting bugs**: If you find a bug, you can report it in the [issues]() section.


# License
[LICENSE](/LICENSE)
