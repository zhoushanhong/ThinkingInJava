package com.atom;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    public static void main( String[] args ) {
        User user1 = new User("张三");
        User user2 = new User("李四");
        User user3 = new User("王五");

        //初始化为 user1
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(user1);
        System.out.println(atomicReference.get());

        //把 user2 赋给 atomicReference
        atomicReference.compareAndSet(user1, user2);
        System.out.println(atomicReference.get());

        //把 user3 赋给 atomicReference
        atomicReference.compareAndSet(user1, user3);
        System.out.println(atomicReference.get());

        atomicReference.compareAndSet(user2, user3);
        System.out.println(atomicReference.get());

    }

    private static class User {
        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
