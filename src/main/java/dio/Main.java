package dio;

import dio.exception.AccountNotFoundException;
import dio.exception.NoFundsEnoughException;
import dio.exception.WalletNotFoundException;
import dio.exception.InvestmentNotFoundException;
import dio.model.AccountWallet;
import dio.repository.AccountRepository;
import dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.time.OffsetDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("===== Ola, seja bem vindo ao Fin Java! =====");
        System.out.println();
        while (true){
            System.out.println("Selecione a operacao desejada");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferir entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Historico de Conta");
            System.out.println("14 - Sair");

            int option = scanner.nextInt();

            switch (option){
                case 1 -> createAccount();
                case 2 -> createInvestment();
                case 3 -> createInvestmentWallet();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transferToAccount();
                case 7 -> incInvestment();
                case 8 -> rescueInvestment();
                case 9 -> accountRepository.list().forEach(System.out::println);
                case 10 -> investmentRepository.list().forEach(System.out::println);
                case 11 -> investmentRepository.listWallets().forEach(System.out::println);
                case 12 ->{
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos atualizados com sucesso!");
                }
                case 13 -> checkHistory();
                case 14 -> System.exit(0);
                default -> System.out.println("Opçao invalida!");
            }
        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';'");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de deposito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada: " + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        int tax = scanner.nextInt();
        System.out.println("Informe o valor inicial");
        long initialFunds = scanner.nextLong();
        dio.model.Investment investiment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado: " + investiment);
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix para saque:");
        String pix = scanner.next();
        System.out.println("Informe o valor que será sacado:");
        long amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void deposit(){
        System.out.println("Informe a chave pix para deposito:");
        String pix = scanner.next();
        System.out.println("Informe o valor que será depositado:");
        long amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void transferToAccount(){
        System.out.println("Informe a chave pix da conta de origem:");
        String source = scanner.next();
        System.out.println("Informe a chave pix da conta de destino:");
        String target = scanner.next();
        System.out.println("Informe o valor que será depositado:");
        long amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(source, target, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void createInvestmentWallet(){
        System.out.println("Informe a chave pix da conta:");
        try {
            String pix = scanner.next();
            AccountWallet account = accountRepository.findByPix(pix);
            System.out.println("Informe o id do investimento:");
            int investmentId = scanner.nextInt();
            dio.model.InvestmentWallet investmentWallet = investmentRepository.initInvestment(account, investmentId);
            System.out.println("Sucesso! Carteira de investimento criada: " + investmentWallet);
        } catch (AccountNotFoundException | InvestmentNotFoundException | WalletNotFoundException | NoFundsEnoughException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void incInvestment(){
        System.out.println("Informe a chave pix da conta para investimento:");
        String pix = scanner.next();
        System.out.println("Informe o valor que será investido:");
        long amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
        } catch (AccountNotFoundException | WalletNotFoundException | NoFundsEnoughException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void rescueInvestment(){
        System.out.println("Informe a chave pix da conta para resgate do investimento:");
        String pix = scanner.next();
        System.out.println("Informe o valor que será sacado:");
        long amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | WalletNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory(){
	System.out.println("Informe a chave pix da conta para verificar extrato:");
	String pix = scanner.next();
	try {
		List<dio.model.MoneyAudit> history = accountRepository.findByPix(pix).getFinancialTransactions();
		Map<OffsetDateTime, List<dio.model.MoneyAudit>> grouped = history.stream().collect(Collectors.groupingBy(
			h -> h.getCreatedAt().truncatedTo(SECONDS)
		));
		grouped.forEach((k, v) -> {
			System.out.println(k.format(ISO_DATE_TIME));
			System.out.println(v.get(0).getTransactionId());
			System.out.println(v.get(0).getDescription());
			System.out.println(v.size());
		});
	} catch (AccountNotFoundException ex){
		System.out.println(ex.getMessage());
	}
}
}
