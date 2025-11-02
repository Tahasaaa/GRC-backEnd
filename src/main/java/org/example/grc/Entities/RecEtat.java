package org.example.grc.Entities;

public enum RecEtat {
    EN_ATTENTE("En attente"),
    EN_COURS("En cours"),
    CLOTUREE("Clôturée"),
    REJETEE("Rejetée");

    private final String label;

    RecEtat(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
