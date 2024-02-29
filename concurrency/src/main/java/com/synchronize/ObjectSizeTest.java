package com.synchronize;

import org.openjdk.jol.info.ClassLayout;

public class ObjectSizeTest {
    public static void main(String[] args) {
        // jvm延迟偏向
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        Model model = new Model();
        System.out.println(ClassLayout.parseInstance(model).toPrintable());
    }

    private static class Model {
        private long p;
    }
}
