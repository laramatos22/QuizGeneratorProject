
# Gerador de Questionários

#### Departamento de Eletrónica, Telecomunicações e Informática

<img src="https://blogs.ua.pt/cidtff/wp-content/uploads/2018/12/logo_UA.png" alt="Universidade de Aveiro" width="250"/>

### Linguagens Formais e Autómatos

---

| NMec | Nome | Email | Contribuição (%) | Contribuição detalhada (\*) |
|:-:|:--|:--|:-:|:--|
| 85088 | Francisco Martinho | martinho.francisco@ua.pt | 20% | secondary-semantic-analysis (33%)<br>secondary-grammar (33%)<br>examples (20%)<br>testing (25%)<br>doc (15%)|
| 88930 | João Simões | jtsimoes@ua.pt | 20% | secondary-semantic-analysis (33%)<br>secondary-grammar (33%)<br>examples (20%)<br>testing (25%)<br>doc (15%)||
| 93088 | Eduardo Cruz | e.cruz@ua.pt | 10% | auxilary-grammar (50%)<br>docs (35%)<br>testing (50%)|
| 95228 | Lara Matos | lara.catarina.matos@ua.pt | 20% | primary-semantic-analysis (10%)<br>docs (35%)<br>testing (15%)<br>examples (10%)<br>code-generation (35%)|
| 98003 | João Felisberto | joaofelisberto@ua.pt | 30% | primary-grammar (90%)<br>primary-semantic-analysis (90%)<br>code-generation (??%)<br>secondary-semantic-analysis (33%)<br>secondary-grammar (33%)<br>examples (30%)<br>testing (25%)<br>doc (35%)|

>**(\*) Tópicos:**
primary-grammar (%)
| primary-semantic-analysis (%)
| code-generation (%)
| secondary-grammar (%)
| secondary-semantic-analysis (%)
| secondary-interpretation/secondary-code-generation (%)
| examples (%)
| testing (%)
| other (%) (explain)

## Resumo 

No âmbito da Unidade Curricular de Linguagens Formais e Autómatos, foi
proposto o desenvolvimento de um projeto a este grupo de alunos. Esse
projeto consiste num Gerador de Questionários.

Neste relatório, serão abordados itens relacionados com este trabalho de grupo, tais
como os objetivos com este trabalho, a linguagem para a
Base de Dados (***CQuiz***), a linguagem para o Desenvolvimento de
Questionários (***Quiz***) e as conclusões tiradas com o
desenvolvimento deste projeto.

O diretório `/doc/manual/` contém ainda um [Manual de Utlização](/doc/manual/manual.pdf) com
todas as indicações a ter em consideração para o bom funcionamento por
parte do utilizador.

## Introdução

No nosso dia a dia, é muito frequente depararmo-nos com avaliações e
desafios. Para isso, é usual a utilização de questionários para a
avaliação de conhecimentos, sendo essas questões de vários tipos, tais
como de escolha múltipla, correspondência, resposta direta, entre
outros.

Os objetivos deste projeto são:

-   Definição de uma linguagem principal para a construção de
    questionários interativos;

-   Construção de um compilador para transformar a linguagem num programa
    de linguagem genérica;

-   Definição de uma linguagem auxiliar para a construção de bancos de
    perguntas;

-   Extração dos bancos de perguntas através de linguagem principal.

Para iniciar este projeto, surge a linguagem ***Quiz*** onde se processa
toda a estrutura do questionário, com semelhanças a linguagens de alto
nível e ferramentas adicionais para desenvolvimento de questionários
interativos.

***Quiz*** apresenta a possibilidade de:

-   Definir variáveis de diferentes tipos;

-   Utilizar ciclos e instruções de condição;

-   Conter instruções para a apresentação de questionários e respetiva
    modulação;

-   Processar respostas e sua respetiva recolha das mesmas para análise.

Quando o ficheiro `Quiz.g4` é compilado, usando os comandos de
**ANTLR4**, cria-se um ficheiro com a linguagem de destino. Neste caso,
será em **Java**.

Sabendo agora da existência da linguagem de desenvolvimento de
questionários, é necessária a criação de uma linguagem auxiliar
`CQuiz.g4` que serve de base de dados, ou seja, guarda as perguntas para
posterior utilização nos questionários. Esta linguagem é muito mais
simples do que a ***Quiz***, permitindo assim ao programador descrever
ficheiros de perguntas que são carregadas para uma base de dados que
tanto pode ser ou não utilizada para o desenvolvimento do(s)
questionário(s) que queremos.

Para um melhor entendimento do que está descrito acima, seguem-se
secções que irão descrever detalhadamente as linguagens [***CQuiz***](#linguagem-auxiliar-cquiz) e [***Quiz***](#linguagem-principal-quiz), com respetivos exemplos.
Adicionalmente, foi também criado um [Manual de Utlização](/doc/manual/manual.pdf) no diretório
`/doc/manual/`, com instruções de compilação dos programas desenvolvidos e
como é que cada um deverá correr.

## Linguagem auxiliar *CQuiz* 

Foi criada uma linguagem simples ***CQuiz*** para gerar uma base de
dados de perguntas que o utilizador pode usufruir. Esta linguagem tem
como objetivo a descrição de ficheiros que contêm as perguntas que podem
ser usadas num determinado questionário.

Cada pergunta é constituída pelas características presentes na seguinte
tabela:

| Característica | Definição |
|:--|:--|
|  ID | identificador (índice da pergunta) |
|  pergunta   |    string (texto da pergunta) |
|  grupos     |    genérico (tema, dificuldade, ...) |
|  dependências |  só pode responder a uma pergunta se tiver feito as anteriores |
|  tipo | classifica o tipo de pergunta |
|  resposta     |  string (texto da resposta) |
|  *validator*  |   valida a resposta |

É de salientar que, em cada tema, não é permitido que haja IDs iguais
nem perguntas iguais, da mesma forma como também não é permitido
respostas iguais numa mesma pergunta.

Assim, ficou mais percetível como é que cada pergunta é constituída. No
seguimento deste raciocínio, a base de dados das perguntas será
organizada numa Lista com todas as perguntas que podem ser usadas num
determinado questionário originado pelo utilizador.

Exemplo:
```
ID: question {
	[group1, group2, group3, ...];
	[depend1, depend2, ...];
	<multiple|match|numeric|short|long>;
	< possible answers >
	< correct answer >
}
```
## Linguagem principal *Quiz*

### Instruções

Em **Quiz** tanto pode haver instruções em bloco como instruções de
linha. Nestas últimas, as instruções são sempre terminadas com o
caracter `;`.

### Tipos

A linguagem **Quiz** apresenta os seguintes tipos:

-   `int`: representação de um valor inteiro; equivalente ao Integer na
    linguagem de destino.

-   `string`: representação de strings; equivalente ao `String` na linguagem
    de destino;

-   `question`: representação de questões da classe Question;

-   `number`: representação de um número;

-   `text`: representação de texto;

-   `list`: representação de uma estrutura de dados List na linguagem de
    destino;

### Comentários

Na linguagem ***Quiz***, é considerada uma linha em comentário sempre
que, no início dessa mesma linha, estiver presente o caracter `#`.

### Palavras Reservadas

As palavras reservadas da linguagem ***Quiz*** são:

-   `main`
-   `stat`
-   `qdb`
-   `question`
-   `varDecl`
-   `mcall`
-   `qans`
-   `qcorrect`
-   `propertyAccess`
-   `fluxCtrl`
-   `if`
-   `else if`
-   `elif`
-   `do`
-   `while`
-   `for`
-   `foreach`
-   `for each`
-   `loopStat`
-   `exp`
-   `list`
-   `tuple`
-   `lstElem`
-   `listIndex`
-   `time`

### Operações Aritméticas

Na tabela seguinte está o conjunto de todas as operações que são
possíveis nesta linguagem:

|  **Símbolo** | **Função** |  **Tipo** |
|:-:|:--|:--|
| + |   Adicão ou Sinal Positivo    |     int
| - |   Subrtração ou Sinal Negativo   |    int
| / |   Divisão          |        int
| % |   Resto da Divisão Inteira    |     int
| * |   Multiplicação    |           int
| == |   Igualdade       |      int, string
| != |   Diferença      |       int, string
| < |    Menor          |         int
| > |    Maior          |         int
| <= |   Menor ou Igual     |         int
| >= |   Maior ou Igual     |         int

Para as estruturas condicionais, temos as palavras reservadas ***and***
e ***or*** que são, respetivamente, a conjunção e a disjunção lógicas.

### Instruções Condicionais

Na linguagem ***Quiz*** existe a permissão de utilização de instruções
condicionais, que alteram o fluxo do programa a desenvolver, escolhendo
assim outros caminhos para o final pretendido.

Para isso, é muito usado um tipo de estrutura com a seguinte sintaxe:

> `if (condition) {...} `
>
> `else if (condition) {...}`
>
> `else {...}`

Quando o programador está a escrever um programa, mais especificamente
uma instrução condicional, não pode aparecer mais do que um **`if`** por
cada instrução. É esta a palavra que expressa o início de uma instrução
condicional. No entanto, pode-se colocar tantos **`if`** quantos aqueles
que o programador assim o desejar. Quando chegar à última condição desse
conjunto de instruções condicionais, terá que ser usado um **`else`**. A
cadeia de instruções condicionais tem que ser expressa obrigatoriamente
sempre por esta ordem aqui descrita.

Dentro de cada **`condition`** do **`if`** ou **`else if`**, pode
existir uma ou mais condições. Neste último caso, podem ser usadas as
palavras reservadas **`and`** e/ou **`or`**, que são, respetivamente, a
conjunção e a disjunção lógica.

### Ciclos

#### While e Do\...While

Para usufruir deste tipo de estrutura condicional, temos que escrever a
seguinte sintaxe:

> `while (condition) {...} `
>
> ou
>
> `do {...} while (condition)`

No primeiro caso, a condição do ciclo **`while`** é definida logo no
início desse mesmo ciclo e é verificada sempre que se chega ao final do
ciclo.

No caso seguinte, a condição é verificada apenas no final do ciclo.

Para que fique claro, caso entrarmos dentro de um dos ciclos, vai
significar que estamos dentro de um novo contexto, isto é, todas as
variáveis que forem criadas dentro desse mesmo ciclo, nunca teremos
acesso a elas fora do ciclo.

#### For

Para usufruir das capacidades do ciclo `for`, temos duas possibilidades:

> `for(num i=0; i<10; i++) {...}`
>
> ou
>
> `for each v in l {...}`

Na primeira sintaxe, é definida uma variável auxiliar para percorrer
todos os valores do intervalo que está indicado. Caso a variável não
sofra alterações como, por exemplo, uma instrução condicional, podemos
presumir que essa mesma variável percorre todos o valores do intervalo
sem qualquer problema que a impeça disso.

É possível uma equivalência entre um intervalo escrito entre parêntesis
e a sua semelhança quando o mesmo intervalo é escrito em linguagem
destino que, no nosso caso, é em **Java**. Um exemplo simples é o que se
sucede:

|  **Configuração entre Parêntesis** | **Equivalência em Java**  |
|:-:|:-:|
|[0,10]|num i=0; i\<=10, i++)

## Conclusão e Resultados 

Através dos conhecimentos que adquirimos ao longo deste exaustivo
semestre, foi possível elaborar este projeto e distribuir de um modo
mais ou menos equilibrado as tarefas que competia cada elemento deste
grupo realizar. As aulas práticas e teóricas foram a principal ajuda
para adquirir os conhecimentos necessários, bem como toda a pesquisa
envolvida.

De uma maneira generalizada, os objetivos foram bem sucedidos, tendo
sido concretizado o objetivo principal deste projeto: a realização de
questionários interativos através de uma linguagem criada por nós,
alunos/futuros engenheiros, para que seja usada por qualquer
utilizador.

Por último, deixamos um agradecimento aos nossos professores, Miguel Oliveira e Silva e Artur
Pereira, pela disponibilidade e por nos terem esclarecido dúvidas pertinentes
durante as aulas práticas presenciais.
