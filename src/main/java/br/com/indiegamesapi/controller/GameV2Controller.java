package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.GameModelAssembler;
import br.com.indiegamesapi.dto.request.GameV2RequestDTO;
import br.com.indiegamesapi.dto.response.GameV2ResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.Game;
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
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "v2 - Games")
@RestController
@RequestMapping("/api/game/v2")
public class GameV2Controller {

    private final GameService service;
    private final GameModelAssembler assembler;
    private final PagedResourcesAssembler<Game> pagedAssembler =
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);
    private final IdempotencyService idempotencyService;
    private final AuthService authService;

    public GameV2Controller(GameService service, GameModelAssembler assembler, IdempotencyService idempotencyService, AuthService authService) {
        this.service = service;
        this.assembler = assembler;
        this.idempotencyService = idempotencyService;
        this.authService = authService;
    }

    @Operation(summary = "Criar um novo game (v2, com conquistas)",
            description = "Igual ao POST da v1, mas recebe também o totalAchievements (quantidade total de conquistas). " +
                    "Exige o header X-Idempotency-Key: reenviar com a mesma chave devolve o game já criado (200).")
    @ApiResponse(responseCode = "201", description = "Game criado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameV2ResponseDTO.class)))
    @ApiResponse(responseCode = "200", description = "Repetição: esta X-Idempotency-Key já foi usada, devolve o game criado antes", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameV2ResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos, JSON mal formatado ou header X-Idempotency-Key ausente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "categoryId, developerId ou algum platformId não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Muitas requisições. Tente novamente em 30s\"}")))
    @PostMapping()
    public ResponseEntity<EntityModel<GameV2ResponseDTO>> create(
            @Parameter(description = "Valor único deste cadastro (ex.: um UUID novo)", example = "3f2b8c1e-7d4a-4e9b-a6c2-1f0e9d8b7a65")
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novo game", required = true,
                    content = @Content(schema = @Schema(implementation = GameV2RequestDTO.class)))
            @RequestBody @Valid GameV2RequestDTO dto) {
        Long createdId = idempotencyService.findCreatedId("game-v2", idempotencyKey);
        if (createdId != null) {
            return ResponseEntity.ok(assembler.toV2Model(service.findById(createdId)));
        }
        Game game = service.createV2(dto);
        idempotencyService.register("game-v2", idempotencyKey, game.getId());
        return new ResponseEntity<>(assembler.toV2Model(game), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar todos os games (v2)", description = "Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameV2ResponseDTO.PAGE_EXAMPLE)))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<GameV2ResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        Page<Game> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toV2Model));
    }

    @Operation(summary = "Buscar game por ID (v2)")
    @ApiResponse(responseCode = "200", description = "Game no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameV2ResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "ID inválido (não numérico)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GameV2ResponseDTO>> findById(@Parameter(description = "ID do game", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(assembler.toV2Model(service.findById(id)));
    }

    @Operation(summary = "Atualizar game (v2, com conquistas)",
            description = "Substitui todos os dados do game, inclusive o totalAchievements. Exige login: envie no header X-API-Key a chave obtida em POST /api/auth/login.")
    @ApiResponse(responseCode = "200", description = "Game atualizado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameV2ResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou JSON mal formatado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "401", description = "X-API-Key ausente ou inválida (faça login em POST /api/auth/login)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado (ou categoryId, developerId ou algum platformId não existe)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<GameV2ResponseDTO>> update(
            @Parameter(description = "ID do game", example = "1") @PathVariable Long id,
            @Parameter(description = "Chave obtida em POST /api/auth/login")
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do game", required = true,
                    content = @Content(schema = @Schema(implementation = GameV2RequestDTO.class)))
            @RequestBody @Valid GameV2RequestDTO dto) {
        authService.validate(apiKey);
        return ResponseEntity.ok(assembler.toV2Model(service.updateV2(id, dto)));
    }
}
