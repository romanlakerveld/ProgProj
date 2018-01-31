package com.example.roman.interaction;

import android.content.Context;

/**
 * Interaction class containing all information for an interaction.
 */
public class Interaction {
    private Context context;
    private String source;
    private String target;
    private String interaction;

    /**
     * Save the context received via constructor in a local variable
     */
    public Interaction(Context context){
        this.context=context;
    }

    /**
     *
     * @return source of interaction
     */
    private String getSource() {
        return source;
    }

    /**
     * Set source of interaction, additionally this method gets the common name as well, placing it
     * after the latin.
     * @param source source of interaction to set
     */
    void setSource(String source) {
        // get common name from the database
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String common = databaseAccess.getCommon(source);
        databaseAccess.close();

        // set source and concatenate with common name
        this.source = "<i>" + source + "</i>" + common;
    }

    /**
     *
     * @return target of interaction
     */
    private String getTarget() {
        return target;
    }

    /**
     *
     * @param target target of interaction to set
     */
    void setTarget(String target) {
        // get common name from database
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String common = databaseAccess.getCommon(target);
        databaseAccess.close();

        // set target and concatenate with common name
        this.target = "<i>" + target + "</i>" + common;
    }

    /**
     *
     * @return type of interaction
     */
    public String getInteraction() {
        return interaction;
    }

    /**
     *
     * @param interaction interaction type to set
     */
    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }
}

