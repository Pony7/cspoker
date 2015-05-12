# Introduction #

Communication with the poker server happens through a any of these connections:
  * HTTP connections with XML content (RESTful webservice)
  * Socket connection sending XML content
  * RMI

# XML Serialization #
XML serialization is done by JAXB 2.1. The exact protocol can easily be derived from the source code.

The following XSD specification of the protocol has automatically been compiled from the JAXB annotated Java source.

## XML over socket ##
Immediatly after setting up the TCP connection with the server, a _loginAction_ must be sent to the server. Then any _dispatchableAction_ can be sent.
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" targetNamespace="http://www.cspoker.org/api/2008-11/"
	xmlns:tns="http://www.cspoker.org/api/2008-11/" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xs:element name="loginAction" type="tns:loginAction" />

	<xs:complexType name="loginAction">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence>
					<xs:element name="username" type="xs:string" minOccurs="0" />
					<xs:element name="passwordHash" type="xs:string"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="dispatchableAction" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="xs:long" use="required" />
	</xs:complexType>
</xs:schema>
```

## RESTful HTTP ##
Authentication in the case of HTTP is done through the _Authorization_ header. For now, only Basic Authentication is supported. A _httpRequest_ payload consists of a list of _dispatchableActions_. The following _httpResponse_ contains a list of events that occured since the last request and the results of the actions performed. Actions are requested and acknowledged in the same request/response pair.
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" targetNamespace="http://www.cspoker.org/api/2008-11/"
	xmlns:tns="http://www.cspoker.org/api/2008-11/" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xs:element name="httpRequest" type="tns:httpRequest" />

	<xs:element name="httpResponse" type="tns:httpResponse" />

	<xs:complexType name="httpRequest">
		<xs:sequence>
			<xs:element name="actions" type="tns:dispatchableAction"
				maxOccurs="unbounded" />
		</xs:sequence>
	</xs:complexType>

	<xs:complexType name="dispatchableAction" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="tns:eventId" />
	</xs:complexType>

	<xs:simpleType name="eventId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="httpResponse">
		<xs:sequence>
			<xs:element name="events" type="tns:serverEvent"
				maxOccurs="unbounded" />
			<xs:element name="actionResults" type="tns:actionEvent"
				maxOccurs="unbounded" />
		</xs:sequence>
	</xs:complexType>

	<xs:complexType name="serverEvent" abstract="true">
		<xs:sequence />
	</xs:complexType>

	<xs:complexType name="actionEvent" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="tns:eventId" />
	</xs:complexType>
</xs:schema>
```

## Actions ##
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" targetNamespace="http://www.cspoker.org/api/2008-11/"
	xmlns:tns="http://www.cspoker.org/api/2008-11/" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xs:element name="betOrRaiseAction" type="tns:betOrRaiseAction" />

	<xs:element name="changePasswordAction" type="tns:changePasswordAction" />

	<xs:element name="checkOrCallAction" type="tns:checkOrCallAction" />

	<xs:element name="createAccountAction" type="tns:createAccountAction" />

	<xs:element name="createHoldemTableAction" type="tns:createHoldemTableAction" />

	<xs:element name="foldAction" type="tns:foldAction" />

	<xs:element name="getAvatarAction" type="tns:getAvatarAction" />

	<xs:element name="getHoldemTableInformationAction" type="tns:getHoldemTableInformationAction" />

	<xs:element name="getMoneyAmountAction" type="tns:getMoneyAmountAction" />

	<xs:element name="getPlayerIDAction" type="tns:getPlayerIDAction" />

	<xs:element name="getTableListAction" type="tns:getTableListAction" />

	<xs:element name="joinHoldemTableAction" type="tns:joinHoldemTableAction" />

	<xs:element name="leaveGameAction" type="tns:leaveGameAction" />

	<xs:element name="leaveTableAction" type="tns:leaveTableAction" />

	<xs:element name="loginAction" type="tns:loginAction" />

	<xs:element name="requestMoneyAction" type="tns:requestMoneyAction" />

	<xs:element name="sendServerMessageAction" type="tns:sendServerMessageAction" />

	<xs:element name="sendTableMessageAction" type="tns:sendTableMessageAction" />

	<xs:element name="serverChatInterestAction" type="tns:serverChatInterestAction" />

	<xs:element name="setAvatarAction" type="tns:setAvatarAction" />

	<xs:element name="sitInAction" type="tns:sitInAction" />

	<xs:element name="sitInAnywhereAction" type="tns:sitInAnywhereAction" />

	<xs:element name="tableChatInterestAction" type="tns:tableChatInterestAction" />

	<xs:complexType name="loginAction">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence>
					<xs:element name="username" type="xs:string" minOccurs="0" />
					<xs:element name="passwordHash" type="xs:string"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="dispatchableAction" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="tns:eventId" />
	</xs:complexType>

	<xs:simpleType name="eventId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="changePasswordAction">
		<xs:complexContent>
			<xs:extension base="tns:accountAction">
				<xs:sequence />
				<xs:attribute name="passwordHash" type="xs:string" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="accountAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="createAccountAction">
		<xs:complexContent>
			<xs:extension base="tns:accountAction">
				<xs:sequence />
				<xs:attribute name="username" type="xs:string" />
				<xs:attribute name="passwordHash" type="xs:string" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="getAvatarAction">
		<xs:complexContent>
			<xs:extension base="tns:accountAction">
				<xs:sequence />
				<xs:attribute name="playerId" type="tns:playerId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="playerId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="setAvatarAction">
		<xs:complexContent>
			<xs:extension base="tns:accountAction">
				<xs:sequence />
				<xs:attribute name="avatar" type="xs:base64Binary" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="getPlayerIDAction">
		<xs:complexContent>
			<xs:extension base="tns:accountAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="getMoneyAmountAction">
		<xs:complexContent>
			<xs:extension base="tns:cashierAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="cashierAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="requestMoneyAction">
		<xs:complexContent>
			<xs:extension base="tns:cashierAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sendServerMessageAction">
		<xs:complexContent>
			<xs:extension base="tns:sendMessageAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sendMessageAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:chatAction">
				<xs:sequence>
					<xs:element name="message" type="xs:string" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="chatAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sendTableMessageAction">
		<xs:complexContent>
			<xs:extension base="tns:sendMessageAction">
				<xs:sequence />
				<xs:attribute name="tableID" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="tableId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="createHoldemTableAction">
		<xs:complexContent>
			<xs:extension base="tns:lobbyAction">
				<xs:sequence>
					<xs:element name="name" type="xs:string" minOccurs="0" />
					<xs:element name="configuration" type="tns:tableConfiguration"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="lobbyAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

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

	<xs:complexType name="joinHoldemTableAction">
		<xs:complexContent>
			<xs:extension base="tns:lobbyAction">
				<xs:sequence />
				<xs:attribute name="tableId" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="getHoldemTableInformationAction">
		<xs:complexContent>
			<xs:extension base="tns:lobbyAction">
				<xs:sequence />
				<xs:attribute name="tableId" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="getTableListAction">
		<xs:complexContent>
			<xs:extension base="tns:lobbyAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="detailedHoldemTable">
		<xs:complexContent>
			<xs:extension base="tns:table">
				<xs:sequence>
					<xs:element name="players" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="player" type="tns:seatedPlayer"
									minOccurs="0" maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
					<xs:element name="playing" type="xs:boolean" />
					<xs:element name="property" type="tns:tableConfiguration"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="table">
		<xs:sequence />
		<xs:attribute name="id" type="tns:tableId" />
		<xs:attribute name="name" type="xs:string" />
	</xs:complexType>

	<xs:complexType name="seatedPlayer">
		<xs:complexContent>
			<xs:extension base="tns:player">
				<xs:sequence>
					<xs:element name="seatId" type="tns:seatId" minOccurs="0" />
					<xs:element name="stackValue" type="xs:int" />
					<xs:element name="betChipsValue" type="xs:int" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="player">
		<xs:sequence />
		<xs:attribute name="id" type="tns:playerId" />
		<xs:attribute name="name" type="xs:string" />
	</xs:complexType>

	<xs:simpleType name="seatId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="leaveTableAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="holdemTableAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:lobbyAction">
				<xs:sequence />
				<xs:attribute name="tableId" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sitInAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableAction">
				<xs:sequence />
				<xs:attribute name="seatId" type="tns:seatId" />
				<xs:attribute name="buyIn" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sitInAnywhereAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableAction">
				<xs:sequence />
				<xs:attribute name="buyIn" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="betOrRaiseAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemPlayerAction">
				<xs:sequence />
				<xs:attribute name="amount" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="holdemPlayerAction" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="checkOrCallAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemPlayerAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="foldAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemPlayerAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="leaveGameAction">
		<xs:complexContent>
			<xs:extension base="tns:holdemPlayerAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="serverChatInterestAction">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="tableChatInterestAction">
		<xs:complexContent>
			<xs:extension base="tns:dispatchableAction">
				<xs:sequence />
				<xs:attribute name="tableID" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>
</xs:schema>
```

## Events ##
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" targetNamespace="http://www.cspoker.org/api/2008-11/"
	xmlns:tns="http://www.cspoker.org/api/2008-11/" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xs:element name="actionPerformedEvent" type="tns:actionPerformedEvent" />

	<xs:element name="holdemTableTreeEventWrapper" type="tns:holdemTableTreeEventWrapper" />

	<xs:element name="illegalActionEvent" type="tns:illegalActionEvent" />

	<xs:element name="messageEvent" type="tns:messageEvent" />

	<xs:element name="remoteExceptionEvent" type="tns:remoteExceptionEvent" />

	<xs:element name="serverChatEvents" type="tns:serverChatEvents" />

	<xs:element name="tableChatEvents" type="tns:tableChatEvents" />

	<xs:element name="tableCreatedEvent" type="tns:tableCreatedEvent" />

	<xs:element name="tableRemovedEvent" type="tns:tableRemovedEvent" />

	<xs:complexType name="serverChatEvents">
		<xs:complexContent>
			<xs:extension base="tns:serverEvent">
				<xs:sequence>
					<xs:element name="chatEvent" type="tns:chatEvent"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="serverEvent" abstract="true">
		<xs:sequence />
	</xs:complexType>

	<xs:complexType name="chatEvent" abstract="true">
		<xs:sequence />
	</xs:complexType>

	<xs:complexType name="tableChatEvents">
		<xs:complexContent>
			<xs:extension base="tns:serverEvent">
				<xs:sequence>
					<xs:element name="chatEvent" type="tns:chatEvent"
						minOccurs="0" />
				</xs:sequence>
				<xs:attribute name="tableID" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="tableId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="messageEvent">
		<xs:complexContent>
			<xs:extension base="tns:chatEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
					<xs:element name="message" type="xs:string" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="player">
		<xs:sequence />
		<xs:attribute name="id" type="tns:playerId" />
		<xs:attribute name="name" type="xs:string" />
	</xs:complexType>

	<xs:simpleType name="playerId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="tableCreatedEvent">
		<xs:complexContent>
			<xs:extension base="tns:lobbyEvent">
				<xs:sequence>
					<xs:element name="table" type="tns:table" minOccurs="0" />
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="lobbyEvent" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:lobbyTreeEvent">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="lobbyTreeEvent" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:serverEvent">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="table">
		<xs:sequence />
		<xs:attribute name="id" type="tns:tableId" />
		<xs:attribute name="name" type="xs:string" />
	</xs:complexType>

	<xs:complexType name="tableRemovedEvent">
		<xs:complexContent>
			<xs:extension base="tns:lobbyEvent">
				<xs:sequence>
					<xs:element name="table" type="tns:table" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="tableList">
		<xs:sequence>
			<xs:element name="tables" minOccurs="0">
				<xs:complexType>
					<xs:sequence>
						<xs:element name="table" type="tns:table" minOccurs="0"
							maxOccurs="unbounded" />
					</xs:sequence>
				</xs:complexType>
			</xs:element>
		</xs:sequence>
	</xs:complexType>

	<xs:complexType name="detailedHoldemTable">
		<xs:complexContent>
			<xs:extension base="tns:table">
				<xs:sequence>
					<xs:element name="players" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="player" type="tns:seatedPlayer"
									minOccurs="0" maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
					<xs:element name="playing" type="xs:boolean" />
					<xs:element name="property" type="tns:tableConfiguration"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="seatedPlayer">
		<xs:complexContent>
			<xs:extension base="tns:player">
				<xs:sequence>
					<xs:element name="seatId" type="tns:seatId" minOccurs="0" />
					<xs:element name="stackValue" type="xs:int" />
					<xs:element name="betChipsValue" type="xs:int" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="seatId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

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

	<xs:complexType name="betEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
					<xs:element name="amount" type="xs:int" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="holdemTableEvent" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableTreeEvent">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="holdemTableTreeEvent" abstract="true">
		<xs:sequence />
	</xs:complexType>

	<xs:complexType name="bigBlindEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
				<xs:attribute name="amount" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="callEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="checkEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="foldEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="leaveTableEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="newCommunityCardsEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="communityCards" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="card" type="tns:card" minOccurs="0"
									maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="card" final="extension restriction">
		<xs:sequence />
		<xs:attribute name="rank" type="tns:rank" />
		<xs:attribute name="suit" type="tns:suit" />
	</xs:complexType>

	<xs:complexType name="newDealEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="players" type="tns:seatedPlayer"
						nillable="true" minOccurs="0" maxOccurs="unbounded" />
					<xs:element name="dealer" type="tns:seatedPlayer"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="newRoundEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="round" type="tns:rounds" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="nextPlayerEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="raiseEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
				<xs:attribute name="amount" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="showHandEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:showdownPlayer"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="showdownPlayer">
		<xs:complexContent>
			<xs:extension base="tns:player">
				<xs:sequence>
					<xs:element name="cards" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="card" type="tns:card" minOccurs="0"
									maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
					<xs:element name="description" type="xs:string"
						minOccurs="0" />
					<xs:element name="handCards" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="card" type="tns:card" minOccurs="0"
									maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="sitInEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:seatedPlayer"
						minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="smallBlindEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
				<xs:attribute name="amount" type="xs:int" use="required" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="joinTableEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="player" type="tns:player" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="winnerEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="winners" type="tns:winner" nillable="true"
						minOccurs="0" maxOccurs="unbounded" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="winner">
		<xs:sequence>
			<xs:element name="gainedAmount" type="xs:int" />
			<xs:element name="player" type="tns:player" minOccurs="0" />
		</xs:sequence>
	</xs:complexType>

	<xs:complexType name="holdemTableTreeEventWrapper">
		<xs:complexContent>
			<xs:extension base="tns:lobbyTreeEvent">
				<xs:sequence>
					<xs:element name="event" type="tns:holdemTableTreeEvent"
						minOccurs="0" />
				</xs:sequence>
				<xs:attribute name="tableID" type="tns:tableId" />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="potsChangedEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableEvent">
				<xs:sequence>
					<xs:element name="pots" type="tns:pots" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="pots">
		<xs:restriction base="xs:int" />
	</xs:simpleType>

	<xs:complexType name="newPocketCardsEvent">
		<xs:complexContent>
			<xs:extension base="tns:holdemPlayerEvent">
				<xs:sequence>
					<xs:element name="pocketCards" minOccurs="0">
						<xs:complexType>
							<xs:sequence>
								<xs:element name="card" type="tns:card" minOccurs="0"
									maxOccurs="unbounded" />
							</xs:sequence>
						</xs:complexType>
					</xs:element>
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="holdemPlayerEvent" abstract="true">
		<xs:complexContent>
			<xs:extension base="tns:holdemTableTreeEvent">
				<xs:sequence />
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="actionPerformedEvent">
		<xs:complexContent>
			<xs:extension base="tns:actionEvent">
				<xs:sequence>
					<xs:element name="result" type="xs:anyType" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="actionEvent" abstract="true">
		<xs:sequence />
		<xs:attribute name="id" type="tns:eventId" />
	</xs:complexType>

	<xs:simpleType name="eventId">
		<xs:restriction base="xs:long" />
	</xs:simpleType>

	<xs:complexType name="illegalActionEvent">
		<xs:complexContent>
			<xs:extension base="tns:actionEvent">
				<xs:sequence>
					<xs:element name="message" type="xs:string" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:complexType name="remoteExceptionEvent">
		<xs:complexContent>
			<xs:extension base="tns:actionEvent">
				<xs:sequence>
					<xs:element name="message" type="xs:string" minOccurs="0" />
				</xs:sequence>
			</xs:extension>
		</xs:complexContent>
	</xs:complexType>

	<xs:simpleType name="rank">
		<xs:restriction base="xs:string">
			<xs:enumeration value="DEUCE" />
			<xs:enumeration value="THREE" />
			<xs:enumeration value="FOUR" />
			<xs:enumeration value="FIVE" />
			<xs:enumeration value="SIX" />
			<xs:enumeration value="SEVEN" />
			<xs:enumeration value="EIGHT" />
			<xs:enumeration value="NINE" />
			<xs:enumeration value="TEN" />
			<xs:enumeration value="JACK" />
			<xs:enumeration value="QUEEN" />
			<xs:enumeration value="KING" />
			<xs:enumeration value="ACE" />
		</xs:restriction>
	</xs:simpleType>

	<xs:simpleType name="suit">
		<xs:restriction base="xs:string">
			<xs:enumeration value="CLUBS" />
			<xs:enumeration value="DIAMONDS" />
			<xs:enumeration value="HEARTS" />
			<xs:enumeration value="SPADES" />
		</xs:restriction>
	</xs:simpleType>

	<xs:simpleType name="rounds">
		<xs:restriction base="xs:string">
			<xs:enumeration value="PREFLOP" />
			<xs:enumeration value="FLOP" />
			<xs:enumeration value="TURN" />
			<xs:enumeration value="FINAL" />
		</xs:restriction>
	</xs:simpleType>
</xs:schema>
```