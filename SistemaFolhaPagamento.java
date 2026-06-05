import java.util.ArrayList;
import java.util.Scanner;

/** Dados de um funcionário padrão */
record FuncionarioPadrao(int matricula, String nome) implements Colaborador {

    @Override
    public double calcularExtras() {
        return 0.0;
    }

    @Override
    public void exibirDados() {
        Colaborador.cabecalho(nome, matricula);
        System.out.printf("Extras       : R$ %.2f%n", 0.0);
        System.out.printf("Salário Final: R$ %.2f%n", calcularSalarioFinal());
    }
}

/** Dados de um funcionário comissionado */
record FuncionarioComissionado(int matricula, String nome,
                               double totalVendas, double percentualComissao)
        implements Colaborador {

    @Override
    public double calcularExtras() {
        // Comissão = vendas * percentual / 100
        return totalVendas * percentualComissao / 100.0;
    }

    @Override
    public void exibirDados() {
        Colaborador.cabecalho(nome, matricula);
        System.out.printf("Total de Vendas    : R$ %.2f%n", totalVendas);
        System.out.printf("Percentual Comissão: %.2f%%%n",  percentualComissao);
        System.out.printf("Comissão           : R$ %.2f%n", calcularExtras());
        System.out.printf("Salário Final      : R$ %.2f%n", calcularSalarioFinal());
    }
}

/** Dados de um funcionário de produção */
record FuncionarioProducao(int matricula, String nome,
                           int quantidadePecas, double valorPorPeca)
        implements Colaborador {

    @Override
    public double calcularExtras() {
        // Bônus = valorPorPeça * quantidadeProduzida
        return valorPorPeca * quantidadePecas;
    }

    @Override
    public void exibirDados() {
        Colaborador.cabecalho(nome, matricula);
        System.out.printf("Qtde de Peças  : %d%n",       quantidadePecas);
        System.out.printf("Valor por Peça : R$ %.2f%n",  valorPorPeca);
        System.out.printf("Bônus Produção : R$ %.2f%n",  calcularExtras());
        System.out.printf("Salário Final  : R$ %.2f%n",  calcularSalarioFinal());
    }
}

sealed interface Colaborador
        permits FuncionarioPadrao, FuncionarioComissionado, FuncionarioProducao {

    // Constante do salário base — disponível para todos
    double SALARIO_BASE = 2_000.00; // '_' em literais numéricos melhora a leitura

    int    matricula();
    String nome();

    double calcularExtras();

    /** Método default: implementação padrão compartilhada por todos */
    default double calcularSalarioFinal() {
        return SALARIO_BASE + calcularExtras();
    }

    void exibirDados();

    /** Método estático utilitário — imprime o cabeçalho comum */
    static void cabecalho(String nome, int matricula) {
        System.out.println("--------------------------------------------");
        System.out.println("Nome         : " + nome);
        System.out.println("Matrícula    : " + matricula);
        System.out.printf ("Salário Base : R$ %.2f%n", SALARIO_BASE);
    }
}

// ============================================================
//  PROGRAMA PRINCIPAL
// ============================================================
public class SistemaFolhaPagamento {

    // Lista dinâmica — armazena qualquer implementação de Colaborador
    private static final ArrayList<Colaborador> colaboradores = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // -------------------------------------------------------
    //  Métodos auxiliares de leitura com validação
    // -------------------------------------------------------

    /** Lê uma String não vazia */
    private static String lerString(String mensagem) {
        String valor;
        do {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();
            if (valor.isBlank()) {              // isBlank() — Java 11+
                System.out.println("  ⚠  Campo obrigatório. Tente novamente.");
            }
        } while (valor.isBlank());
        return valor;
    }

    /** Lê um inteiro positivo (> 0) */
    private static int lerInteiroPositivo(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor <= 0) {
                    System.out.println("  ⚠  O valor deve ser maior que zero.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Entrada inválida. Informe um número inteiro.");
            }
        }
    }

    /** Lê um double positivo (> 0) — aceita vírgula ou ponto decimal */
    private static double lerDoublePositivo(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String entrada = scanner.nextLine().trim().replace(",", ".");
                double valor = Double.parseDouble(entrada);
                if (valor <= 0) {
                    System.out.println("  ⚠  O valor deve ser maior que zero.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Entrada inválida. Informe um número válido (ex.: 1500 ou 0,20).");
            }
        }
    }

    /** Lê a opção do menu (inteiro >= 0) */
    private static int lerOpcaoMenu() {
        while (true) {
            try {
                System.out.print("\nEscolha uma opção: ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                if (opcao < 0) {
                    System.out.println("  ⚠  Opção inválida. Tente novamente.");
                } else {
                    return opcao;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Entrada inválida. Informe um número.");
            }
        }
    }

    // -------------------------------------------------------
    //  Opção 1 – Cadastrar Funcionário Padrão
    // -------------------------------------------------------
    private static void cadastrarFuncionarioPadrao() {
        System.out.println("\n=== Cadastro: Funcionário Padrão ===");
        String nome      = lerString("Nome: ");
        int    matricula = lerInteiroPositivo("Matrícula: ");

        colaboradores.add(new FuncionarioPadrao(matricula, nome));
        System.out.println("✔ Funcionário padrão '" + nome + "' cadastrado com sucesso!");
    }

    // -------------------------------------------------------
    //  Opção 2 – Cadastrar Funcionário Comissionado
    // -------------------------------------------------------
    private static void cadastrarFuncionarioComissionado() {
        System.out.println("\n=== Cadastro: Funcionário Comissionado ===");
        String nome       = lerString("Nome: ");
        int    matricula  = lerInteiroPositivo("Matrícula: ");
        double vendas     = lerDoublePositivo("Informe valor das vendas: R$ ");
        double percentual = lerDoublePositivo("Informe comissão percentual (%): ");

        colaboradores.add(new FuncionarioComissionado(matricula, nome, vendas, percentual));
        System.out.println("✔ Funcionário comissionado '" + nome + "' cadastrado com sucesso!");
    }

    // -------------------------------------------------------
    //  Opção 3 – Cadastrar Funcionário de Produção
    // -------------------------------------------------------
    private static void cadastrarFuncionarioProducao() {
        System.out.println("\n=== Cadastro: Funcionário de Produção ===");
        String nome      = lerString("Nome: ");
        int    matricula = lerInteiroPositivo("Matrícula: ");
        int    qtdPecas  = lerInteiroPositivo("Informe qtde de peças: ");
        double valorPeca = lerDoublePositivo("Informe valor da peça: R$ ");

        colaboradores.add(new FuncionarioProducao(matricula, nome, qtdPecas, valorPeca));
        System.out.println("✔ Funcionário de produção '" + nome + "' cadastrado com sucesso!");
    }

    // -------------------------------------------------------
    //  Opção 4 – Gerar Folha de Pagamento
    //  Usa Pattern Matching para instanceof (Java 16+)
    // -------------------------------------------------------
    private static void gerarFolhaPagamento() {
        System.out.println("\n============================================");
        System.out.println("          FOLHA DE PAGAMENTO               ");
        System.out.println("============================================");

        if (colaboradores.isEmpty()) {
            System.out.println("Nenhum colaborador cadastrado.");
            return;
        }

        System.out.println("Total de pessoas cadastradas: " + colaboradores.size());

        double totalGeral = 0.0;

        for (Colaborador c : colaboradores) {

            // Pattern Matching para instanceof (Java 16+ — estável no Java 17)
            // Em vez de: if (c instanceof FuncionarioPadrao) { FuncionarioPadrao fp = (FuncionarioPadrao) c; }
            // Escreve-se simplesmente:
            if (c instanceof FuncionarioPadrao fp) {
                System.out.printf("%n[PADRÃO] Matrícula: %d%n", fp.matricula());
            } else if (c instanceof FuncionarioComissionado fc) {
                System.out.printf("%n[COMISSIONADO] Matrícula: %d%n", fc.matricula());
            } else if (c instanceof FuncionarioProducao fp) {
                System.out.printf("%n[PRODUÇÃO] Matrícula: %d%n", fp.matricula());
            }

            c.exibirDados();
            totalGeral += c.calcularSalarioFinal();
        }

        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL DA FOLHA: R$ %.2f%n", totalGeral);
        System.out.println("============================================");
    }

    // -------------------------------------------------------
    //  Menu — usa Text Block (Java 15+, estável no Java 17)
    // -------------------------------------------------------
    private static void exibirMenu() {
        // Text Block: string multilinha com """ — sem concatenação nem \n manuais
        String menu = """

                ╔══════════════════════════════════════╗
                ║   SISTEMA DE FOLHA DE PAGAMENTO      ║
                ╠══════════════════════════════════════╣
                ║  1 - Cadastrar Funcionário Padrão    ║
                ║  2 - Cadastrar Funcionário Comiss.   ║
                ║  3 - Cadastrar Funcionário Produção  ║
                ║  4 - Gerar Folha de Pagamento        ║
                ║  0 - Sair do Programa                ║
                ╚══════════════════════════════════════╝
                """;
        System.out.print(menu);
    }

    // -------------------------------------------------------
    //  Método principal
    // -------------------------------------------------------
    public static void main(String[] args) {

        int opcao;

        // do-while — garante que o menu é exibido ao menos uma vez
        do {
            exibirMenu();
            opcao = lerOpcaoMenu();

            // Não precisa de break — cada ramo é isolado pela seta ->
            switch (opcao) {
                case 1 -> cadastrarFuncionarioPadrao();
                case 2 -> cadastrarFuncionarioComissionado();
                case 3 -> cadastrarFuncionarioProducao();
                case 4 -> gerarFolhaPagamento();
                case 0 -> System.out.println("\nEncerrando o sistema. Até logo!");
                default -> System.out.println("  ⚠  Opção inválida. Escolha entre 0 e 4.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}