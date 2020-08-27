package com.uhrenclan.RPG_Client;

public class Main {

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
				
			}

			@Override
			public void OnDisconnect() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnReceiveTCPPacket(Packet packet) {
				// TODO Auto-generated method stub
				System.out.println("Received TCP Packet "+packet.ToArray());
			}

			@Override
			public void OnDispatchTCPPacket(Packet packet) {
				// TODO Auto-generated method stub
				System.out.println("Dispatched TCP Packet "+packet.ToArray());
			}});
		try {
			client.Connect("", 80);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
