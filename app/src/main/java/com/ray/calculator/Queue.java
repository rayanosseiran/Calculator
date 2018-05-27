package com.ray.calculator;

public class Queue {
    protected ListNode front;
    protected ListNode back;


    protected Queue()
    {
        front = null;
        back = null;
    }

    protected void Enqueue(String newStr) { // insert at back of queue
        ListNode myNewBackNode = new ListNode(newStr);

        if(back == null) { // if queue is empty
            back = myNewBackNode;
            front = myNewBackNode;
        }
        else
        {
            myNewBackNode.next = back;
            back.previous = myNewBackNode;
            back = myNewBackNode;
        }
    }

    protected String Dequeue() { // remove from front of queue

        if(front == null) {
            System.out.println("Error; the queue is empty");
            return null;

        }
        String result_str = front.key_val;

        if(front.previous == null)
        {
            front = null;
            back = null;
        }
        else {
            ListNode pre_front = front.previous;
            pre_front.next = null;
            front = pre_front;
        }
        return result_str;
    }

}