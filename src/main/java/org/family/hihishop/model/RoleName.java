package org.family.hihishop.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum RoleName {
    @Enumerated(EnumType.STRING)
    ADMIN,
    USER
}



