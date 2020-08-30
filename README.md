# RPG_Client
A simple TCP and UDP Client
## Visit the [**`Docs`**](https://github.com/JRG-Informatik/RPG_Client/wiki)!

# Basic setup
## `Initialize` a new Server:
```java
Server server = new Server();

String serverIp = "127.0.0.1";
int port = 3000;

try {

  server.Listen(serverIp, port);
  System.out.println(String.format("The server is listening [%s:%d]", serverIp, port));
  
} catch(Exception e) {

  System.out.println(String.format("Failed to start server [%s:%d]", serverIp, port));
  
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
  
  packet.Write("Hello from Server :D");
  server.SendTCPDataToAll(packet);

} catch(Exception e) {}
```
## Add `callbacks` for `TCP Packets` from the `Client`
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

## Add `callbacks` for `Server Events`
Define an `ServerEventCallback` for `state` callbacks
```java
Server.ServerEventCallback serverEventCallback = new Server.ServerEventCallback() {

  @Override
  public void OnClientConnection(Client client) {
    System.out.println("Bonded with client "+client.ID);
  }

  @Override
  public void OnClientConnectionAuthenticated(Client client) {
    System.out.println("Authenticated client "+client.ID);
  }
  
  @Override
  public void OnClientDisconnect(Client client) {
    System.out.println("Client "+client.ID+" disconnected");
  }

  @Override
  public void OnClientTCPDisconnect(TCPClient tcpClient) {
    System.out.println("TCPClient "+tcpClient.client.ID+" disconnected");
  }

  @Override
  public void OnReceiveTCPPacket(TCPClient tcpClient, Packet packet) {
    System.out.println("Client "+tcpClient.client.ID+" sent packet: "+packet.toString());
  }

  @Override
  public void OnDispatchTCPPacket(TCPClient tcpClient, Packet packet) {
    System.out.println("Client "+tcpClient.client.ID+" dispatched packet: "+packet.toString());
  }
};
```
Apply `ServerEventCallback` to `Server`
```java
server.setServerEventCallback(serverEventCallback);
```
