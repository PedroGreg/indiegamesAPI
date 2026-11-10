package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.GameModelAssembler;
import br.com.indiegamesapi.dto.request.GameRequestDTO;
import br.com.indiegamesapi.dto.response.GameResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import br.com.indiegamesapi.service.AuthService;
import br.com.indiegamesapi.service.GameService;
import br.com.indiegamesapi.service.IdempotencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// NOVO (versionamento): o controller responde em "/api/game" e em "/api/game/v1" (a mesma v1). A v2 está em GameV2Controller.
@Tag(name = "Games", description = "Games indie com desenvolvedor, categoria, plataformas e status.")
@RestController
@RequestMapping({"/api/game", "/api/game/v1"})
public class GameController {

    private final GameService service;
    private final GameModelAssembler assembler;
    // NOVO: monta o PagedModel já com os links de paginação (first, prev, self, next, last)
    private final PagedResourcesAssembler<Game> pagedAssembler;
    // NOVO: idempotência dos POST e login dos DELETE
    private final IdempotencyService idempotencyService;
    private final AuthService authService;

    public GameController(GameService service, GameModelAssembler assembler, PagedResourcesAssembler<Game> pagedAssembler,
                         IdempotencyService idempotencyService, AuthService authService){
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
        this.idempotencyService = idempotencyService;
        this.authService = authService;
    }

    @Operation(summary = "Criar um novo game",
            description = "Exige o header X-Idempotency-Key (ex.: um UUID). Reenviar com a mesma chave devolve o game já criado (200) em vez de criar outro.")
    @ApiResponse(responseCode = "201", description = "Game criado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameResponseDTO.class)))
    @ApiResponse(responseCode = "200", description = "Repetição: esta X-Idempotency-Key já foi usada, devolve o game criado antes", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos, JSON mal formatado ou header X-Idempotency-Key ausente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "categoryId, developerId ou algum platformId não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PostMapping()
    public ResponseEntity<EntityModel<GameResponseDTO>> create(
            // NOVO (idempotência): header obrigatório; sem ele a resposta é 400
            @Parameter(description = "Valor único deste cadastro (ex.: um UUID novo)", example = "3f2b8c1e-7d4a-4e9b-a6c2-1f0e9d8b7a65")
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novo game", required = true,
                    content = @Content(schema = @Schema(implementation = GameRequestDTO.class)))
            @RequestBody @Valid GameRequestDTO dto){
        // NOVO (idempotência): se esta chave já criou um registro, devolve ele e NÃO cria de novo
        Long createdId = idempotencyService.findCreatedId("game", idempotencyKey);
        if (createdId != null) {
            return ResponseEntity.ok(assembler.toDtoModel(service.findById(createdId)));
        }
        Game game1 = service.create(dto);
        // NOVO (idempotência): guarda qual registro esta chave criou
        idempotencyService.register("game", idempotencyKey, game1.getId());
        return new ResponseEntity<>(assembler.toDtoModel(game1), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar todos os games", description = "Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com todos os games", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findAll(pageable);
        // ALTERADO: o PagedResourcesAssembler converte cada item com o assembler e adiciona os links de paginação
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar game por ID", description = "Retorna o game com os links HATEOAS para os recursos relacionados.")
    @ApiResponse(responseCode = "200", description = "Game encontrado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "ID inválido (não numérico)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GameResponseDTO>> findById(@Parameter(description = "ID do game", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(assembler.toDtoModel(service.findById(id)));
    }

    @Operation(summary = "Buscar game por nome", description = "Busca games cujo nome contém o texto, sem diferenciar maiúsculas de minúsculas. Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com o resultado", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro name não enviado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findByName(
            @Parameter(description = "Texto a procurar no nome", example = "Undertale") @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findByName(name, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    // NOVO: endpoints das consultas personalizadas (GameRepository)

    @Operation(summary = "Buscar games por status", description = "Lista os games com o status informado. Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada de games", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/status/{status}")
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findByStatus(
            @Parameter(description = "DEVELOPMENT, EARLY_ACCESS ou RELEASED", example = "RELEASED") @PathVariable GameStatus status,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findByStatus(status, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar games de um desenvolvedor", description = "Consulta JPQL (One-to-Many). Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada de games", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/developer/{developerId}")
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findByDeveloper(
            @Parameter(description = "ID do desenvolvedor", example = "1") @PathVariable Long developerId,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findByDeveloper(developerId, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar games de uma categoria", description = "Consulta JPQL (One-to-Many). Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada de games", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findByCategory(
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long categoryId,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findByCategory(categoryId, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar games de uma plataforma", description = "Consulta JPQL com JOIN (Many-to-Many). Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada de games", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Plataforma não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/platform/{platformId}")
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> findByPlatform(
            @Parameter(description = "ID da plataforma", example = "1") @PathVariable Long platformId,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> page = service.findByPlatform(platformId, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Atualizar game")
    @ApiResponse(responseCode = "200", description = "Game atualizado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou JSON mal formatado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GameResponseDTO>> update(
            @Parameter(description = "ID do game", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do game", required = true,
                    content = @Content(schema = @Schema(implementation = GameRequestDTO.class)))
            @RequestBody @Valid GameRequestDTO dto){
        return ResponseEntity.ok(assembler.toDtoModel(service.update(id, dto)));
    }

    @Operation(summary = "Deletar game",
            description = "Exige login: envie no header X-API-Key a chave obtida em POST /api/auth/login.")
    @ApiResponse(responseCode = "204", description = "Game deletado", content = @Content)
    @ApiResponse(responseCode = "401", description = "X-API-Key ausente ou inválida (faça login em POST /api/auth/login)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do game", example = "1") @PathVariable Long id,
            // NOVO (login): chave de API. required = false para o AuthService responder 401 (e não 400) quando faltar
            @Parameter(description = "Chave obtida em POST /api/auth/login")
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        // NOVO (login): sem chave válida, lança UnauthorizedException -> 401
        authService.validate(apiKey);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
