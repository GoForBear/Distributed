package Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import util.NIOConfig;

public class discardServer {
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final int serverPort;

    public discardServer(int serverPort){
        this.serverPort = serverPort;
    }

    public void runServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(serverPort);
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
//                    socketChannel.pipeline().addLast(new discardHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture closeCahnnel = channelFuture.channel().closeFuture();
            closeCahnnel.sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdown();
            bossGroup.shutdown();
        }
    }

    public static void main(String[] args) {
        int serverPort = NIOConfig.SOCKET_SERVER_PORT;
        new discardServer(serverPort).runServer();
    }
}
