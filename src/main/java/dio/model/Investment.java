package dio.model;

public class Investment {

    private final long id;
    private final long tax;
    private final long initialFunds;

    public Investment(long id, long tax, long initialFunds) {
        this.id = id;
        this.tax = tax;
        this.initialFunds = initialFunds;
    }

    public long getId() {
        return id;
    }

    public long getTax() {
        return tax;
    }

    public long getInitialFunds() {
        return initialFunds;
    }
}
