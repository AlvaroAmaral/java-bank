package dio.repository;

import dio.exception.AccountNotFoundException;
import dio.exception.PixInUseException;
import dio.model.AccountWallet;

import java.util.List;

import static dio.repository.CommonsRepository.checkFundsForTrasaction;

public class AccountRepository {

    private List<AccountWallet> accounts;

    public AccountWallet create(final List<String> pix, final long initialFunds){
       var pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).toList();
        for (var p : pix){
            if (pixInUse.contains(p)) {
                throw new PixInUseException("O pix '" + p + "' já está em uso");
            }
        }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "depósito");
    }

    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);
        checkFundsForTrasaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        var target = findByPix(targetPix);
        checkFundsForTrasaction(source, amount);
        var message = "pix enviado de '" + sourcePix + "' para '" + targetPix + "'";
        target.addMoney(source.reduceMoney(amount), source.getService(), message);

    }

    public AccountWallet findByPix(final String pix){
        return this.accounts.stream()
                .filter(a -> a.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Conta com a chave pix '" + pix + "' não encontrada"));
    }

    public List<AccountWallet> list(){
        return this.accounts;
    }
}
