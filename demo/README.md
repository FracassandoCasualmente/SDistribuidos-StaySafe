# Demonstration guide and tutorial


### Brief Notes
#### In this guide commands' syntax meanings are:
* `{*thing*}` - thing is obligatory
* `[*thing]` - thing is optional

##PART 1

### Getting started

* First of all, go to the root directory **G15-StaySafe** (or equivalent)
* Be sure you've ran the command to install and compile all the code
```
mvn clean install -DskipTests
```
* **Now** chose **1** of the following:
	**(1)** Open 4 terminals in this directory if you want to test each client individually
			**OR**
	**(2)** Open 2 terminals in this directory if you want to use a general client

* If you chose **(1)** : change to this directories:
	* *Note*: (**directory** `run command`)
	* *Note*: RepId is the id of the replica you want to bind or connect
	* *Note*: RepPort is the port you want your replica to bind
	* **dgs-server**  `./target/appassembler/bin/dgs-server localhost 2181 {RepId} localhost {RepPort}` **OR** `./execute.sh {RepId}`
	* **journalist**  `./target/appassembler/bin/journalist localhost 2181 [%RepId%]`
	* **researcher**  `./target/appassembler/bin/researcher localhost 2181 [%RepId%]`
	* **sniffer**	  `./target/appassembler/bin/sniffer localhost 2181 IST-T Taguspark, Porto Salvo [%RepId%]` (optional->) `< ../demo/tests/SnifferInputs.txt`

* If you chose **(2)** : change to this directories:
	* *Note*: (**directory** `run command`)
	* **dgs-server**  `./execute.sh {RepId}`
	* **dgs-client**  `./execute.sh [%RepId%]`

* After you do that, you'll run the modules by order with the respective *run command*
	* While executing each client module, you'll be able to call the command `help` that will display the other available commands and instructions on how to use them

# Demonstration

## **run** the project with option **(1)** (showed above) including the **optional** part
	* If you now type `ctrl_ping` in *journalist* you should see a message confirming that the server is running
	* Type `help` to see the list of available commands, you may try it in *researcher* too.
	* If you now type in *researcher*

		`ctrl_ping` You'll verify if the server is running
		`single_prob 32,33,48,49,24,25,16,17` You'll see the individual infection probability(**IIP**) of each person registered, the even numbers are infected (IIP = 1) and the odds are not infected (0 <= IIP <= 1)
		`mean_dev` You'll see the mean and standart deviation of the **IIP** of the non-infected declared citizens
		`percentiles` You'll see the 50th, 25th and 75th percentiles of the **IIP**
		`ctrl_clear` Makes the server state clear all the values, mean_dev and percentiles will be zero
		`ctrl_init` Initializes the server with some new observations, mean_dev and percentiles will stop being zero

## Now let's see some of the **exceptions** that might be triggered
	* open **dgs-client** with `./execute.sh`
	
	* Type `ctrl_clear potato` and an error message will show up because you used too many arguments
	* Type `single_prob 400` and an error message will show up because there is no citizen with that ID
	* Type `single_prob potato` and an error message will show up because "potato" is not a valid number
	* Type `sniffer_info ISCTE` and an error message will show up because there is not a sniffer with that name
	* Type `kappa` and an error message will show up because it isn't a valid command
	* Type `sniffer_join tecnico,tagus` and then `sniffer_join tecnico,alameda` and and error will show up because the sniffer name is already in use with a different address


## PART 2
	* Open terminal in root directory and run `mvn clean install -DskipTests`
	* Start ZooKeeper Server, maybe also do deleteall /grpc/staysafe/dgs
	* Open a terminal in dgs-server and do `./run3.sh` to run 3 servers (to kill them you'll need to do `./crash-servers.sh`)
	       * In alternative, run `./execute 1` and then `./execute 2` and `./execute 3` in different terminals or use the regular command
	
	* Run a sniffer in it's module with `./execute.sh IST-Taguspark Algum lugar lá em Oeiras %2% < ../demo/tests/SnifferInputs.txt`, notice that the sniffer will also sleep for 10 seconds at the half of the way
	* Run the researcher in the respective module with `execute.sh %2%` and write `mean_dev`
	* Close the researcher and open it again with `èxecute.sh %1%` and do `mean_dev` again and you will see that the data is the same in both replicas
	* Now, open another researcher with `execute.sh %2%` and write `ctrl_clear` and  then, `mean_dev` in both researchers, if you are fast enough, you can see than researcher 2 received 0.000,0.000 and researcher 1 received 0.224,0.185. The replicas didn't propagate (yet)
	* After a while, type `ctrl_init` in researcher 1 and use `kill [pid]` where `pid` is the PID of the replica 1 (you need to search for it in the server shell). If you are fast enough, when you type in researcher 1 `single_prob 33`, a message will show up telling that are no updates, that's because the server you are now connected too has a 'smaller' vectorial timestamp than the one you have seen in replica 2, so the frontend discarded the message has we can't ensure you haven't seen a version posterior to this.















	
