package com.demo.java.basic.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;

/**
 * public class Example {
 * public Example() {
 * }
 * <p>
 * public static void main(String[] var0) {
 * System.out.println("Hello world!");
 * }
 * }
 */
public class AsmHelloWorld extends ClassLoader implements Opcodes {
    public static void main(String[] args) throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        // 创建Example类
        classWriter.visit(V1_6, ACC_PUBLIC, "Example", null, "java/lang/Object", null);

        // 生成默认的构造方法
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        // 生成构造方法的字节码指令
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();

        // 生成main方法 : public static void main(String args[]);
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[]{"java/io/IOException"});
        // 生成main方法中的字节码指令: System.out.println("hello world")
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        methodVisitor.visitLdcInsn("Hello world!");
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(2, 2);
        methodVisitor.visitEnd();

        classWriter.visitEnd();

        // 获取生成的class文件对应的二进制流
        byte[] bytes = classWriter.toByteArray();

        // 将二进制流写到本地磁盘上
        FileOutputStream outputStream = new FileOutputStream("Example.class"); //磁盘上生成Example.class文件
        outputStream.write(bytes);
        outputStream.close();

        // 直接将二进制流加载到内存中
        AsmHelloWorld loader = new AsmHelloWorld();
        Class<?> exampleClass = loader.defineClass("Example", bytes, 0, bytes.length);

        // 创建example实例
        Object example = exampleClass.newInstance();
        System.out.println(example);

        // 通过反射调用main方法
        exampleClass.getMethods()[0].invoke(null, new Object[]{null});

    }
}