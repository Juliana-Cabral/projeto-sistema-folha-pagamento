import java.util.ArrayList;
import java.util.Scanner;

// ============================================================
//  PILAR 1 — ABSTRAÇÃO
//  Classe abstrata que modela o conceito genérico de Colaborador.
//  Não pode ser instanciada diretamente — só suas subclasses.
// ============================================================
abstract class Colaborador {

    // --------------------------------------------------------
    //  PILAR 2 — ENCAPSULAMENTO
    //  Atributos privados: só acessíveis por getters/setters.
    //  O mundo externo não manipula os dados diretamente.
    // --------------------------------------------------------
    private int    matricula;
    private String nome;

    // Constante do salário base — compartilhada por todas as subclasses
    protected static final double SALARIO_BASE = 2_000.00;

    // Construtor da classe abstrata — chamado pelos construtores filhos via super()
    public Colaborador(int matricula, String nome) {
        this.matricula = matricula;
        this.nome      = nome;
    }

    // --------------------------------------------------------
    //  Getters — acesso controlado aos atributos privados
    // --------------------------------------------------------
    public int    getMatricula() { return matricula; }
    public String getNome()      { return nome; }

    // --------------------------------------------------------
    //  PILAR 3 — POLIMORFISMO
    //  Método abstrato: cada subclasse OBRIGATORIAMENTE o implementa
    //  com sua própria lógica. O nome é o mesmo, o comportamento muda.
    // --------------------------------------------------------
    public abstract double calcularExtras();

    // Método concreto que usa o resultado abstrato acima
    public double calcularSalarioFinal() {
        return SALARIO_BASE + calcularExtras();
    }

    // Método abstrato para exibição — cada subclasse exibe seus dados
    public abstract void exibirDados();

    // Método utilitário compartilhado — imprime o cabeçalho comum
    protected void exibirCabecalho() {
        System.out.println("--------------------------------------------");
        System.out.println("Nome         : " + nome);
        System.out.println("Matrícula    : " + matricula);
        System.out.printf ("Salário Base : R$ %.2f%n", SALARIO_BASE);
    }

    // toString sobrescrito — representação textual do objeto
    @Override
    public String toString() {
        return String.format("Colaborador[matricula=%d, nome=%s, salarioFinal=R$ %.2f]",
                matricula, nome, calcularSalarioFinal());
    }
}

// ============================================================
//  PILAR 4 — HERANÇA
//  FuncionarioPadrao herda tudo de Colaborador e especializa
//  apenas o que for diferente (calcularExtras e exibirDados).
// ============================================================
class FuncionarioPadrao extends Colaborador {

    // Construtor repassa os dados comuns para a superclasse
    public FuncionarioPadrao(int matricula, String nome) {
        super(matricula, nome); // chama o construtor de Colaborador
    }

    // Funcionário padrão não tem extras — retorna 0
    @Override
    public double calcularExtras() {
        return 0.0;
    }

    // Exibe cabeçalho comum (herdado) + dados específicos
    @Override
    public void exibirDados() {
        exibirCabecalho();
        System.out.printf("Extras        : R$ %.2f%n", 0.0);
        System.out.printf("Salário Final : R$ %.2f%n", calcularSalarioFinal());
    }
}

// ============================================================
//  FuncionarioComissionado — herda Colaborador e adiciona
//  dois atributos próprios: totalVendas e percentualComissao.
// ============================================================
class FuncionarioComissionado extends Colaborador {

    // Atributos específicos desta subclasse — encapsulados
    private double totalVendas;
    private double percentualComissao;

    public FuncionarioComissionado(int matricula, String nome,
                                   double totalVendas, double percentualComissao) {
        super(matricula, nome);
        this.totalVendas        = totalVendas;
        this.percentualComissao = percentualComissao;
    }

    // Getters dos atributos específicos
    public double getTotalVendas()        { return totalVendas; }
    public double getPercentualComissao() { return percentualComissao; }

    // Fórmula da comissão: vendas * percentual / 100
    @Override
    public double calcularExtras() {
        return totalVendas * percentualComissao / 100.0;
    }

    @Override
    public void exibirDados() {
        exibirCabecalho();
        System.out.printf("Total de Vendas     : R$ %.2f%n", totalVendas);
        System.out.printf("Percentual Comissão : %.2f%%%n",  percentualComissao);
        System.out.printf("Comissão            : R$ %.2f%n", calcularExtras());
        System.out.printf("Salário Final       : R$ %.2f%n", calcularSalarioFinal());
    }
}

// ============================================================
//  FuncionarioProducao — herda Colaborador e adiciona
//  quantidadePecas e valorPorPeca.
// ============================================================
class FuncionarioProducao extends Colaborador {

    // Atributos específicos — encapsulados
    private int    quantidadePecas;
    private double valorPorPeca;

    public FuncionarioProducao(int matricula, String nome,
                               int quantidadePecas, double valorPorPeca) {
        super(matricula, nome);
        this.quantidadePecas = quantidadePecas;
        this.valorPorPeca    = valorPorPeca;
    }

    // Getters dos atributos específicos
    public int    getQuantidadePecas() { return quantidadePecas; }
    public double getValorPorPeca()    { return valorPorPeca; }

    // Fórmula do bônus: valor * quantidade
    @Override
    public double calcularExtras() {
        return valorPorPeca * quantidadePecas;
    }

    @Override
    public void exibirDados() {
        exibirCabecalho();
        System.out.printf("Qtde de Peças   : %d%n",       quantidadePecas);
        System.out.printf("Valor por Peça  : R$ %.2f%n",  valorPorPeca);
        System.out.printf("Bônus Produção  : R$ %.2f%n",  calcularExtras());
        System.out.printf("Salário Final   : R$ %.2f%n",  calcularSalarioFinal());
    }
}

// ============================================================
//  CLASSE DE SERVIÇO — FolhaPagamentoService
//  Responsabilidade única: gerenciar a lista de colaboradores
//  e gerar a folha. Separa a lógica de negócio da interface.
// ============================================================
class FolhaPagamentoService {

    // ArrayList tipado — armazena qualquer subclasse de Colaborador
    private final ArrayList<Colaborador> colaboradores = new ArrayList<>();

    // Adiciona qualquer Colaborador (polimorfismo: aceita os 3 tipos)
    public void adicionar(Colaborador colaborador) {
        colaboradores.add(colaborador);
    }

    public int getTotalColaboradores() {
        return colaboradores.size();
    }

    public boolean isEmpty() {
        return colaboradores.isEmpty();
    }

    // Gera e imprime a folha de pagamento completa
    public void gerarFolha() {
        System.out.println("\n============================================");
        System.out.println("          FOLHA DE PAGAMENTO               ");
        System.out.println("============================================");

        if (isEmpty()) {
            System.out.println("Nenhum colaborador cadastrado.");
            return;
        }

        System.out.println("Total de pessoas cadastradas: " + getTotalColaboradores());

        double totalGeral = 0.0;

        // Polimorfismo em ação: o for-each chama o exibirDados() correto
        // para cada tipo sem precisar de if/else ou instanceof
        for (Colaborador c : colaboradores) {
            c.exibirDados();
            totalGeral += c.calcularSalarioFinal();
        }

        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL DA FOLHA : R$ %.2f%n", totalGeral);
        System.out.println("============================================");
    }
}

// ============================================================
//  CLASSE DE INTERFACE — Menu
//  Responsabilidade única: interação com o usuário.
//  Toda leitura, validação e exibição de telas fica aqui.
// ============================================================
class Menu {

    private final Scanner              scanner;
    private final FolhaPagamentoService servico;

    // Injeção de dependência via construtor
    public Menu(Scanner scanner, FolhaPagamentoService servico) {
        this.scanner = scanner;
        this.servico = servico;
    }

    // --------------------------------------------------------
    //  Métodos privados de leitura com validação
    // --------------------------------------------------------

    private String lerString(String mensagem) {
        String valor;
        do {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();
            if (valor.isBlank()) {
                System.out.println("  ⚠  Campo obrigatório. Tente novamente.");
            }
        } while (valor.isBlank());
        return valor;
    }

    private int lerInteiroPositivo(String mensagem) {
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

    private double lerDoublePositivo(String mensagem) {
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

    private int lerOpcao() {
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

    // --------------------------------------------------------
    //  Métodos de cadastro — criam objetos e delegam ao serviço
    // --------------------------------------------------------

    private void cadastrarFuncionarioPadrao() {
        System.out.println("\n=== Cadastro: Funcionário Padrão ===");
        String nome      = lerString("Nome: ");
        int    matricula = lerInteiroPositivo("Matrícula: ");

        // Cria o objeto e delega para o serviço armazenar
        Colaborador funcionario = new FuncionarioPadrao(matricula, nome);
        servico.adicionar(funcionario);

        System.out.println("✔ '" + nome + "' cadastrado com sucesso!");
    }

    private void cadastrarFuncionarioComissionado() {
        System.out.println("\n=== Cadastro: Funcionário Comissionado ===");
        String nome       = lerString("Nome: ");
        int    matricula  = lerInteiroPositivo("Matrícula: ");
        double vendas     = lerDoublePositivo("Informe valor das vendas: R$ ");
        double percentual = lerDoublePositivo("Informe comissão percentual (%): ");

        Colaborador funcionario = new FuncionarioComissionado(matricula, nome, vendas, percentual);
        servico.adicionar(funcionario);

        System.out.println("✔ '" + nome + "' cadastrado com sucesso!");
    }

    private void cadastrarFuncionarioProducao() {
        System.out.println("\n=== Cadastro: Funcionário de Produção ===");
        String nome      = lerString("Nome: ");
        int    matricula = lerInteiroPositivo("Matrícula: ");
        int    qtdPecas  = lerInteiroPositivo("Informe qtde de peças: ");
        double valorPeca = lerDoublePositivo("Informe valor da peça: R$ ");

        Colaborador funcionario = new FuncionarioProducao(matricula, nome, qtdPecas, valorPeca);
        servico.adicionar(funcionario);

        System.out.println("✔ '" + nome + "' cadastrado com sucesso!");
    }

    // --------------------------------------------------------
    //  Exibe o menu
    // --------------------------------------------------------
    private void exibir() {
        String tela = """

                ╔══════════════════════════════════════╗
                ║   SISTEMA DE FOLHA DE PAGAMENTO     ║
                ╠══════════════════════════════════════╣
                ║  1 - Cadastrar Funcionário Padrão   ║
                ║  2 - Cadastrar Funcionário Comiss.  ║
                ║  3 - Cadastrar Funcionário Produção ║
                ║  4 - Gerar Folha de Pagamento       ║
                ║  0 - Sair do Programa               ║
                ╚══════════════════════════════════════╝
                """;
        System.out.print(tela);
    }

    // --------------------------------------------------------
    //  Executa o loop principal do menu
    // --------------------------------------------------------
    public void executar() {
        int opcao;

        do {
            exibir();
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarFuncionarioPadrao();
                case 2 -> cadastrarFuncionarioComissionado();
                case 3 -> cadastrarFuncionarioProducao();
                case 4 -> servico.gerarFolha();
                case 0 -> System.out.println("\nEncerrando o sistema. Até logo!");
                default -> System.out.println("  ⚠  Opção inválida. Escolha entre 0 e 4.");
            }

        } while (opcao != 0);
    }
}

// ============================================================
//  CLASSE PRINCIPAL — SistemaFolhaPagamento
//  Responsabilidade única: inicializar e conectar os objetos.
//  O main() cria as dependências e dispara a execução.
// ============================================================
public class SistemaFolhaPagamento {

    public static void main(String[] args) {

        // Criação dos objetos — cada um com sua responsabilidade
        Scanner              scanner = new Scanner(System.in);
        FolhaPagamentoService servico = new FolhaPagamentoService();
        Menu                 menu    = new Menu(scanner, servico);

        // Inicia o programa
        menu.executar();

        // Libera o recurso ao encerrar
        scanner.close();
    }
}