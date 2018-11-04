package com.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

//客户端
public class Client implements Runnable{

	private Socket s;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean bConnected = false;
	private Server server;//server实例

	public Socket getSocket() {
		return s;
	}

	public Client(Socket s, Server ser) {
		this.s=s;
		this.server = ser;
		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			bConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//把Server传了进来,在Client接收到消息和异常退出的时候我们通过Server实例来调用对应的Server里面的方法
	public void send(String str) {//发送消息
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			server.removeClient(this);
		
		}
	}

	public void run() {//接受消息
		try {
			while (bConnected) {
				String str = dis.readUTF();
				server.newMessage(str,this);
			}
		} catch (EOFException e) {//异常退出
			System.out.println("Client closed!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
				if (s != null) {
					server.removeClient(this);
					s.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Client client = (Client) o;
		return s.equals(client.s);
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return s.toString();
	}
}
