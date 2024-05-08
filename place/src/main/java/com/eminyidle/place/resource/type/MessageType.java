package com.eminyidle.place.resource.type;

public enum MessageType {
    ADD_PLACE(1),
    DELETE_PLACE(2),
    UPDATE_PLACE_DATE(3),
    ADD_ACTIVITY(4),
    DELETE_ACTIVITY(5),
    UPDATE_PLACE(6);

    private int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
