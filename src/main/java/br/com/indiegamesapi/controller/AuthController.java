package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.dto.request.LoginRequestDTO;
import br.com.indiegamesapi.dto.response.LoginResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Login", description = "Confere email e senha e devolve uma chave de API nova. Envie-a no header X-API-Key dos PUT e DELETE.")
    @ApiResponse(responseCode = "200", description = "Login realizado", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = LoginResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Email ou senha não enviados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "401", description = "Email ou senha inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @PostMapping("/login")
    public ResponseEntity<EntityModel<LoginResponseDTO>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciais", required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class)))
            @RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO login = service.login(dto.email(), dto.password());
        EntityModel<LoginResponseDTO> model = EntityModel.of(login,
                linkTo(methodOn(UserController.class).findById(login.userId())).withRel("user").withType("GET"),
                linkTo(methodOn(AuthController.class).logout(null)).withRel("logout").withType("POST"));
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Logout", description = "Apaga a chave enviada no header X-API-Key; ela para de funcionar.")
    @ApiResponse(responseCode = "204", description = "Logout realizado", content = @Content)
    @ApiResponse(responseCode = "401", description = "X-API-Key ausente ou inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "Chave obtida no login")
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {
        service.logout(apiKey);
        return ResponseEntity.noContent().build();
    }
}
