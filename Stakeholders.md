Each stakeholder participates in CSPoker for different reasons and therefore influences the (non-)functional requirements.

# Developers #

## Kenzo ##
Uses CSPoker as a case study for his thesis about annotation-driven security services
  * Non-functional requirements
    * Modularity in the design

## Guy ##
Uses CSPoker as a test server for poker A.I.
  * Functional requirements
    * Support for Texas Hold'em no limit poker
    * Persistent game traces
  * Non-functional requirements
    * Separation of the purely game related API from other features
    * Good performance

## Cedric ##
Might use CSPoker as a case study for his thesis on the automatic verification of multi-threaded java programs.
  * Non-functional requirements
    * Concurrent game logic and network server
    * Limit uses of the Java concurrency API's to a small subset of the available features

## Craig ##
Unknown interests.
## Stephan ##
Uses CSPoker to learn about SWT and GUI design. Interest in using it for testing poker A.I.
  * Non-functional requirements
    * Separate research API (A.I. players etc.) from pure game functionality (that a poker room company would be limited to)
    * Interoperability with other standard protocols (U of A bot server protocol, standard-conforming hand histories for exporting to other applications)
    * Support for other poker forms and betting structures (omaha, limit etc.)
    * Security (no ability to see hole cards of others even when having server access)

# Deployment #

## Poker room company ##
  * Functional requirements
    * Support for all the popular game types
    * Rake support
    * Admin functionality
    * Support for multiple platforms
  * Non-functional requirements
    * Security
    * Stability
    * Performance
    * ...

## End User ##
  * Functional requirements
    * Simple client GUI
    * Support for all the popular game types
    * Support for multiple platforms
  * Non-functional requirements
    * Fast response time
    * Security

# Sponsors #

## Server Sponsor ##
The CSPoker demo/web server is sponsored by an individual.
  * Non-functional requirements
    * CSPoker is open source and released under an OSI approved license
    * All dependencies are open source and available under an OSI approved license
    * Development decisions are communicated to a public mailing list or website.

# Open Source Community #
Because CSPoker is an open source project, it is necessary to be very portable and adaptable. New developers or users can always show up with previously unknown requirements.

## New Developers ##
  * Functional requirements
    * Support for multiple communication protocols
    * Support for multiple platforms
  * Non-functional requirements
    * Legibility
    * Use of an automatic build system
    * Support for popular IDE's
    * Use of popular libraries and dependencies
    * Platform independent development tools

## Distribution Channels ##
  * Non-functional requirements
    * Adherence to naming and packaging conventions
    * Use of an OSI approved license