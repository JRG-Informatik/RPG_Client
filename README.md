# RPG_Client
A simple TCP and UDP Client
## Visit the [**`Docs`**](https://github.com/JRG-Informatik/RPG_Client/wiki)!

# Basic setup
## `Initialize` a new Client:
```java
Client client = new Client();

String serverIp = "127.0.0.1";
int port = 3000;

try {

  client.Connect(serverIp, port);
  System.out.println(String.format("Connecting... [%s:%d]", serverIp, port));
  
} catch(Exception e) {

  System.out.println(String.format("Failed to connect to server [%s:%d]", serverIp, port));
  
}
```
## Send `TCP` Packet
Define an enum to manage all outgoing packet types
```java
public enum PacketSendIDs{
  ServerAuthentication, //ID 0 is used for basic server authentication (DO NOT USE)!
  dataTypeOne
}
```
```java
try(Packet packet = new Packet(PacketSendIDs.dataTypeOne.ordinal())){
  
  packet.Write("Hello from Client :D");
  client.SendTCPData(packet);

} catch(Exception e) {}
```
## Add `callbacks` for `TCP Packets` from the `Server`
Define an enum to manage all incoming packet types
```java
public enum PacketTypeIDs{
  ServerAuthentication, //ID 0 is used for basic server authentication (DO NOT USE)!
  dataTypeOneReceived
}
```
```java
Client.PacketHandler onDataTypeOneReceived = new Client.PacketHandler() {

  @Override
  public void callback(Packet _packet) {
    //process the packet data
  }
}

client.addPacketHandler(PacketTypeIDs.dataTypeOneReceived.ordinal(), onDataTypeOneReceived);
```

## Add `callbacks` for `Client Events`
Define an `ClientEventCallback` for `state` callbacks
```java
Client.ClientEventCallback clientEventCallback = new Client.ClientEventCallback() {
  @Override
	public void OnConnectionToServer() {
		System.out.println("Connected to server!");
	}

	@Override
	public void OnTCPDisconnect() {
		System.out.println("Disconnected TCP from server!");
	}

	@Override
	public void OnDisconnect() {
		System.out.println("Disconnected from server!");
	}

	@Override
	public void OnReceiveTCPPacket(Packet packet) {
		System.out.println("Received TCP Packet "+packet.toString());
	}

	@Override
	public void OnDispatchTCPPacket(Packet packet) {
		System.out.println("Dispatched TCP Packet "+packet.toString());
	}
});
```
Apply `ClientEventCallback` to `Client`
```java
client.setClientEventCallback(clientEventCallback);
```
