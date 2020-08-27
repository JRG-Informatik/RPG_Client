package com.uhrenclan.RPG_Client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private boolean connected = false;
	private ThreadManager threadManager = new ThreadManager();
	
	public String ip;
    public int port;
    public int ID;
    public TCPClient tcp;
	
	private ClientEventCallback clientEventCallback;
	public static interface ClientEventCallback{
		void OnConnectionToServer();
		void OnTCPDisconnect();
		void OnDisconnect();
		void OnReceiveTCPPacket(Packet packet);
		void OnDispatchTCPPacket(Packet packet);
	}
	public void setClientEventCallback(ClientEventCallback _clientEventCallback) {
		clientEventCallback = _clientEventCallback;
	}
	public ClientEventCallback getClientEventCallback() {
		return clientEventCallback;
	}
	
	public static interface PacketHandler{
		void callback(Packet _packet);
	}
	private Map<Integer, PacketHandler> packetHandlers = new HashMap<Integer, PacketHandler>(){{
		//Welcome callback from Server
		put(0, new PacketHandler() {
			@Override
			public void callback(Packet _packet) {
				//Read ID from Server packet
		        ID = _packet.ReadInt();
		        //response to Welcome to server
		        try(Packet __packet = new Packet(0)){
		        	__packet.Write(ID);
		        	SendTCPData(__packet);
		        }catch (Exception e) {}
		        //connect UDP
			}});
	}};
	public PacketHandler getPacketHandler(int id) {
		return packetHandlers.get(id);
	}
	public void addPacketHandler(int id, PacketHandler packetHandler) {
		if(id<=0) return;
		packetHandlers.put(id, packetHandler);
	}
	public void addPacketHandlers(Map<Integer, PacketHandler> _packetHandlers) {
		for(Map.Entry<Integer, PacketHandler> pair: _packetHandlers.entrySet()) {
			addPacketHandler(pair.getKey(), pair.getValue());
		}
	}
	public void removePacketHandler(int id) {
		if(id<=0)return;
		packetHandlers.remove(id);
	}
    
    public Client() {}
    
    public void Connect(String _ip, int _port) throws Exception {
    	if(_ip==null||_ip.isEmpty()) ip = "127.0.0.1";
    	else ip = _ip;
    	port = _port;
    	
    	Socket socket = new Socket(ip, port);
    	tcp = new TCPClient(this);
		tcp.Connect(socket);
		
		connected = true;
		new Thread() {
			@Override
			public void run() {
				while(connected) {
					threadManager.UpdateMain();
				}
			}
		}.start();
    }
    
    private void SendTCPData(Packet _packet){
        _packet.WriteLength();
        tcp.SendData(_packet);
    }
    
	public void ExecuteOnMainThread(Runnable runnable) {
		threadManager.ExecuteOnMainThread(runnable);
	}
}
