package com.hills.mcs_02.account;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

//序列化通用方法
public class SerializableUtil {
    public static <E> String list2String(List<E> list) throws IOException{
        //实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
        //baos改为byteOutput
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        //然后将得到的字符数据装载到ObjectOutputStream
        //oos改为objectOutput
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        //writeObject 方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
        objectOutput.writeObject(list);
        //最后，用Base64.encode将字节文件转换成Base64编码，并以String形式保存
        String listString = new String(Base64.encode(byteOutput.toByteArray(),Base64.DEFAULT));
        //关闭oos
        objectOutput.close();
        return listString;
    }

    public static  String obj2Str(Object obj)throws IOException
    {
        if(obj == null) {
            return "";
        }
        //实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        //然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        //writeObject 方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
        objectOutput.writeObject(obj);
        //最后，用Base64.encode将字节文件转换成Base64编码，并以String形式保存
        String listString = new String(Base64.encode(byteOutput.toByteArray(),Base64.DEFAULT));
        //关闭oos
        objectOutput.close();
        return listString;
    }

    //将序列化的数据还原成Object
    public static Object str2Obj(String str) throws StreamCorruptedException,IOException{
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        //bais---byteInput
        ByteArrayInputStream byteInput = new ByteArrayInputStream(mByte);
        //ois---objectInput
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);

        try {
            return objectInput.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    //将序列化的数据还原成list
    public static <E> List<E> string2List(String str) throws StreamCorruptedException, IOException {
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream byteInput = new ByteArrayInputStream(mByte);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        List<E> stringList = null;
        try {
            stringList = (List<E>) objectInput.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringList;
    }

}
