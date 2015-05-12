# Introduction #

In the current version of CSPoker there is some scattering of responsibilities to different modules. The lines between some modules are also vague. Therefor a partial redesign of the server into more distinct modules is proposed. We believe these changes will enhance the scalability and extendibility of CSPoker.
These modules are just general outlines and any comments are welcome.

### API ###
There are a couple of XSD files shared between the other files:
**_actions.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/actions" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:complexType name="tableSpecificAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:action">
				<xs:sequence>
					<xs:element name="tableId" type="xs:long" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="action" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="xs:long" use="required" />
	</xs:complexType>
</xs:schema>
```
**_table.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/elements/table"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:complexType name="tableConfiguration">
		<xs:sequence />
		<xs:attribute name="smallBlind" type="xs:int" use="required" />
		<xs:attribute name="bigBlind" type="xs:int" use="required" />
		<xs:attribute name="smallBet" type="xs:int" use="required" />
		<xs:attribute name="bigBet" type="xs:int" use="required" />
		<xs:attribute name="autoDeal" type="xs:boolean" use="required" />
		<xs:attribute name="autoBlinds" type="xs:boolean" use="required" />
		<xs:attribute name="maxNbPlayers" type="xs:int" use="required" />
		<xs:attribute name="delay" type="xs:long" use="required" />
	</xs:complexType>
</xs:schema>
```

# Modules #

## Lobby ##
The lobby is the access point to all the tables. An admin API could be provided for creating and managing these tables.
### API ###
  * getAllTables()
  * createTable()
  * getTableInfo()
**_lobby.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/lobby"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/lobby" xmlns:ns1="http://www.cspoker.org/api/2008-8/elements/table"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/elements/table"
		schemaLocation="../elements/table.xsd" />
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="createTableAction" type="tns:createTableAction" />
	<xs:element name="joinTableAction" type="tns:joinTableAction" />
	<xs:element name="tableInformationAction" type="tns:tableInformationAction" />
	<xs:element name="tableListAction" type="tns:tableListAction" />
	<xs:complexType name="createTableAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence>
					<xs:element name="name" type="xs:string" minOccurs="0" />
					<xs:element name="configuration" type="ns1:tableConfiguration"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="joinTableAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence>
					<xs:element name="tableId" type="xs:long" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="tableInformationAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
				<xs:attribute name="tableId" type="xs:long" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="tableListAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## Chat ##
The chat functionality will be modularized. The chat module will support different groups (e.g. my friends) between whom communication is possible. At the creation of a table a group for this table will be added automatically to the groups.
### API ###
  * register()
  * unregister()
  * sendMessage()
**_chat.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/chat"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/chat"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="sendServerMessageAction" type="tns:sendServerMessageAction" />
	<xs:element name="sendTableMessageAction" type="tns:sendTableMessageAction" />
	<xs:complexType name="sendServerMessageAction">
		<xs:complexContent>
			<xs:extension base="tns:sendMessageAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="sendMessageAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence>
					<xs:element name="message" type="xs:string" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="sendTableMessageAction">
		<xs:complexContent>
			<xs:extension base="tns:sendMessageAction">
				<xs:sequence>
					<xs:element name="tableId" type="xs:long" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## Cashier ##
The cashier is responsible to provide money to the players. This could be done by exchanging real dollars or through the use of a revenue generating function.
### API ###
  * requestMoney()
**_cashier.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/cashier"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/cashier"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="moneyAmountAction" type="tns:moneyAmountAction" />
	<xs:element name="requestMoneyAction" type="tns:requestMoneyAction" />
	<xs:complexType name="moneyAmountAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="requestMoneyAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## HoldemTable ##
A specialization of the table for only Holdem games. This is done to make it easier to add other kinds of tables. The table also has an event buffer. This buffer is used when a player tries to watch later on to display all events that have happened in the ongoing game. A table has a configuration object that contains all config info:
  * auto deal
  * explicit blinds
  * nb players
  * small blind
  * big blind
  * game type
  * limit

A player can choose to join a table. When he joins a table he can choose how much chips he wants to transfer to this table.
### API ###
  * sitin(amount)
  * ...
**_holdemtable.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/holdemtable"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/holdemtable"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="leaveTableAction" type="tns:leaveTableAction" />
	<xs:element name="sitInAction" type="tns:sitInAction" />
	<xs:element name="startGameAction" type="tns:startGameAction" />
	<xs:complexType name="leaveTableAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="startGameAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="sitInAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence>
					<xs:element name="seatId" type="xs:long" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## HoldemPlayer ##
Once a player joins the game, the game API becomes available.
### API ###
  * call()
  * bet()
  * check()
  * raise()
  * ...
**_holdemplayer.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/holdemplayer"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/holdemplayer"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="betOrRaiseAction" type="tns:betOrRaiseAction" />
	<xs:element name="checkOrCallAction" type="tns:checkOrCallAction" />
	<xs:element name="foldAction" type="tns:foldAction" />
	<xs:element name="leaveGameAction" type="tns:leaveGameAction" />
	<xs:complexType name="betOrRaiseAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
				<xs:attribute name="amount" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="checkOrCallAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="foldAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="leaveGameAction">
		<xs:complexContent>
			<xs:extension base="actions:tableSpecificAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## Account ##
This module should provide the following actions:
  * create new accounts
  * change password
  * statistics
  * authentication
  * (avatar)
### API ###
**_account.xsd_**
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0"
	targetNamespace="http://www.cspoker.org/api/2008-8/actions/account"
	xmlns:tns="http://www.cspoker.org/api/2008-8/actions/account"
	xmlns:actions="http://www.cspoker.org/api/2008-8/actions/actions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xs:import namespace="http://www.cspoker.org/api/2008-8/actions/actions"
		schemaLocation="actions.xsd" />
	<xs:element name="changePasswordAction" type="tns:changePasswordAction" />
	<xs:element name="createAccountAction" type="tns:createAccountAction" />
	<xs:element name="getAvatarAction" type="tns:getAvatarAction" />
	<xs:element name="setAvatarAction" type="tns:setAvatarAction" />
	<xs:complexType name="changePasswordAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
				<xs:attribute name="passwordHash" type="xs:string" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="createAccountAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
				<xs:attribute name="username" type="xs:string" />
				<xs:attribute name="passwordHash" type="xs:string" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="getAvatarAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
				<xs:attribute name="playerId" type="xs:long" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
	<xs:complexType name="setAvatarAction">
		<xs:complexContent>
			<xs:extension base="actions:action">
				<xs:sequence />
				<xs:attribute name="avatar" type="xs:base64Binary" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## Tournament ##
Joining tables and increasing blinds should be supported.

## Authentication ##
For the authentication md5-hash of the password will be used in the header.