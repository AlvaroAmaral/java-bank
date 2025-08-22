package dio.repository;

import dio.exception.NoFundsEnoughException;
import dio.model.Money;
import dio.model.MoneyAudit;
import dio.model.Wallet;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static dio.model.BankService.ACCOUNT;

public final class CommonsRepository {

    private CommonsRepository() {}

    public static void checkFundsForTrasaction(final Wallet source, final long amount){
        if (source.getFunds() < amount){
            long disponivel = source.getFunds();
            throw new NoFundsEnoughException("Fundos insuficientes. Disponível: " + disponivel + " centavos");
        }
    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String description){
        MoneyAudit history = new MoneyAudit(transactionId, ACCOUNT, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(funds).collect(java.util.stream.Collectors.toList());
    }
}
