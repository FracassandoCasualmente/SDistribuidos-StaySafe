# Demonstration guide and tutorial

## Getting started

* First of all, go to the root directory **G15-StaySafe** (or equivalent)
* Be sure you've ran the command to install and compile all the code
```
mvn clean install -DskipTests
```
* **Now** chose **1** of the following:
	(1) Open 4 terminals in this directory if you want to test each client individually
			**OR**
	(2) Open 2 terminals in this directory if you want to use a general client

* If you chose (1) : change to this directories:
	* (**{directory}** `run command`)
	* **dgs-server**  `mvn exec:java`
	* **journalist**  `./target/appassembler/bin/journalist localhost 8080`
	* **researcher**  `./target/appassembler/bin/researcher localhost 8080`
	* **sniffer**	  `./target/appassembler/bin/sniffer localhost 8080 IST-T Taguspark, Porto Salvo` (optional->) `< ../demo/tests/SnifferInputs.txt`

* If you chose (2) : change to this directories:
	* (**{directory}** `run command`)
	* **dgs-server**  `mvn exec:java`
	* **dgs-client**  `mvn exec:java`

* After you do that, you'll run the modules by order with the respective *run command*
	* While executing each client module, you'll be able to call the command `help` that will display the other available commands and instructions on how to use them

# Demonstration

## **run** the project with option **(1)** (showed above) including the **optional** part
	* If you now type `ctrl_ping` in *journalist* you should see a message confirming that the server is running
	* Type `help` to see the list of available commands, you may try it in *researcher* too.
	* If you now type in *researcher*:
		
		`ctrl_ping` You'll verify if the server is running
		`single_prob 32,33,48,49,24,25,16,17` You'll see the individual infection probability(**IIP**) of each person registered, the even numbers are infected (IIP = 1) and the odds are not infected (0 <= IIP <= 1)
		`mean_dev` You'll see the mean and standart deviation of the **IIP** of the non-infected declared citizens
		`percentiles` You'll see the 50th, 25th and 75th percentiles of the **IIP**
		`ctrl_clear` Makes the server state clear all the values, mean_dev and percentiles will be zero
		`ctrl_init` Initializes the server with some new observations, mean_dev and percentiles will stop being zero

## Now let's see some of the **exceptions** that might be triggered:
	## open **dgs-client** with `mvn exec:java`
	
	* Type `ctrl_clear potato` and an error message will show up because you used too many arguments
	* Type `single_prob 400` and an error message will show up because there is no citizen with that ID
	* Type `single_prob potato` and an error message will show up because "potato" is not a valid number
	* Type `sniffer_info ISCTE` and an error message will show up because there is not a sniffer with that name
	* Type `kappa` and an error message will show up because it isn't a valid command
	* Type
















	
