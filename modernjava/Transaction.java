public class Transaction {
    private final Trader trader;
    private final int year;
    private final int value;

    @Override
    public String toString() {
        return "{" + this.trader + ", " + "year : " + this.year + ", " + "value : " + this.value + "}";
    }

    public Trader getTrader() {
        return trader;
    }

    public int getYear() {
        return year;
    }

    public int getValue() {
        return value;
    }

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
}
