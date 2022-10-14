package com.example.consensus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class Universal {
    private Node[] head; // 头数组
    private Node tail; // 表尾

    public Universal(int n) {
        tail = new Node(null); // 表尾是一个哨兵结点
        tail.seq = 1; // 序号为 1
        head = new Node[n];
        for (int j = 0; j < n; j++) {
            head[j] = tail; // 初始化头结点
        }
    }

    public static Node max(Node[] array) {
        Node max = array[0];
        for (int i = 1; i < array.length; i++) // 遍历数组
            if (max.seq < array[i].seq) // 比较 seq 域
                max = array[i];
        return max; // 返回具有最大序列号的结点
    }

    public Response apply(Invocation invoc, int threadID) { // 将调用作为输入，并返回适当的响应
        // int i = (int) Thread.currentThread().getId(); // 线程 id
        // System.out.println(threadID);
        Node prefer = new Node(invoc); // 方法调用
        while (prefer.seq == 0) { // 尝试加入日志
            // 选择当前序号最大的结点（日志头）进行竞争
            Node before = max(head);
            Node after = before.decideNext.decide(prefer, threadID); // 竞争获胜的结点
            before.next = after; // 将获胜者的结点加入链表
            after.seq = before.seq + 1; // 修改获胜者结点的序号
            head[threadID] = after; // 参与竞争的线程更新 head 数组
        }
        // 通过将日志中的方法调用依次顺序应用到私有副本上来计算响应
        SeqObject MyObject = new SeqObject();
        Node current = tail.next; // 从初始状态开始重放
        while (current != prefer) {
            MyObject.apply(current.invoc); // 当前结点的方法
            current = current.next;
        }
        return MyObject.apply(current.invoc); // 返回结果
    }

}

class RMWConsensus<T> extends ConsensusProtocol<T> {
    public RMWConsensus(Class<T> componentType, int n) {
        super(componentType, n);
        // TODO Auto-generated constructor stub
    }

    private AtomicInteger r = new AtomicInteger(-1); // 初始化-1
    // T decide(T value, int threadID);

    public T decide(T value, int threadID) {
        propose(value, threadID);
        // int threadID = (int) Thread.currentThread().getId();
        r.compareAndSet(-1, threadID);
        return proposed[r.get()]; // 决定胜利者的值
    }
}

class Node {
    // 类对象的标准接口，其对象是完全有序的
    public Invocation invoc;
    public Consensus<Node> decideNext; // 决定下一个结点
    public Node next; // 指向下一个结点的指针
    public int seq; // 序列号

    public Node(Invocation invoc) { // 创建新结点
        this.invoc = invoc;
        decideNext = new RMWConsensus<Node>(Node.class, 10);
        next = null;
        seq = 0;
    }

}

class Counter {
    public static int counter = 0;
    public static final int TARGET = 1000;
    public static Universal universal = new Universal(1);

    public static Invocation getInvocation(int value) {
        Invocation invoc = null;
        try {
            invoc = new Invocation(Counter.class.getMethod("compareAndAdd", int.class),
                    new Object[] { new Counter(), value });
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return invoc;
    }

    public static int compareAndAdd(int excepted) {
        if (counter == excepted) {
            counter++;
        }
        return counter;
    }

}

class SeqObject {
    public Response apply(Invocation invoc) {
        // 在s对象上调用该方法并获取结果:
        Response response = null;
        try {
            response = new Response(invoc.method.invoke(invoc.args[0], invoc.args[1]));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}

// Invocation 对象
class Invocation {
    // public String method; // 方法名

    Method method;
    public Object[] args; // 参数

    public Invocation(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }
}

// Response 对象，包含调用结果或返回值。
class Response {
    public Object value; // 响应值

    public Response(Object value) {
        this.value = value;
    }
}
