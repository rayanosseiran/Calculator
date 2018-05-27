package com.ray.calculator;


public class ListNode {

    protected String key_val;
    protected ListNode next;
    protected ListNode previous;

    protected ListNode(String input_str) {
        key_val = input_str;
        next = null;
        previous = null;
    }
}

