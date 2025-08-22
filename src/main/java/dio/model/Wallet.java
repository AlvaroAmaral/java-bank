package dio.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Wallet {

    private final BankService service;

    protected final List<Money> money;

    public Wallet(BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    public BankService getService() {
        return this.service;
    }

    protected List<Money> generateMoney(final long amount, final String description){
        MoneyAudit history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history))
                .limit(amount)
                .collect(java.util.stream.Collectors.toList());
    }

    public long getFunds(){
        return money.size();
    }

    public void addMoney(final List<Money> money, final String description) {
        MoneyAudit history = new MoneyAudit(UUID.randomUUID(), this.service, description, OffsetDateTime.now());
        money.forEach(m -> m.addHistory(history));
        this.money.addAll(money);
    }

    public List<Money> reduceMoney(final long amount){
	List<Money> toRemove = new ArrayList<>();
	for (int i = 0; i < amount; i++){
		toRemove.add(this.money.remove(0));
	}
	return toRemove;
}

    public List<MoneyAudit> getFinancialTransactions(){
        return money.stream().flatMap(m -> m.getHistory().stream()).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public String toString() {
        long cents = getFunds();
        long reais = cents / 100;
        return "Wallet{service=" + service + ", money= R$" + reais + "}";
    }
}
