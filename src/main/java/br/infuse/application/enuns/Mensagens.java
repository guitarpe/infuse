package br.infuse.application.enuns;

public enum Mensagens {
    SUCCESS_MSG_IMPORT_FILE("Arquivo importado com sucesso"),
    ERROR_INVALID_FILE("Arquivo inválido"),
    ERROR_READ_JSON_FILE("Erro ao ler o arquivo JSON"),
    ERROR_READ_XML_FILE("Erro ao ler o arquivo XML"),
    ERROR_CONVERT_FILE("Erro ao ler o arquivo de entrada"),
    ERROR_FIELD_CLIENT_EMPTY("O campo 'cliente' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_NAME_EMPTY("O campo 'nome' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_CONTROL_EMPTY("O campo 'controle' é obrigatório! Verifique o arquvivo."),
    ERROR_FIELD_VALUE_EMPTY("O campo 'valor' é obrigatório! Verifique o arquvivo."),
    ERROR_LIMIT_ORDERS("O sistema aceita no mínimo 1 pedido e no máximo 10! Verifique o arquvivo."),
    ERROR_TYPE_FILE_NOT_JSON("Arquivo deve ser do tipo JSON"),
    ERROR_FILE_JSON_EMPTY("Arquivo está vazio"),
    ERROR_FILE_JSON_NULL("Arquivo está null"),
    ERROR_CLIENT_NOT_FOUND("Cliente não encontrado! Verifique o arquvivo."),
    ERROR_LIST_NOT_FOUND("Não foram encontrados registros com esses parâmetros"),
    ERROR_SAVE_ORDER("Erro ao salvar pedido "),
    ERROR_SAVE_CLIENT("Erro ao salvar cliente "),
    ERROR_LIST_ORDERS("Erro ao listar pedidos "),
    ERROR_LIST_CLIENTS("Erro ao listar clientes "),
    ERROR_CLIENT_EXISTS("Cliente já cadastrado"),
    ERROR_NOT_CLIENT_SAVE("Nenhum cliente foi salvo, verifique os dados de entrada"),
    NOT_SAVE_ORDERS("Nenhum pedido foi salvo, verifique os dados de entrada"),
    SUCCESS_SAVE_ORDER("Pedidos salvo com sucesso"),
    SUCCESS_SAVE_CLIENT("Clientes salvo com sucesso"),
    SUCCESS_LIST_ORDERS("Pedidos listados com sucesso"),
    SUCCESS_LIST_CLIENTS("Clientes listados com sucesso"),
    ERROR_MSG_NO_PARAMS("Pelo menos um parâmetro 'nome' ou 'id' deve ser fornecido.");

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
