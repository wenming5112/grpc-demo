package com.grpc.demo;

import io.grpc.stub.StreamObserver;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/25 14:40
 **/
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    /**
     * 根据姓名查询学生
     *
     * @param request          request
     * @param responseObserver responseObserver
     */
    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接收到客户端信息：" + request.getUsername());

        //将返回结果构造完，并且返回给客户端
        responseObserver.onNext(MyResponse.newBuilder().setRealName("张三").build());
        //告诉客户端方法执行完毕
        responseObserver.onCompleted();
    }

    /**
     * 根据年龄查询学生
     *
     * @param request          request
     * @param responseObserver responseObserver
     */
    @Override
    public void getStudentByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息：" + request.getAge());

        responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(50).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(40).setCity("上海").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(30).setCity("深圳").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("赵六").setAge(20).setCity("重庆").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest value) {
                System.out.println("onNext" + value.getAge());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                StudentResponse studentResponse1 = StudentResponse.newBuilder().setName("张三").setAge(20).setCity("深圳").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("李四").setAge(22).setCity("重庆").build();
                StudentResponseList studentResponseList = StudentResponseList.newBuilder()
                        .addStudentResponse(studentResponse1).addStudentResponse(studentResponse2).build();
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();
            }
        };
    }
}
