package dio.repository;

import dio.exception.PixInUseException;
import dio.exception.WalletNotFoundException;
import dio.model.AccountWallet;
import dio.model.Investment;
import dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static dio.repository.CommonsRepository.checkFundsForTrasaction;


public class InvestmentRepository {

    private long nextId = 0;
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds){
        this.nextId ++;
        Investment investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id){
        if (!wallets.isEmpty()) {
            java.util.List<AccountWallet> accountInUse = wallets.stream().map(w -> w.getAccount()).collect(java.util.stream.Collectors.toList());
            if (accountInUse.contains(account)) {
                throw new PixInUseException("A conta '" + account + "' já possui investimento");
            }
        }
        dio.model.Investment investment = findById(id);
        checkFundsForTrasaction(account, investment.getInitialFunds());
        InvestmentWallet wallet = new InvestmentWallet(investment, account, investment.getInitialFunds());
        wallets.add(wallet);
        return wallet;
    }
    public InvestmentWallet deposit(final String pix, final long funds){
        InvestmentWallet wallet = findWalletByAccountPix(pix);
        checkFundsForTrasaction(wallet.getAccount(), funds);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), "Investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds){
        InvestmentWallet wallet = findWalletByAccountPix(pix);
        checkFundsForTrasaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), "saque de investimento");
        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAmount(){
        wallets.forEach(w -> w.updateAmount(w.getInvestment().getTax()));
    }

    public Investment findById(final long id){
        return investments.stream().filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new dio.exception.InvestmentNotFoundException(
                        "Investimento id " + id + " não encontrado"));
    }

    public InvestmentWallet findWalletByAccountPix(final String pix){
        return wallets.stream().filter(w -> w.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(
                        () -> new WalletNotFoundException("A carteira não foi encontrada")
                );
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> list(){
        return this.investments;
    }
}
