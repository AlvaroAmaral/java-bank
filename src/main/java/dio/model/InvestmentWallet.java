package dio.model;

import static dio.model.BankService.INVESTMENT;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

public class InvestmentWallet extends Wallet{


    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(final Investment investment, final AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), "investimento");
    }

    public void updateAmount(final long percent){
        long amount = getFunds() * percent / 100;
        MoneyAudit history = new MoneyAudit(UUID.randomUUID(), getService(), "rendimentos", OffsetDateTime.now());
        java.util.List<Money> money = Stream.generate(() -> new Money(history)).limit(amount).collect(java.util.stream.Collectors.toList());
        this.money.addAll(money);
    }

    public Investment getInvestment() {
        return investment;
    }

    public AccountWallet getAccount() {
        return account;
    }

}
