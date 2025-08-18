package dio.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MoneyAudit {

    private final UUID transactionId;
    private final BankService targetService;
    private final String description;
    private final OffsetDateTime createdAt;

    public MoneyAudit(UUID transactionId, BankService targetService, String description, OffsetDateTime createdAt) {
        this.transactionId = transactionId;
        this.targetService = targetService;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public BankService getTargetService() {
        return targetService;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
