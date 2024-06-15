# Sistema de Registro de Pedidos

##### Este sistema foi criado para registrar pedidos a partr de arquivos XML/JSON limitados a 10 pedidos por arquivo.

Juntamente com o serviço de pedidos também foi criado endpoints para cadastro e consulta de clientes.

### 1 - Endponts disponíveis:

1) Cadastrar Pedidos - **POST**- http://localhost:8080/api/pedidos/registrar
2) Atualizar Pedido - **PUT** - http://localhost:8080/api/pedidos/123/atualizar
3) Consultar Pedidos - **GET** - http://localhost:8080/api/pedidos
4) Buscar Pedido - **GET** - http://localhost:8080/api/pedidos/123
5) Deletar Pedido - **DELETE** - http://localhost:8080/api/pedidos/123/deletar

6) Cadastrar Cliente - **POST** - http://localhost:8080/api/clientes/registrar
7) Atualizar Cliente - **PUT** - http://localhost:8080/api/clientes/1/atualizar
8) Listar Clientes - **GET** - http://localhost:8080/api/clientes
9) Consultar Cliente - **GET** - http://localhost:8080/api/clientes/1
10) Deletar Cliente - **DELETE** - http://localhost:8080/api/clientes/1/deletar

### 2 - Especificações

#### 2.1 - **POST** - Cadastrar Pedidos

No registro de pedidos o usuário pode enviar um arquivo no formato JSON ou XML. O sistema faz uma validação relacionada a quantidade de registros que é limitado a 10 registros por arquivo. Todas as regras de adição de percentual de desconto está dentro da procedure chamada no ato do registro.

##### 2.1.1 - Formatos dos arquivos
- JSON
```json
{
    "pedidos": [
            {
                "cliente": 3,
                "controle": 789,
                "produto": "produto 3",
                "valor": 11.5,
                "quantidade": 0,
                "registro": ""
            },
            {
                "cliente": 4,
                "controle": 101112,
                "produto": "produto 4",
                "valor": 12.2,
                "quantidade": 11,
                "registro": "2024-04-10 10:30:30"
            }
        ]
}
```
- XML
```xml
<pedidos>
    <pedidoDTO>
        <cliente>1</cliente>
        <controle>123</controle>
        <produto>produto 1</produto>
        <valor>10.0</valor>
        <quantidade>6</quantidade>
        <registro>2024-04-10 10:30:30</registro>
    </pedidoDTO>
    <pedidoDTO>
        <cliente>2</cliente>
        <controle>456</controle>
        <produto>produto 2</produto>
        <valor>11.0</valor>
        <quantidade>11</quantidade>
        <registro></registro>
    </pedidoDTO>
</pedidos>
```
##### 2.1.2 - Regras: 
- Caso a quantidade seja maior que 5 aplicar 5% de desconto no valor total, para quantidades a partir de 10 aplicar 10% de desconto no valor total.
- Caso não seja informada a datado pedidoDTO o sistema irá registrar a data atual.
- Caso não seja informada a quantidade o sistema irá adicionar o valor 1.

#### 2.2 - **PUT** - Atualizar Pedido

Atualiza um pedido já cadastrado

http://localhost:8080/api/pedidos/123/atualizar
```json
{
  "cliente": 3,
  "controle": 789,
  "produto": "produto 3",
  "valor": 11.5,
  "quantidade": 0,
  "registro": ""
}
```

#### 2.3 - **GET** - Consultar Pedidos

Na consulta dos pedidos o sistema permite realizar a consultas por todos os parâmetros ou por número pedido, data cadastro e de forma paginada.

##### 2.3.1 - Consulta por todos os parâmetros:
http://localhost:8080/api/pedidos?page=1&size=10?&pedido=789&data=2024-04-10&qtde=1&valor=0.00&produto=produto%3&cliente=3

##### 2.3.2 - Consulta por num. pedido ou data de registro:
http://localhost:8080/api/pedidos?page=1&size=10&pedido=789&data=2024-04-10

#### 2.4 - **GET** - Buscar Pedido

Realiza a busca de um pedido específico por seu número de controle

http://localhost:8080/api/pedidos/123

#### 2.5 - **DELETE** - Deletar Pedido

Exclui um pedido já cadastrado por seu número de controle

http://localhost:8080/api/pedidos/123/deletar

#### 2.6 - **POST** - Cadastrar Clientes

O registro de clientes funciona a partir de uma lista de clientes, validando os já existentes, apenas de acordo com a collection disponível na pasta resources dentro do projeto.

##### 2.6.1 - Formato de corpor no cadastro de clientes
```json
{
  "nome": "cliente 6",
  "email": "cliente6@email.com",
  "telefone": "(44) 00000-0000"
}
```
#### 2.7 - **PUT** - Atualizar Cliente

Atualiza um cliente já cadastrado

http://localhost:8080/api/clientes/1/atualizar

```json
{
  "nome": "cliente 6",
  "email": "cliente6@email.com",
  "telefone": "(44) 00000-0000"
}
```

#### 2.8 - **GET** - Listar Clientes

Lista todos os clientes cadastrados de forma paginada.

http://localhost:8080/api/clientes?page=1&size=10

#### 2.9 - **GET** - Consultar Clientes

Realiza a consulta do cliente por seu id.

http://localhost:8080/api/clientes/3

#### 2.10 - **DELETE** - Deletar Cliente

Realiza a exclusão de um clienmte já cadastrado.

http://localhost:8080/api/clientes/3/deletar


### 3 - Observações
- Os arquivos collection e SQL estão disponíveis na pasta resources dentro da estrutura do projeto
- As dependências utilizadas no projeto funcionam para a versão 9.0.87 do Tomcat
