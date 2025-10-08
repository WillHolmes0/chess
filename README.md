# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram]https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCeN4fj+NA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoMMMDgeWxGkd8vz-LsJGoQ2oGCkB-oqe8kE6TB6z6H8Ow0Whhnwr6zpdjACBCeKGKCcJBJEmApIqsGGp6OuKKEmoWBvoYSaVHZtQAJJoFQJpIOueD2D4cykFaMZxoUdkgdURljDmqh5vMZmBTAwXeXp+XwsmyCpjA6b4WgymFfyJU0eVlWhQ29HbhFVK3vyDJMlOHnil5oUuG6nJzjS+4VMuMAAOJMhusAesO-KRR2VBIutr6OYK4X1AAaolWjouNaBwCiHDmEgJqqO5rloJNORrG615+eqMDiglyDrppOz9e20WStMl7QEgABeKAcFGWUKZguU7aBLxtbm+ZmUWJb1D4kN6tDcO7Fc+k7aJaZOAAjEp2btdj0G49A+OE-ExPw71Tbhcdjn1IecgoM+8QYl92ikjzu7zSO9SPsL56XlR6Bzbyd5LsKAZrgGl6gzt4PXRkqgASjjzAWjNUY4qam6bBiuIfWZloeTdUlGAOF4XThGqaV6m2-B9vIY7XMMaYXi+AEXgoOgMRxIkkfRx5vhYKJvMSaWDTSBG-ERu0EbdD0cmqApwwUUrhTk1Fpv+qXAcwCbsIYeJe0ugJQlJxiLn2En70+brg3S8NK1MsLZcK-7SEqwuArq-Uq3MMLMBl5gP0agTFEcwxW2Lk3+09n2usflXpZr0TUCw-DMCZSgsbI6jTcFUVHU48W0AKmzG-VWnmH1dhjU057j9GaFhfrANeUMz4k2DjeAe28HKdllnbJCfcZBDXpEYFA3BjyIPQB3Nup4e4uBHgHSe+57wa2kBgpkhgiFIMlofBupZE74P-AgQCR88ppxeGTGq39Xbu2aspOiTZGLh38HdGI2BxQan4miFaSoNAp3vunZaOd872CVCXbB5ceH0K-PUGuSDUZwObk5GRORAwIGWvIjuaIrE5h7hLI6UtVaDznjQnByAch2LUAQ9xaASFq0qEtOe2s9SLwDtAlxsDdr7T8aYSW-comjiHmACx3jnoBIdDPFJAY+xyJzJEqeHCTH6L7Okg+lcGH1DMWAdJhtjZ32MUZZY6icwoQaOMVpKAYrSBQtTcIwRAggk2PEXUKAZqmV9ssZIoA1QTOtuZMYXSABykzdIXG6Nwr+FMf5uxgLhARIwWnyPaZ0pUPS+kDKGcsEZYz5k+xttMhAsz7k2W+CstZtFNmNhDmHZiHAADsbgnAoCcDECMwQ4BcQAGzwAnIYbxddFFNPTq0DoaiNFszLlmD5cwtmJkqXo0JEBsUtRGLilA0J2E7xbrdI8KBvEYgFuibxDjkFb2SW4rRNivHyN8VozJi4gkaxCQvJeHLimxK0ey1ByTmUMqVBiClgrp7CtllrbxhTSHm3gfksQdDCVwnqHSwWrKWFsIYcU5pSzzm9J0v0wZMB8WNxdg1A5nsukXPtVcp1PzQ5MQCJYDBLlNgxyQAkMAQa+wQFDQAKQgOKPVMQZkgDVEUXZqcaj1GaMyGSPQumaPHugLM2BnlBqgHACALkoBrE9dIZ19kHhVONFoktZbKCVurbW21VLLU0qcgAKwTWgRlQ7xRmpCiSGVMDOXD25Z42pfKUCTrAIQgVWrAlClnmtMVESJU6pKaEyiESEkoJnduxdcwMRjpHUqAhdaVVkIvZq-d-bahdOnUki9bpVlXr8Y+7JIS3QwF-WIV9xj9pW31U4iDLdzqA0Fi0bA6BO3QGvcOid3kXCluAOW1DUBHGdkFBy9VHAmjtqgAhRlda1ggCrdANYUGANqs1uucjuGML3JgJAGANgUBEAAGaoHXF0mAdHq0VKbUSm9ZqjasPrl+K1YEG28LdR7MlQjfkBv8F4XDYaI26flIgYMsBgDYFLYQPIBQkUZqUdmzO2dc752MOhRt4N6ZYweWMe6mD5i9sU2+9BvmMQ+aoWy09JGgtUJHrobgPIMTMa3TISh6JckIDPcAP6LUxWxeg0R5xU96ihcnAMMMyQUDxcS0tChvm0sZay+tReuXP2FaiyypUZW4vaASxurJLGatULqzKRNiLOt5ZMWDI+RWUsoHqfJxpu0jIqZ2Xw-Z6nBF+qAA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
