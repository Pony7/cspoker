# Introduction #

Some of these ideas are related and can be combined in a single project. We ask you to contact the mailing list with your project proposals so we can discuss them.

# Server #

The server is written in Java.

### Improve security ###
Communication in CSPoker is unsafe. The HTTP module can be altered to use HTTPS for communication. The sockets module could use some of the built-in Java security tools to encrypt data.

### Support other types of games ###
At this time, only Texas Hold'em is supported. Any other poker related game can be implemented using the server framework.

### Add new communication modules ###
CSPoker already has a wide set of communication modules: RMI, RESTful HTTP, XML over Sockets. Maybe we're not thinking of all possible use cases.

### Handle network failure ###
Right now, when the network fails, you're in trouble. The client can't handle missing events and the server might kick players from the table. This behaviour can be more intelligent without complicating the server code too much.

### Persistency ###
Persistency is a key feature that needs to be implemented before the final release. Speaks for itself basically.

### Concurrency ###
CSPoker is written with concurrency in mind. Still, testing and optimization is needed to make the server fully scalable.

### Tournament mode ###
Support for tournaments with increasing blind values is needed both for human players and for some AI work.

# Client #

### Extend the Java FX client ###
When new functionality is added to the server, support for that functionality is needed in the client implementation. (see for instance other types of games)

### Adobe Flex client ###
A client in Flash/Adobe Flex would allow us to make CSPoker more popular with online game sites. The implementation is pretty straightforward using either the HTTP or XML over Sockets communication module.

A client in .NET could also extend our user base. Technically even an AJAX client is possible when leveraging the HTTP communication module.

### Facebook Application ###
A Flash client could be turned in to a Facebook app.

### Google Android client ###
Google Android provides a very easy to program environment for handheld devices. We believe it will grow to be very popular. Being the first poker software for Android would give a great boost to popularity and possibly extend our usebase by a large number.

# Artificial Intelligence #

### Module for statistical analysis of hand and card odds ###
A module that can do statistical analysis is a key requirement to develop some AI bots.

### Genetic algorithm bot ###
A genetic algorithm can be designed to learn how to play poker using for instance the statistical module and the tournament game type for selection. A suitable (non-bitstring) representation is needed to define decisions. Genetic classification algorithms can be an inspiration.

### Neural network bot / Support vector machine bot ###
Similar to the genetic algorithm bot but then using other machine learning techniques.

### Reinforcement learning bot ###
More research needed.