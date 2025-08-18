package dio.repository;

import dio.exception.AccountNotFoundException;
import dio.exception.PixInUseException;
import dio.model.AccountWallet;

import java.util.List;

import java.util.ArrayList;

import static dio.repository.CommonsRepository.checkFundsForTrasaction;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds){
       java.util.List<String> pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).collect(java.util.stream.Collectors.toList());
        for (String p : pix){
            if (pixInUse.contains(p)) {
                throw new PixInUseException("O pix '" + p + "' já está em uso");
            }
        }
        AccountWallet newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        AccountWallet target = findByPix(pix);
        target.addMoney(fundsAmount, "depósito");
    }

    public long withdraw(final String pix, final long amount){
        AccountWallet source = findByPix(pix);
        checkFundsForTrasaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        AccountWallet source = findByPix(sourcePix);
        AccountWallet target = findByPix(targetPix);
        checkFundsForTrasaction(source, amount);
        String message = "pix enviado de '" + sourcePix + "' para '" + targetPix + "'";
        target.addMoney(source.reduceMoney(amount), message);

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
