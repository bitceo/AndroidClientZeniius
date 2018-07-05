
package com.atomic.android.enums;


/**
 * Created by Hung Hoang on 09/07/2017.
 */

public enum ItemType {LOAD(10), ITEM(11);
    private final int typeCode;

    ItemType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}