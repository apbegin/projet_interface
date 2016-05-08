package com.ift2905.chat;

public class UserList {

    private int len;
    private int capacity;
    private String[] data;

    public UserList(int capacity){
        this.len=0;
        this.capacity=capacity;
        this.data = new String[capacity];
    }

    public int getLength(){ return this.len; }

    public String getUser(int p){
        if (p<this.len && p>=0) {
            return data[p];
        } else {
            return null;
        }
    }

    public void deleteUserAtPosition(int p){
        if (p<this.len && p>=0){
            for(int i=p; i<this.len-1; i++){
                this.data[i]=this.data[i+1];
            }
            this.data[this.len-1]=null;
            this.len-=1;
        }
    }

    public int getPosition(String user){
        //Sert aussi à  savoir si le user est dans la liste: if pos>=0
        if(this.len==0){
            return -1;
        } else {
            for (int i = 0; i < this.len; i++) {
                if (user.equals(data[i])) return i;
            }
        }
        return -1;
    }

    public void deleteUser(String user){
        int pos = this.getPosition(user);
        if (pos>=0) this.deleteUserAtPosition(pos);
    }

    public void addUser(String user){
        if(this.len<this.capacity){
            if(this.getPosition(user)<0){
                data[len]=user;
                this.len++;
            }
        }
    }

    public void printList(){
        for(int i=0; i<this.len-1; i++){
            System.out.print(this.data[i]+", ");
        }
        System.out.println(this.data[this.len-1]);
    }

}

