package com.hedgehogkb;

public class TestAccess {
    private String name;

    public TestAccess(String name) {
        this.name = name;
    }

    private void testAccess() {
        System.out.println("printed: " + name);
    }

    public void otherAccess(TestAccess o) {
        System.out.println("test has access to variable: " + o.name);
        //next we test for methods
        o.testAccess();
    }

}
