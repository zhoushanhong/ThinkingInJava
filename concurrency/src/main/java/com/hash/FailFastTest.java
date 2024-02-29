package com.hash;

import java.util.ArrayList;
import java.util.Iterator;

public class FailFastTest {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Michael", 31));
        users.add(new User("Dendy", 32));

        printUsers(users);

        // fast fail example
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals("Michael")) {
                users.add(new User("Park", 30)); // 能触发 ConcurrentModifyException
                users.remove(0); // 不能触发 ConcurrentModifyException
            }
        }

        // correct way
        users.removeIf(user -> user.name.equals("Michael"));

        printUsers(users);
    }

    private static void printUsers(ArrayList<User> users) {
        for (User user : users) {
            System.out.println(user);
        }

        System.out.println();
    }

    private static class User{
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
