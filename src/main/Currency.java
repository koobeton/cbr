package main;

enum Currency {

    USD("R01235"),
    EUR("R01239"),
    RUB(null);

    private final String cbrCode;

    Currency(String cbrCode) {
        this.cbrCode = cbrCode;
    }

    String getCbrCode() {
        return cbrCode;
    }
}