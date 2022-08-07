package dev.heinzl.simplessoproxy.utils;

import java.util.Deque;
import java.util.LinkedList;

public class Algorithms {

    public static boolean containsBalancedBrackets(String str) {

        Deque<Character> deque = new LinkedList<>();
        for (char ch : str.toCharArray()) {
            if (ch == '{' || ch == '[' || ch == '(') {
                deque.addFirst(ch);
            } else {
                if (!deque.isEmpty()
                        && ((deque.peekFirst() == '{' && ch == '}')
                                || (deque.peekFirst() == '[' && ch == ']')
                                || (deque.peekFirst() == '(' && ch == ')'))) {
                    deque.removeFirst();
                }
            }
        }
        return deque.isEmpty();
    }
}
