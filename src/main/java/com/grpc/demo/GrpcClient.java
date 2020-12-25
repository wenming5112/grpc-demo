package com.grpc.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/25 14:49
 **/
public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                .usePlaintext().build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);

        // 调用服务端方法查询用户名
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("李四").build());
        System.out.println(myResponse.getRealName());

        // 调用服务端方法 根据年龄查询学生
        Iterator<StudentResponse> iterator = blockingStub.getStudentByAge(StudentRequest.newBuilder().setAge(20).build());
        while (iterator.hasNext()) {
            StudentResponse studentResponse = iterator.next();
            System.out.println(studentResponse.getName() + "：" + studentResponse.getName() + "：" + studentResponse.getCity());
        }

        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList value) {
                value.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName() + "：" + studentResponse.getAge() + "：" + studentResponse.getCity());
                });
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("completed!");
            }
        };

        StreamObserver<StudentRequest> studentRequestStreamObserver = StudentServiceGrpc.newStub(managedChannel).getStudentsWrapperByAges(studentResponseListStreamObserver);
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(10).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(30).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(40).build());
        studentRequestStreamObserver.onCompleted();

        try {
            //因为grpc调用参数为stream的时候，都是采取异步处理，客户端程序不会等待结果响应
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
