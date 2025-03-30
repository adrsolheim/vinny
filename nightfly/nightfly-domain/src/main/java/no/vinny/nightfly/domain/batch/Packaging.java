package no.vinny.nightfly.domain.batch;

public enum Packaging {
    KEG("Keg", "Fat"),
    BOTTLE("Bottle", "Flaske"),
    CAN("Can", "Boks"),
    FERMENTER("Fermenter", "Gjæringskar");

    private final String name;
    private final String navn;

    Packaging(String name, String navn) {
        this.name = name;
        this.navn = navn;
    }

    public String getName() {
        return name;
    }

    public String getNavn() {
        return navn;
    }
}
