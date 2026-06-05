# Sistema de Folha de Pagamento

## Descrição

Sistema desenvolvido em Java para cadastro de colaboradores e geração de folha de pagamento. O programa calcula automaticamente o salário final de cada colaborador de acordo com seu tipo de contratação, utilizando os princípios da Programação Orientada a Objetos (POO).

## Funcionalidades

- Cadastro de Funcionário Padrão
- Cadastro de Funcionário Comissionado
- Cadastro de Funcionário de Produção
- Cálculo automático de salários
- Geração da folha de pagamento
- Validação de entradas do usuário
- Armazenamento dos colaboradores em ArrayList

## Conceitos Aplicados

- Abstração
- Encapsulamento
- Herança
- Polimorfismo
- Classes Abstratas
- ArrayList
- Estruturas de Decisão
- Laços de Repetição
- Tratamento de Exceções

## Tecnologias

- Java
- Scanner
- ArrayList

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```

2. Compile o arquivo:

   ```bash
   javac SistemaFolhaPagamento.java
   ```

3. Execute o programa:

   ```bash
   java SistemaFolhaPagamento
   ```

## 📚 Estrutura do Sistema

- `Colaborador` → Classe abstrata base.
- `FuncionarioPadrao` → Salário sem adicionais.
- `FuncionarioComissionado` → Salário com comissão sobre vendas.
- `FuncionarioProducao` → Salário com bônus por produção.
- `FolhaPagamentoService` → Gerenciamento dos colaboradores e geração da folha.
- `Menu` → Interface de interação com o usuário.
- `SistemaFolhaPagamento` → Classe principal.

## 👩‍💻 Autora
Juliana Eleriano Cabral 
Projeto desenvolvido para estudo e aplicação dos conceitos de Programação Orientada a Objetos em Java.


Juliana Santos

Projeto desenvolvido para estudo e aplicação dos conceitos de Programação Orientada a Objetos em Java.
