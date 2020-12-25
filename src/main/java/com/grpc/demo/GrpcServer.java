package com.grpc.demo;

import cn.hutool.core.util.ObjectUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * grpc server
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/12/25 14:42
 **/
public class GrpcServer {
    private Server server;

    private void start() throws IOException {
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();
        System.out.println("Server started!!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown JVM!!");
            GrpcServer.this.stop();
        }));
    }

    private void stop() {
        if (ObjectUtil.isNotEmpty(this.server)) {
            this.server.shutdown();
        }
    }

    /**
     * 让服务启动后处于等待状态，不然服务已启动马上就停止了
     *
     * @throws InterruptedException e
     */
    private void awaitTermination() throws InterruptedException {
        if (ObjectUtil.isNotEmpty(this.server)) {
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        GrpcServer grpcServer = new GrpcServer();
        grpcServer.start();
        grpcServer.awaitTermination();
    }

}
