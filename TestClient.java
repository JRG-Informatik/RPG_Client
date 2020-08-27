package com.uhrenclan;

import com.uhrenclan.RPG_Client.Client;
import com.uhrenclan.RPG_Client.Packet;
import com.uhrenclan.RPG_Client.Client.ClientEventCallback;
import com.uhrenclan.RPG_Client.Client.PacketHandler;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = new Client();
		client.setClientEventCallback(new Client.ClientEventCallback() {

			@Override
			public void OnConnectionToServer() {
				// TODO Auto-generated method stub
				System.out.println("Connected to server!");
			}

			@Override
			public void OnTCPDisconnect() {
				// TODO Auto-generated method stub
				System.out.println("Disconnected TCP from server!");
			}

			@Override
			public void OnDisconnect() {
				// TODO Auto-generated method stub
				System.out.println("Disconnected from server!");
			}

			@Override
			public void OnReceiveTCPPacket(Packet packet) {
				// TODO Auto-generated method stub
				System.out.println("Received TCP Packet "+packet.toString());
			}

			@Override
			public void OnDispatchTCPPacket(Packet packet) {
				// TODO Auto-generated method stub
				System.out.println("Dispatched TCP Packet "+packet.toString());
			}});
		client.addPacketHandler(1, new Client.PacketHandler() {
			@Override
			public void callback(Packet _packet) {
				System.out.println("Incoming String [number 1]: "+_packet.ReadString());
			}});
		try {
			client.Connect("127.0.0.1", 3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
