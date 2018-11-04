package com.example;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//线程服务的实现，通过serversocket循环监听客户端的链接,并且把已经链接的客户端保存起来
public class Server extends Thread {
    public interface OnServiceListener {
        void onClientChanged(List<Client> clients);

        void onNewMessage(String message, Client client);
    }

    private OnServiceListener listener;

    public void setOnServiceListener(OnServiceListener listener) {
        this.listener = listener;
    }


    boolean started = false;//标记服务是否已经启动
    ServerSocket ss = null;//将serverSocket
    List<Client> clients = new ArrayList<Client>();//维护一个客户端的集合列表
//下面的就是server线程的不断的循环检测
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            ss = new ServerSocket(8888);
            started = true;
            System.out.println("server is started");
        } catch (BindException e) {
            System.out.println("port is not available....");
            System.out.println("please restart");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (started) {
                //然后是server类的while方法加入监听客户端的代码
                Socket s = ss.accept();//接受
                Client c = new Client(s, Server.this);
                System.out.println("a client connected!");
                new Thread(c).start();
                addClient(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//消息接受和Client退出以及新的Client 的到来我们都通过回调的方式通知服务端的界面ChatRoom类
//实现消息的发送和接受

    //修改一下Server类接收到消息的方法,然后新增一个发送消息的方法给Chatroom调用
    public synchronized void snedMessage(String msg) {
        for (Client client1 : clients) {
            client1.send(msg);
        }
    }

    public synchronized void newMessage(String msg, Client client) {
        if (listener != null) {
            listener.onNewMessage(msg, client);
            for (Client client1 : clients) {
                if (!client1.equals(client)) {
                    client1.send(client1.getSocket().getInetAddress() + "#" + msg);
                }
            }
        }
    }

    //介于多个线程会访问这个列表,并且ArrayList不是线程安全的,所以我们在Server类里面创建几个添加和删除的方法
    public synchronized void addClient(Client client) {
        clients.add(client);
        if (listener != null) {
            listener.onClientChanged(clients);
        }
    }


    public synchronized void removeClient(Client client) {
        clients.remove(client);
        if (listener != null) {
            listener.onClientChanged(clients);
        }
    }

}
