package gui.pl.demo;

public class Option {
    public int idZestawu;
    public int idSciezki;
    public String nazwa;

    public Option(int idZestawu, int idSciezki, String nazwa) {
        this.idZestawu = idZestawu;
        this.idSciezki = idSciezki;
        this.nazwa = nazwa;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}
