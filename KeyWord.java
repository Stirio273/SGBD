package bddrelationnel;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;

public class KeyWord {
    String syntax;
    int position;
    KeyWord next;
    KeyWord previous;

    public String getSyntax() {
        return this.syntax;
    }

    public void setSyntax(String s) {
        this.syntax = s;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public KeyWord getNext() {
        return next;
    }

    public void setNext(KeyWord next) {
        this.next = next;
    }

    public KeyWord getPrevious() {
        return previous;
    }

    public void setPrevious(KeyWord previous) {
        this.previous = previous;
    }

    public KeyWord() {
    }

    public KeyWord(String syntax) {
        this.syntax = syntax;
    }

    public Object changeType(String text, Class<?> c) {
        switch (c.getSimpleName()) {
            case "Integer":
                return Integer.valueOf(text);
            default:
                return text;
        }
    }
}