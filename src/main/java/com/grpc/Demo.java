package com.grpc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.grpc.demo.HelloWorldProto;

import java.util.List;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/24 14:51
 **/
public class Demo {
    public static void main(String[] args) {
        HelloWorldProto.Student.Builder builder = HelloWorldProto.Student.newBuilder();
        builder.setId(1);
        builder.setName("张三");
        builder.setEmail("123@qq.com");
        builder.setSex(HelloWorldProto.Student.Sex.MAN);
        // 手机号
        HelloWorldProto.Student.PhoneNumber.Builder phoneBuilder = HelloWorldProto.Student.PhoneNumber.newBuilder();
        phoneBuilder.setNumber("18208201111");
        phoneBuilder.setType(HelloWorldProto.Student.PhoneType.MOBILE);
        HelloWorldProto.Student.PhoneNumber phoneNumber = phoneBuilder.build();
        builder.addPhone(phoneNumber);
        // 座机
        HelloWorldProto.Student.PhoneNumber.Builder phoneBuilder1 = HelloWorldProto.Student.PhoneNumber.newBuilder();
        phoneBuilder1.setNumber("18208201112");
        phoneBuilder1.setType(HelloWorldProto.Student.PhoneType.HOME);
        HelloWorldProto.Student.PhoneNumber phoneNumber1 = phoneBuilder1.build();
        builder.addPhone(phoneNumber1);

        HelloWorldProto.Student stu = builder.build();
        System.out.println("protobuf数据大小: " + stu.toByteString().size());
        //再将封装有数据的对象实例，转换为字节数组，用于数据传输、存储等
        byte[] stuByte = stu.toByteArray();
        HelloWorldProto.Student student = null;

        try {
            student = HelloWorldProto.Student.parseFrom(stuByte);
            //这里得到了Student实例了，就可以根据需要来操作里面的数据了
            System.out.println("学生ID:" + student.getId());
            System.out.println("姓名：" + student.getName());
            System.out.println("性别：" + (student.getSex().getNumber() == 0 ? "男" : "女"));
            System.out.println("邮箱：" + student.getEmail());
            //遍历phoneNumber字段
            List<HelloWorldProto.Student.PhoneNumber> phList = student.getPhoneList();
            for (HelloWorldProto.Student.PhoneNumber p : phList) {
                System.out.println(p.getType() + "电话:" + p.getNumber());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        /*如何快速的进行json格式化*/
        String jsonObject = "";
        try {
            jsonObject = JsonFormat.printer().print(student);
        } catch (InvalidProtocolBufferException e) {
            e.getMessage();
        }
        System.out.println(jsonObject);
        System.out.println("json数据大小: " + jsonObject.getBytes().length);
    }
}
