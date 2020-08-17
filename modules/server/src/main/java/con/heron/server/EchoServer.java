package con.heron.server;

import java.net.InetSocketAddress;

import con.heron.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by heron.lee 17/08/2020
 */
public class EchoServer {
    private final int port = 10998;

    public static void main(String[] args) throws Exception {
        EchoServer server = new EchoServer();
        server.start();
    }

    public void start () throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                     .channel(NioServerSocketChannel.class)
                     .localAddress(new InetSocketAddress(port))
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(handler);
                         }
                     });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
            System.out.println("server started");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
