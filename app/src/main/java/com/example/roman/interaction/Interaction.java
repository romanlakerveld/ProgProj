package com.example.roman.interaction;

import android.content.Context;

/**
 * Created by roman on 31/01/2018.
 */

public class Interaction {
    // variable to hold context
    private Context context;
    private String source;
    private String target;
    private String interaction;

    //save the context recieved via constructor in a local variable

    public Interaction(Context context){
        this.context=context;
    }


    String getSource() {
        return source;
    }

    void setSource(String source) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String common = databaseAccess.getCommon(source);
        databaseAccess.close();

        this.source = "<i>" + source + "</i>" + common;
    }

    String getTarget() {
        return target;
    }

    void setTarget(String target) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String common = databaseAccess.getCommon(target);
        databaseAccess.close();

        this.target = "<i>" + target + "</i>" + common;
    }

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }
}

