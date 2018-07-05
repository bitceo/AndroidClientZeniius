
package com.atomic.android.model;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

@IgnoreExtraProperties
public class Notification implements Serializable {

    private String actionType;
    private String content;
    private HashMap<String,Long> listSubjectId;
    private String objectId;
    private String parentObjectId;
    private String status;
    private long timestamp;

    private String id;

    public String getActionType() {
        return actionType;
    }

    public String getContent() {
        return content;
    }

    public HashMap<String,Long> getListSubjectId() {
        return listSubjectId;
    }



    public String getObjectId() {
        return objectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getParentObjectId() {
        return parentObjectId;
    }

}
