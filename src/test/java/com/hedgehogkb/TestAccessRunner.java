package com.hedgehogkb;

public class TestAccessRunner {
    public static void main(String[] args) {
        TestAccess t1 = new TestAccess("Marnie");
        TestAccess t2 = new TestAccess("Reid");

        t1.otherAccess(t2);

        //System.out.println(t1.name);
        //t1.testAccess();
    }
}
