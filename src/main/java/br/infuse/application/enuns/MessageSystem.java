package br.infuse.application.enuns;

public enum MessageSystem {
    ERROR_INVALID_FILE("Arquivo inválido"),
    ERROR_READ_JSON_FILE("Erro ao ler o arquivo JSON"),
    ERROR_FIELD_CLIENT_EMPTY("O campo 'cliente' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_NAME_EMPTY("O campo 'nome' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_CONTROL_EMPTY("O campo 'controle' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_VALUE_EMPTY("O campo 'valor' é obrigatório! Verifique o arquvivo."),
    ERROR_LIMIT_ORDERS("O sistema aceita no mínimo 1 pedido e no máximo 10! Verifique o arquvivo."),
    ERROR_CLIENT_NOT_FOUND("Cliente não encontrado! Verifique o arquvivo."),
    ERROR_LIST_NOT_FOUND("Não foram encontrados registros com esses parâmetros"),
    ERROR_SAVE_ORDER("Erro ao salvar pedido"),
    ERROR_SAVE_CLIENT("Erro ao salvar pedido"),
    ERROR_LIST_ORDERS("Erro ao listar pedidos"),
    ERROR_LIST_CLIENTS("Erro ao listar clients"),
    ERROR_CLIENT_EXISTS("Cliente já cadastrado"),
    SUCCESS_SAVE_ORDER("Pedido salvo com sucesso"),
    SUCCESS_SAVE_CLIENT("Cliente salvo com sucesso"),
    SUCCESS_LIST_ORDERS("Pedidos listados com sucesso"),
    SUCCESS_LIST_CLIENTS("Pedidos listados com sucesso")
    ;

    private final String value;

    MessageSystem(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageSystem fromValue(String v) {
        for (MessageSystem c: MessageSystem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
