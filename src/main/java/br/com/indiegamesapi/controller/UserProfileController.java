package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.UserProfileModelAssembler;
import br.com.indiegamesapi.dto.request.UserProfileRequestDTO;
import br.com.indiegamesapi.dto.response.UserProfileResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.UserProfile;
import br.com.indiegamesapi.service.AuthService;
import br.com.indiegamesapi.service.IdempotencyService;
import br.com.indiegamesapi.service.UserProfileService;
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

// NOVO (versionamento): o controller responde em "/api/userprofile" e em "/api/userprofile/v1" (a mesma v1).
@Tag(name = "Perfil do Usuário", description = "Perfis (bio) dos usuários. O perfil é criado junto no POST /api/user.")
@RestController
@RequestMapping({"/api/userprofile", "/api/userprofile/v1"})
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileModelAssembler assembler;
    // NOVO: monta o PagedModel já com os links de paginação (first, prev, self, next, last)
    private final PagedResourcesAssembler<UserProfile> pagedAssembler;
    // NOVO: idempotência dos POST e login dos DELETE
    private final IdempotencyService idempotencyService;
    private final AuthService authService;

    public UserProfileController(UserProfileService service, UserProfileModelAssembler assembler, PagedResourcesAssembler<UserProfile> pagedAssembler,
                         IdempotencyService idempotencyService, AuthService authService){
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
        this.idempotencyService = idempotencyService;
        this.authService = authService;
    }

    @Operation(summary = "Criar um novo perfil de usuário",
            description = "Exige o header X-Idempotency-Key (ex.: um UUID). Reenviar com a mesma chave devolve o perfil de usuário já criado (200) em vez de criar outro.")
    @ApiResponse(responseCode = "201", description = "Perfil de usuário criado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @ApiResponse(responseCode = "200", description = "Repetição: esta X-Idempotency-Key já foi usada, devolve o perfil de usuário criado antes", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos, JSON mal formatado ou header X-Idempotency-Key ausente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PostMapping()
    public ResponseEntity<EntityModel<UserProfileResponseDTO>> create(
            // NOVO (idempotência): header obrigatório; sem ele a resposta é 400
            @Parameter(description = "Valor único deste cadastro (ex.: um UUID novo)", example = "3f2b8c1e-7d4a-4e9b-a6c2-1f0e9d8b7a65")
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novo perfil de usuário", required = true,
                    content = @Content(schema = @Schema(implementation = UserProfileRequestDTO.class)))
            @RequestBody @Valid UserProfileRequestDTO dto){
        // NOVO (idempotência): se esta chave já criou um registro, devolve ele e NÃO cria de novo
        Long createdId = idempotencyService.findCreatedId("userprofile", idempotencyKey);
        if (createdId != null) {
            return ResponseEntity.ok(assembler.toDtoModel(service.findById(createdId)));
        }
        UserProfile userProfile1 = service.create(dto);
        // NOVO (idempotência): guarda qual registro esta chave criou
        idempotencyService.register("userprofile", idempotencyKey, userProfile1.getId());
        return new ResponseEntity<>(assembler.toDtoModel(userProfile1), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar todos os perfis de usuário", description = "Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com todos os perfis de usuário", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = UserProfileResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<UserProfileResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<UserProfile> page = service.findAll(pageable);
        // ALTERADO: o PagedResourcesAssembler converte cada item com o assembler e adiciona os links de paginação
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Buscar perfil de usuário por ID", description = "Retorna o perfil de usuário com os links HATEOAS para os recursos relacionados.")
    @ApiResponse(responseCode = "200", description = "Perfil de usuário encontrado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "ID inválido (não numérico)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Perfil de usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserProfileResponseDTO>> findById(@Parameter(description = "ID do perfil de usuário", example = "1") @PathVariable Long id){
        return ResponseEntity.ok(assembler.toDtoModel(service.findById(id)));
    }

    // NOVO: consulta personalizada (JPQL) pela bio
    @Operation(summary = "Buscar perfis pela bio", description = "Busca perfis cuja bio contém o texto, sem diferenciar maiúsculas de minúsculas. Paginação: page (começa em 0), size (padrão 10) e sort (ex.: name,asc).")
    @ApiResponse(responseCode = "200", description = "Lista paginada com o resultado", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = UserProfileResponseDTO.PAGE_EXAMPLE)))
    @ApiResponse(responseCode = "400", description = "Parâmetro bio não enviado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<UserProfileResponseDTO>>> findByBio(
            @Parameter(description = "Texto a procurar na bio", example = "Backend") @RequestParam String bio,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<UserProfile> page = service.searchByBio(bio, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toDtoModel));
    }

    @Operation(summary = "Atualizar perfil de usuário")
    @ApiResponse(responseCode = "200", description = "Perfil de usuário atualizado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = UserProfileResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou JSON mal formatado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Perfil de usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserProfileResponseDTO>> update(
            @Parameter(description = "ID do perfil de usuário", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do perfil de usuário", required = true,
                    content = @Content(schema = @Schema(implementation = UserProfileRequestDTO.class)))
            @RequestBody @Valid UserProfileRequestDTO dto){
        return ResponseEntity.ok(assembler.toDtoModel(service.update(id, dto)));
    }

    @Operation(summary = "Deletar perfil de usuário",
            description = "Exige login: envie no header X-API-Key a chave obtida em POST /api/auth/login.")
    @ApiResponse(responseCode = "204", description = "Perfil de usuário deletado", content = @Content)
    @ApiResponse(responseCode = "401", description = "X-API-Key ausente ou inválida (faça login em POST /api/auth/login)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "409", description = "Perfil de usuário pertence a um usuário (delete o usuário, que leva o perfil junto) e não pode ser deletado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "404", description = "Perfil de usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Limite de 20 requisições por minuto excedido; aguarde os segundos do header Retry-After", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do perfil de usuário", example = "1") @PathVariable Long id,
            // NOVO (login): chave de API. required = false para o AuthService responder 401 (e não 400) quando faltar
            @Parameter(description = "Chave obtida em POST /api/auth/login")
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        // NOVO (login): sem chave válida, lança UnauthorizedException -> 401
        authService.validate(apiKey);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
