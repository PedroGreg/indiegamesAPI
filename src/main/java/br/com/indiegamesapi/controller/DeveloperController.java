package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.DeveloperModelAssembler;
import br.com.indiegamesapi.dto.request.DeveloperRequestDTO;
import br.com.indiegamesapi.dto.response.DeveloperResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.Developer;
import br.com.indiegamesapi.service.AuthService;
import br.com.indiegamesapi.service.DeveloperService;
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

// NOVO (versionamento): o controller responde em "/api/developer" e em "/api/developer/v1" (a mesma v1).
@Tag(name = "Desenvolvedores", description = "Estúdios ou pessoas que desenvolvem os games.")
@RestController
@RequestMapping({"/api/developer", "/api/developer/v1"})
public class DeveloperController {

    private final DeveloperService service;
    private final DeveloperModelAssembler assembler;
    // NOVO: monta o PagedModel já com os links de paginação (first, prev, self, next, last)
    private final PagedResourcesAssembler<Developer> pagedAssembler;
    // NOVO: idempotência dos POST e login dos DELETE
    private final IdempotencyService idempotencyService;
    private final AuthService authService;

    public DeveloperController(DeveloperService service, DeveloperModelAssembler assembler, PagedResourcesAssembler<Developer> pagedAssembler,
                         IdempotencyService idempotencyService, AuthService authService){
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
        this.idempotencyService = idempotencyService;
        this.authService = authService;
    }

    @Operation(summary = "Criar um novo desenvolvedor",
            description = "Exige o header X-Idempotency-Key (ex.: um UUID). Reenviar com a mesma chave devolve o desenvolvedor já criado (200) em vez de criar outro.")
    @ApiResponse(responseCode = "201", description = "Desenvolvedor criado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DeveloperResponseDTO.class)))
    @ApiResponse(responseCode = "200", description = "Repetição: esta X-Idempotency-Key já foi usada, devolve o desenvolvedor criado antes", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DeveloperResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos, JSON mal formatado ou header X-Idempotency-Key ausente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PostMapping()
    public ResponseEntity<EntityModel<DeveloperResponseDTO>> create(
            // NOVO (idempotência): header obrigatório; sem ele a resposta é 400
            @Parameter(description = "Valor único deste cadastro (ex.: um UUID novo)", example = "3f2b8c1e-7d4a-4e9b-a6c2-1f0e9d8b7a65")
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novo desenvolvedor", required = true,
                    content = @Content(schema = @Schema(implementation = DeveloperRequestDTO.class)))
            @RequestBody @Valid DeveloperRequestDTO dto){
        // NOVO (idempotência): se esta chave já criou um registro, devolve ele e NÃO cria de novo
        Long createdId = idempotencyService.findCreatedId("developer", idempotencyKey);
        if (createdId != null) {
            return ResponseEntity.ok(assembler.toDtoModel(service.findById(createdId)));
        }
        Developer developer1 = service.create(dto);
        // NOVO (idempotência): guarda qual registro esta chave criou
        idempotencyService.register("developer", idempotencyKey, developer1.getId());
        return new ResponseEntity<>(assembler.toDtoModel(developer1), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar todos os desenvolvedores", description = "Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com todos os desenvolvedores", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = DeveloperResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<DeveloperResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Developer> page = service.findAll(pageable);
        // ALTERADO: o PagedResourcesAssembler converte cada item com o assembler e adiciona os links de paginação
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar desenvolvedor por ID", description = "Retorna o desenvolvedor com os links HATEOAS para os recursos relacionados.")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor encontrado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DeveloperResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "ID inválido (não numérico)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DeveloperResponseDTO>> findById(@Parameter(description = "ID do desenvolvedor", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(assembler.toDtoModel(service.findById(id)));
    }

    @Operation(summary = "Buscar desenvolvedor por nome", description = "Busca desenvolvedores cujo nome contém o texto, sem diferenciar maiúsculas de minúsculas. Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com o resultado", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = DeveloperResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro name não enviado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<DeveloperResponseDTO>>> findByName(
            @Parameter(description = "Texto a procurar no nome", example = "Toby") @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Developer> page = service.findByName(name, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Atualizar desenvolvedor")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor atualizado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DeveloperResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou JSON mal formatado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DeveloperResponseDTO>> update(
            @Parameter(description = "ID do desenvolvedor", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do desenvolvedor", required = true,
                    content = @Content(schema = @Schema(implementation = DeveloperRequestDTO.class)))
            @RequestBody @Valid DeveloperRequestDTO dto){
        return ResponseEntity.ok(assembler.toDtoModel(service.update(id, dto)));
    }

    @Operation(summary = "Deletar desenvolvedor",
            description = "Exige login: envie no header X-API-Key a chave obtida em POST /api/auth/login.")
    @ApiResponse(responseCode = "204", description = "Desenvolvedor deletado", content = @Content)
    @ApiResponse(responseCode = "401", description = "X-API-Key ausente ou inválida (faça login em POST /api/auth/login)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "409", description = "Desenvolvedor está em uso por um ou mais games e não pode ser deletado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do desenvolvedor", example = "1") @PathVariable Long id,
            // NOVO (login): chave de API. required = false para o AuthService responder 401 (e não 400) quando faltar
            @Parameter(description = "Chave obtida em POST /api/auth/login")
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        // NOVO (login): sem chave válida, lança UnauthorizedException -> 401
        authService.validate(apiKey);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
