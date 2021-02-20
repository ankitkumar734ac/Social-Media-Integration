package com.ankit.griplogin;

public class Bean {
    private static Bean bean;
    private Bean(){}
    public static Bean getBean(){
        if(bean == null){
            bean=new Bean();
        }
        return bean;
    }


    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
