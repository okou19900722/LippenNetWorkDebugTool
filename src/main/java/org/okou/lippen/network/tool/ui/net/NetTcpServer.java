package org.okou.lippen.network.tool.ui.net;

import java.net.BindException;
import java.util.List;

import javax.swing.JOptionPane;

import org.okou.lippen.network.tool.listener.MessageReceivedListener;
import org.okou.lippen.network.tool.model.DataManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NetTcpServer extends AbstractNetTcp {
	private ServerBootstrap server;
	public NetTcpServer(DataManager data, MessageReceivedListener listener){
		super(data, listener);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		server.group(bossGroup, workGroup);
		server.channel(NioServerSocketChannel.class);
		server.childHandler(initializer);
	}
	public boolean start(String ip, int port){
		try {
			ChannelFuture f = server.bind(ip, port).sync();
			channel = f.channel();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "�������쳣", "�������쳣", JOptionPane.OK_OPTION);
			return false;
		} catch (Exception e) {
			if(e instanceof BindException) {
				JOptionPane.showMessageDialog(null, "�˿�[" + port + "]�Ѿ���ռ��", "�����쳣", JOptionPane.OK_OPTION);
			}
			return false;
		} 
		return true;
	}
	@Override
	public void sendMsg(String text) {
		List<Channel> channels = data.getConnections();
		byte[] bytes = msg2Bytes(text);
		if(bytes != null) {
			for (Channel c : channels) {
				c.writeAndFlush(bytes);
			}
		}
		
	}
}