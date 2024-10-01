package com.greenatom.exception;

import java.io.IOException;

public class ShipOverlapsException extends IOException {
    public ShipOverlapsException() {
        super("Ship overlaps with an existing one!");
    }
}
