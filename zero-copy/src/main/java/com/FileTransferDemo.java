package com;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileTransferDemo {
    public static void main(String[] args) throws IOException {
        //文件太小，也没有跨网络，只能稍微体现出一点点零拷贝的优势。
        File sourceFile = new File("FileTransfer.txt");
        System.out.println(sourceFile.getAbsolutePath() + "; size = " + sourceFile.length());

        long startTime = System.currentTimeMillis();
        moveFileBySendFile(sourceFile, new File("FileTransferSendFile.txt"));
        long endTime = System.currentTimeMillis();
        System.out.println("sendFile文件拷贝耗时：" + (endTime - startTime));

        startTime = endTime;
        moveFileByMmap(sourceFile, new File("FileTransferMmap.txt"));
        endTime = System.currentTimeMillis();
        System.out.println("mmap文件拷贝耗时：" + (endTime - startTime));

//        startTime = endTime;
//        moveFileByStream(sourceFile, new File("FileTransferStream.txt"));
//        endTime = System.currentTimeMillis();
//        System.out.println("传统IO文件拷贝耗时：" + (endTime - startTime));
    }

    private static void moveFileByMmap(File sourceFile, File targetFile) {
        try {
            final FileInputStream sourceFis = new FileInputStream(sourceFile);
            final FileChannel sourceReadChannel = sourceFis.getChannel();

            MappedByteBuffer buffer = sourceReadChannel.map(FileChannel.MapMode.READ_ONLY, 0, sourceReadChannel.size());

            final FileOutputStream targetFos = new FileOutputStream(targetFile);
            final FileChannel targetWriteChannel = targetFos.getChannel();

            targetWriteChannel.write(buffer);

            sourceFis.close();
            targetFos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //NIO中transfer方式的零拷贝。这种拷贝方式不光拷贝硬盘文件，还可以用作底层硬件之间的拷贝实现。例如kafka中使用这种方式将消息从硬盘拷贝到网卡。
    private static void moveFileBySendFile(File sourceFile, File targetFile) {
        try {
            final FileInputStream sourceFis = new FileInputStream(sourceFile);
            final FileChannel sourceReadChannel = sourceFis.getChannel();

            final FileOutputStream targetFos = new FileOutputStream(targetFile);
            final FileChannel targetWriteChannel = targetFos.getChannel();
            sourceReadChannel.transferTo(0, sourceFile.length(), targetWriteChannel);
            sourceFis.close();
            targetFos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //传统数据流拷贝方式
    private static void moveFileByStream(File sourceFile, File targetFile) {
        try {
            BufferedReader sourceBr = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
            BufferedWriter targetBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));
            while (true) {
                final String line = sourceBr.readLine();
                if (null == line || "".equals(line)) {
                    break;
                }
                targetBw.write(line);
            }
            targetBw.flush();
            sourceBr.close();
            targetBw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
