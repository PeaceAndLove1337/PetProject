package data;

public enum Bank {
    VTB("ВТБ"),
    SBERBANK("СБЕРБАНК"),
    TINKOFF("ТUНЬКОФФ"),
    ALPHABANK("АЛЬФАБАНК"),
    CB("ЦЕНТРОБАНК РФ");

    private String nameInRussian;

    Bank(String nameInRussian) {
        this.nameInRussian = nameInRussian;
    }

    public String getNameInRussian() {
        return nameInRussian;
    }
}
