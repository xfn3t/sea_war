package com.greenatom.exception;

import java.io.IOException;

public class ShipTypeException extends IOException {
    public ShipTypeException() {
        super("You can't set ships this type");
    }

    public ShipTypeException(String s) {
        super(s);
    }
}
