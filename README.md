# Sistema de Registro de Pedidos

##### Este sistema foi criado para registrar pedidos a partr de arquivos XML/JSON limitados a 10 pedidos por arquivo.

Juntamente com o serviço de pedidos também foi criado endpoints para cadastro e consulta de clientes.

### Endponts disponíveis:

- Registro de Pedidos - POST- http://localhost:8080/api/pedidos/receive
- Consulta de Pedidos - POST - http://localhost:8080/api/pedidos/consult
- Registro de Clientes - POST - http://localhost:8080/api/clients/register
- Consulta de Clientes - POST - http://localhost:8080/api/clients/consult

### Especificações

#### - Registro de Pedidos

No registro de pedidos o usuário pode enviar um arquivo no formato JSON ou XML. O sistema faz uma validação relacionada a quantidade de registros que é limitado a 10 registros por arquivo. Todas as regras de adição de percentual de desconto está dentro da procedure chamada no ato do registro. Optei por deixar a regra na procedure pois isso facilita na manutenção de regras no sistema.

Regra: 
Caso a quantidade seja maior que 5 aplicar 5% de desconto no valor total, para quantidades a partir de 10 aplicar 10% de desconto no valor total.

#### - Consulta de Pedidos

Na consulta dos pedidos o sistema permite realizar a consultas por todos os parâmetros ou por número pedido, data cadastro.

#### - Registro de Clientes

O registro de clientes funciona a partir de uma lista de clientes, validando os já existentes, apenas de acordo com a collection disponível na pasta resources dentro do projeto.

#### - Consulta de Clientes

Na consulta de clientes pode ser feita por ID ou nome do cliente de acordo com a collection disponível na pasta resources dentro do projeto.

### Observações
- Os arquivos collection e SQL estão disponíveis na pasta resources dentro da estrutura do projeto
- As dependências utilizadas no projeto funcionam para a versão 9.0.87 do Tomcat
