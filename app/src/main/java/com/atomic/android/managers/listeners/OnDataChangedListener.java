
package com.atomic.android.managers.listeners;

/**
 * Created by Hung Hoang on 09/07/2017.
 */
import java.util.List;

/**
 * Created by Kristina on 10/28/16.
 */

public interface OnDataChangedListener<T> {

    void onListChanged(List<T> list);
}
