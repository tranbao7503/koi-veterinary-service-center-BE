package org.ftf.koifishveterinaryservicecenter.enums;

public enum Role {
    MANAGER(1),
    CUSTOMER(2),
    VETERINARIAN(3),
    STAFF(4);


    private int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
