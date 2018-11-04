package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//服务端的界面
public class Chatroom extends JFrame implements Server.OnServiceListener, ActionListener {
    private JLabel clientLabel;//客户列表控件
    private JList clientList;//客户列表
    private JLabel historyLabel;//聊天记录标签
    private JScrollPane jScrollPane;//嵌套在聊天记录外面的容器，让里面的容器可以滚动
    private JTextArea historyContentLabel;//聊天记录里面的控件
    private JTextField messageText;//服务端输入框
    private JButton sendButton;//发送按钮
    private Server server;
    private StringBuffer buffers;


    public Chatroom() {
        buffers = new StringBuffer();//new一个字符串缓存
        clientLabel = new JLabel("客户列表");
        clientLabel.setBounds(0, 0, 100, 30);//设置客户列表标签的位置
        clientList = new JList<>();//把客户列表new出来
        clientList.setBounds(0, 30, 100, 270);//设置客户列表的位置
        historyLabel = new JLabel("聊天记录");
        historyLabel.setBounds(100, 0, 500, 30);//设置聊天记录的位置

        historyContentLabel = new JTextArea();//new一个聊天记录里面的控件
        jScrollPane=new JScrollPane(historyContentLabel);//new一个聊天记录外的那个容器
        jScrollPane.setBounds(100, 30, 500, 230);//设置聊天记录的容器位置
        //分别设置水平和垂直滚动条自动出现
        jScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        messageText = new JTextField();//new出服务端输入框
        messageText.setBounds(100, 270, 440, 30);//设置服务端输入框的位置
        sendButton = new JButton("发送");//new出发送按钮
        sendButton.setBounds(540, 270, 60, 30);//设置发送按钮的位置


        sendButton.addActionListener(this);//为Button设置事件回调
        this.setLayout(null);//设置为null 的意思就是不需要任何布局方式,我们利用位置来自己定位

        add(clientLabel);
        add(clientList);
        add(historyLabel);
        add(jScrollPane);
        add(messageText);
        add(sendButton);

        //设置窗体
        this.setTitle("客服中心");//窗体标签
        this.setSize(600, 330);//窗体大小
        this.setLocationRelativeTo(null);//居中显示
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出和关闭JFrame
        this.setVisible(true);//显示窗体
        this.setResizable(false);

        server = new Server();
        server.setOnServiceListener(this);
        server.start();
    }

    //回调的注册

    //更新增加聊天用户列表
    @Override
    public void onClientChanged(List<Client> clients) {
        // TODO Auto-generated method stub
        clientList.setListData(clients.toArray());
    }

    //更新增加的信息
    @Override
    public void onNewMessage(String message, Client client) {
        // TODO Auto-generated method stub
        buffers.append(client.getSocket().getInetAddress().toString()+"\n");
        buffers.append(message+"\n");
        historyContentLabel.setText(buffers.toString());
    }

    //Chatroom调用Server类发送消息的方法
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == sendButton) {
            server.snedMessage("服务器#"+messageText.getText().toString());
            buffers.append("服务器"+"\n");
            buffers.append(messageText.getText().toString()+"\n");
            historyContentLabel.setText(buffers.toString());
        }
    }


}
