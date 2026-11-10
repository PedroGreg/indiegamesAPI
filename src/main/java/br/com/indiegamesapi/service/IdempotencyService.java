package br.com.indiegamesapi.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// NOVO: idempotência dos POST (header X-Idempotency-Key).
//
// Ideia: o cliente manda um valor único em cada cadastro (ex.: um UUID). Se ele reenviar o MESMO POST
// com a MESMA chave (ex.: a internet caiu e ele não sabe se deu certo), a API não cria de novo:
// devolve o registro que já tinha criado na primeira vez.
//
// Aqui guardamos, para cada chave já usada, o ID do registro que ela criou:
//     "category:3f2b8c1e-..."  ->  7   (a categoria de id 7 foi criada com essa chave)
// O nome do recurso vai junto na chave do Map para a mesma X-Idempotency-Key não se misturar entre endpoints.
//
// ConcurrentHashMap é um Map seguro para várias requisições ao mesmo tempo.
// Fica em memória (some ao reiniciar a API), assim como o banco H2 deste projeto.
@Service
public class IdempotencyService {

    private final Map<String, Long> processedKeys = new ConcurrentHashMap<>();

    // Devolve o ID criado anteriormente com esta chave, ou null se a chave é nova
    public Long findCreatedId(String resource, String idempotencyKey) {
        return processedKeys.get(resource + ":" + idempotencyKey);
    }

    // Registra que esta chave criou o registro com este ID
    public void register(String resource, String idempotencyKey, Long createdId) {
        processedKeys.put(resource + ":" + idempotencyKey, createdId);
    }
}
