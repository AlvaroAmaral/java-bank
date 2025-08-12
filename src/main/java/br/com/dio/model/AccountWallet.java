package br.com.dio.model;

import java.util.List;

public class AccountWallet extends Wallet{
    private final List<String> pix;

    public AccountWallet(final BankService service){
        super(service);
    }
}
