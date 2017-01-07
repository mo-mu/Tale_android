package com.momu.tale.database;

/**
 * Created by songm on 2017-01-07.
 */

public class Questions {
    String q;
    int id;
    String created_at, updated_at;

    public int getId() {
        return id;
    }

    public String getQ() {
        return q;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Questions(){}

    public Questions(int id, String q, String created_at, String updated_at){
        this.id= id;
        this.q = q;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
