# StaySafe

Distributed Systems 2020-2021, 1st semester project


## Authors

**Group G15**

### Code identification

In all the source files (including POMs), please replace __GXX__ with your group identifier.  
The group identifier is composed of a G and the group number - always with two digits.  
This change is important for code dependency management, to make sure that your code runs using the correct components and not someone else's.

### Team members
 

| Number | Name              | User                             | Email                               |
| -------|-------------------|----------------------------------| ------------------------------------|
| 93601  | Miguel Santos     | <https://github.com/FracassandoCasualmente>   | <mailto:miguel.conrado.santos@tecnico.ulisboa.pt>   |
| 93586  | João Rodrigues       | <https://github.com/joaorodrigues-ist>     | <mailto:joao.pedro.freixo.rodrigues@tecnico.ulisboa.pt>     |


### Task leaders

*(fill-in table below with with team members assigned as leader to each task set; and then delete this line)*  

| Task set | To-Do                         | Leader              |
| ---------|-------------------------------| --------------------|
| core     | protocol buffers, dgs-client | _(whole team)_      |
| T1       | sniffer_join, sniffer_info, individual_infection_probability, researcher, sniffer       | _Miguel Santos_ |
| T2       | report, aggregate_infection_probability, journalist, sniffer               | _João Rodrigues_       |
<!--| T3       | track, trackMatch, trace      | _Charlie Rules_     |-->
| T3       | test T1                       | _Bob Systems_     |
| T4       | test T2                       | _Alice Distributed_ |
<!--| T6       | test T3                       | _Bob Systems_       |-->


## Getting Started

The overall system is composed of multiple modules.
The main server is the _dgs_.
The clients are the _sniffer_, the _journalist_ and the _researcher_.

See the [project statement](https://github.com/tecnico-distsys/StaySafe/blob/main/part1.md) for a full description of the domain and the system.

### Prerequisites

Java Developer Kit 11 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```

### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The integration tests are skipped because they require the servers to be running.


## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [gRPC](https://grpc.io/) - RPC framework


## Versioning

We use [SemVer](http://semver.org/) for versioning. 
