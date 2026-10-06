package com.apigamesinidie.controller;

import com.apigamesinidie.assembler.CategoryModelAssembler;
import com.apigamesinidie.model.Category;
import com.apigamesinidie.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Requisições de Categorias")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService service;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryService service, CategoryModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Tag(name = "Criar", description = "Criar um novo")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Categoria criada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Category>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nova categoria",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Category.class),
                            examples = @ExampleObject(value = "{ \"name\": \"RPG\" }"))
            )
            @RequestBody @Valid Category category){
        Category category1 = service.create(category);
        EntityModel<Category> createCategory = assembler.toModel(category1);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos", description = "Buscar todos")
    @Operation(summary = "Buscar todas(GET)")
    @ApiResponse(responseCode = "200", description = "Categorias", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Category>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<Category> pageCategory = service.findAll(pageable);
        PagedModel<EntityModel<Category>> allCategory = PagedModel.of(
                pageCategory.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageCategory.getSize(), pageCategory.getNumber(), pageCategory.getTotalElements(), pageCategory.getTotalPages())
                );
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID", description = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Categoria", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Category>> findById(@PathVariable Long id){
        Category category1 = service.findById(id);
        EntityModel<Category> findIdCategory= assembler.toModel(category1);
        return new ResponseEntity<>(findIdCategory, HttpStatus.OK);
    }

    @Tag(name = "Buscar por nome", description = "Buscar por nome")
    @Operation(summary = "Buscar por nome(GET)")
    @ApiResponse(responseCode = "200", description = "Categorias", content = @Content)
    @ApiResponse(responseCode = "204", description = "Categorias", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Category>>> findByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable){
        Page<Category> pageCategory = service.findByName(name, pageable);
        PagedModel<EntityModel<Category>> allNameCategory = PagedModel.of(
                pageCategory.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageCategory.getSize(), pageCategory.getNumber(), pageCategory.getTotalElements(), pageCategory.getTotalPages())
        );
        return new ResponseEntity<>(allNameCategory, HttpStatus.OK);
    }

    @Tag(name = "Atualizar", description = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Category>> update(@PathVariable Long id,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Nova categoria",
                                                                required = true,
                                                                content = @Content(mediaType = "application/json",
                                                                        schema= @Schema(implementation = Category.class),
                                                                        examples = @ExampleObject(value = "{ \"name\": \"RPG\" }"))
                                                        )
                                                        @RequestBody @Valid Category category){
        Category category1 = service.update(id, category);
        EntityModel<Category>  updateCategory = assembler.toModel(category1);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @Tag(name = "Deletar", description = "Deletar por ID")
    @Operation(summary = "Deletar(DELETE)")
    @ApiResponse(responseCode = "204", description = "Categoria deletada", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
