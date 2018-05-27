package com.ray.calculator;

public class Stack {
    protected ListNode top;
    protected Stack() {
        top = null;
    }

    protected void push(String newstr) // push to top
    {
        ListNode NewTop = new ListNode(newstr);
        if (top != null)  // if stack is empty
        {
            top.next = NewTop;
            NewTop.previous = top;
        }
        top = NewTop;
    }

    protected String pop()
    {
        if(top == null) // if empty, error
        {
            System.out.println("The stack is empty.");
            return null;
        }
        String result_str = top.key_val;
        if(top.previous == null) // if only element, stack becomes empty
        {
            top = null;
        }
        else {
            ListNode newTop = top.previous;
            newTop.next = null;
            top = newTop;

        }
        return result_str;  // return removed string
    }
}