package br.infuse.application.enuns;

public enum Mensagens {
    //CLIENTES
    CLIENT_SUCCESS_SAVE("Cliente salvo com sucesso"),
    CLIENT_SUCCESS_UPDT("Cliente atualizado com sucesso"),
    CLIENT_SUCCESS_DEL("Cliente deletado com sucesso"),
    CLIENT_SUCCESS_LIST("Clientes listados com sucesso"),
    CLIENT_SUCCESS_FOUND("Cliente encontrado com sucesso"),
    CLIENT_ERROR_SAVE("Erro ao salvar cliente "),
    CLIENT_ERROR_UPDT("Erro ao atualizar cliente "),
    CLIENT_ERROR_DEL("Erro ao deletar cliente "),
    CLIENT_ERROR_LIST("Erro ao listar clientes "),
    CLIENT_ERROR_FOUND("Cliente não encontrado"),
    //PEDIDOS
    ORDER_SUCCESS_SAVE("Pedido salvo com sucesso"),
    ORDER_SUCCESS_UPDT("Pedido atualizado com sucesso"),
    ORDER_SUCCESS_LIST("Pedidos listados com sucesso"),
    ORDER_SUCCESS_FOUND("Pedido encontrado com sucesso"),
    ORDER_SUCCESS_DEL("Pedido deletado com sucesso"),
    ORDER_ERROR_SAVE("Erro ao salvar pedido "),
    ORDER_ERROR_UPDT("Erro ao atualizar pedido "),
    ORDER_ERROR_DEL("Erro ao deletar pedido "),
    ORDER_ERROR_LIST("Pedidos não encontrados "),
    ORDER_ERROR_FOUND("Pedido não encontrado"),
    //ARQUIVO
    FILE_ERROR_READ_XML("Erro ao ler arquivo XML: "),
    FILE_ERROR_PROCCESS("Erro ao processar arquivo: {}"),
    FILE_ERROR_N_ORDERS_INVALID("Número de pedidos inválido."),
    FILE_ERROR_INVALID_FIELDS("Campos do pedido inválidos."),
    FILE_ERROR_JSON_EMPTY("Arquivo JSON vazio."),
    FILE_ERROR_AMOUNT_ORD_INVALID("Número de pedidos inválido."),
    FILE_ERROR_READ_JSON_FILE("Erro ao ler o arquivo JSON"),
    FILE_ERROR_FIELD_ORD_INVALID("Campos do pedido inválido."),
    FILE_ERROR_INVALID_FILE("Arquivo inválido"),
    FILE_ERROR_CONVERT_FILE("Erro ao ler o arquivo de entrada"),
    FILE_ERROR_FILE_JSON_EMPTY("Arquivo está vazio");

    private final String value;

    Mensagens(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Mensagens fromValue(String v) {
        for (Mensagens c: Mensagens.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
