//package dictionary;

import java.io.*;
import java.util.*;

public class Dictionary {
    public static void main(String[] args)throws IOException {
        AVLTree tree = new AVLTree();
        
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        int cap = 0, digit=0, proCount=0;
        String word="", pro="",def="";
        String strLine="", buffer="";
        while (scanner.hasNext() && (strLine != null)){
            strLine = scanner.nextLine();
            if(!(isCaps(strLine)) && !(strLine.isEmpty()) && scanner.hasNext()){
                buffer = scanner.nextLine();
                if(!(buffer.isEmpty())){
                    strLine = strLine +"\n"+buffer;
                if(scanner.hasNext())
                    buffer = scanner.nextLine();}
                while(!(buffer.isEmpty())&& scanner.hasNext() && (!(isDefn(buffer)) || !(isDigit(buffer)))){
                    strLine = strLine +"\n"+buffer;
                    buffer = scanner.nextLine();}}
            if((isCaps(strLine))&&!(strLine.isEmpty())&&(isSpecial(strLine)))
                cap++; 
            if(cap == 1 && (isCaps(strLine)) && !(strLine.isEmpty()))
                word = strLine;
            if(cap==1&&proCount==0&& !(strLine.equals(word))&&!(strLine.isEmpty())&&!(isDefn(strLine))&&!(isDigit(strLine))){
                pro = strLine+"\n\n"; proCount =1;}
            if(cap == 1&&!(strLine.isEmpty()) && (isDefn(strLine) || isDigit(strLine))){
                if(isDigit(strLine) || digit ==1){
                    def = def + strLine+"\n\n";
                    digit =1;}
                else
                    def = strLine+"\n\n";}
            if(cap == 2){
                if(tree.search(word)){
                    tree.add(word,pro+"\n"+def);
                }
                else
                    tree.insert(word,pro+def);
                cap = 1;word =strLine;pro="";def=""; digit = 0; proCount=0;               
            }
        }
       if(tree.search(word)){
                    tree.add(word,"\n\n"+pro+def+"\n");
                }
                else
                    tree.insert(word,"\n\n"+pro+def+"\n");    
        Scanner input = new Scanner(System.in);
        String userIn ="";
        System.out.println(tree.root.height+1);
        while(!(userIn == null)){
            System.out.print("$ ");
            userIn = input.nextLine();
            if(userIn.equals("EXIT"))
                System.exit(0);
            if(userIn.contains("SEARCH")){
                String search = userIn.substring(7);
                if(tree.search(search))
                    System.out.print("\n"+tree.find(search));
                else
                    System.out.print(tree.find(search));
            }
            else
                System.out.print("Invalid command\n");
        }      
    }
    private static boolean isCaps(String str){return (str.equals(str.toUpperCase()));}
    
    private static boolean isDefn(String str){return str.contains("Defn");}
        
    private static boolean isDigit(String str){char charArray[] = str.toCharArray();return Character.isDigit(charArray[0]);}
    
    private static boolean isSpecial(String str){char charArray[] = str.toCharArray();return Character.isLetter(charArray[0]);}
}
class AVLTree {
    class Node{ 
        String key, proDef; 
        int height;
        Node left, right;
        
        public Node(String key, String proDef) {
            this.key = key;
            this.proDef = proDef;
            height = 0;
            left = right = null;
        }
    }
    Node root;
    
    public AVLTree(){
        root = null;
    }
        private int height(Node n){ //get height of tree
            return (n == null)? -1 : n.height;
        }
        private int max(int a, int b){ //get the max of 2 jauns
            return (a>b)? a : b;
        }
        private void updateHeight(Node n){
            n.height = max(height(n.left),height(n.right)) +1;
        }
        private Node rightRotate(Node y) {
            Node x = y.left;
            Node t = x.right;
            //rotate that sucker
            x.right = y;
            y.left = t;
            //update the heights
            updateHeight(x);
            updateHeight(y);
            //return new root
            return x;
        }
        private Node leftRotate(Node x) {
            Node y = x.right;
            Node t = y.left;
            //rotate that sucker
            y.left = x;
            x.right = t;
            //update the heights
            updateHeight(x);
            updateHeight(y);
            //return new root
            return y;
        }
        private int getBalance(Node n){
            return (n == null)? 0 : height(n.right)-height(n.left);
        }
        public void insert(String key, String proDef){
            root = insertRec(root, key,proDef);
        }
        private Node insertRec(Node node, String key, String proDef){
            //normal tree insert
            if(node == null)
                return new Node(key,proDef);
            if(key.compareTo(node.key) < 0)
                node.left = insertRec(node.left, key,proDef);
            else if (key.compareTo(node.key) > 0)
                node.right = insertRec(node.right, key,proDef);
            else
                return node;
            
            //get balance factor to see if it is unbalanced now
            int balance = getBalance(node);
            //try all 4 ways to balance
            //LeftLeft
            if (balance < -1 && key.compareTo(node.left.key) < 0)
                return rightRotate(node);
            //RightRight
            else if (balance > 1 && key.compareTo(node.right.key) > 0)
                return leftRotate(node);
            //LeftRight
            else if (balance < -1 && key.compareTo(node.left.key) > 0){
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            //RightLeft
            else if (balance > 1 && key.compareTo(node.right.key) < 0){
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            else{updateHeight(node); return node;}
        }
    String find(String key) {
        Node current = root;
        while (current != null) {
            if (current.key.equals(key)) {
                break;
            }
            current = current.key.compareTo(key) < 0 ? current.right : current.left;
        }
        if(current != null)
            return (current.proDef);
        else
            return ("WORD does not exist\n");
    }
    boolean search(String key) {
        Node current = root;
        while (current != null) {
            if (current.key.equals(key)) {
                break;
            }
            current = current.key.compareTo(key) < 0 ? current.right : current.left;
        }
        if(current != null)
            return current.key.equals(key);
        else
            return false;
    }
    void add(String key, String proDef) {
        Node current = root;
        while (current != null) {
            if (current.key.equals(key)) {
                break;
            }
            current = current.key.compareTo(key) < 0 ? current.right : current.left;
        }
        if (current != null)
            current.proDef = current.proDef + proDef;
    }
}
  
    
