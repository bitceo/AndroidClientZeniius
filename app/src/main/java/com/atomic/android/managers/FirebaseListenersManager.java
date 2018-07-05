
package com.atomic.android.managers;
/**
 * Created by Hung Hoang on 09/07/2017.
 */
import android.content.Context;

import com.atomic.android.ApplicationHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class FirebaseListenersManager {
    Map<Context, List<ValueEventListener>> activeListeners = new HashMap<>();

    Map<Context, List<ChildEventListener>> activeChildListeners = new HashMap<>();

    void addListenerToMap(Context context, ValueEventListener valueEventListener) {
        if (activeListeners.containsKey(context)) {
            activeListeners.get(context).add(valueEventListener);
        } else {
            List<ValueEventListener> valueEventListeners = new ArrayList<>();
            valueEventListeners.add(valueEventListener);
            activeListeners.put(context, valueEventListeners);
        }
    }

    void addListenerToMap(Context context, ChildEventListener childEventListener) {
        if (activeChildListeners.containsKey(context)) {
            activeChildListeners.get(context).add(childEventListener);
        } else {
            List<ChildEventListener> childEventListeners = new ArrayList<>();
            childEventListeners.add(childEventListener);
            activeChildListeners.put(context, childEventListeners);
        }
    }

    public void closeListeners(Context context) {
        DatabaseHelper databaseHelper = ApplicationHelper.getDatabaseHelper();
        if (activeChildListeners.containsKey(context)) {
            for (ChildEventListener listener : activeChildListeners.get(context)) {
                databaseHelper.closeChildListener(listener);
            }
            activeListeners.remove(context);
        }

        if (activeListeners.containsKey(context)) {
            for (ValueEventListener listener : activeListeners.get(context)) {
                databaseHelper.closeListener(listener);
            }
            activeListeners.remove(context);
        }
    }
}
