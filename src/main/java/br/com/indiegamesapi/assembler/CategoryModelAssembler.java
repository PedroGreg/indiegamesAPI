package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.CategoryController;
import br.com.indiegamesapi.controller.CategoryV2Controller;
import br.com.indiegamesapi.controller.GameController;
import br.com.indiegamesapi.dto.response.CategoryResponseDTO;
import br.com.indiegamesapi.dto.response.CategoryV2ResponseDTO;
import br.com.indiegamesapi.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    public EntityModel<Category> toModel(Category category){
        return EntityModel.of(category, links(category));
    }

    // Mesmos links, mas com o CategoryResponseDTO no corpo (é o que os controllers usam)
    public EntityModel<CategoryResponseDTO> toDtoModel(Category category){
        return EntityModel.of(new CategoryResponseDTO(category), links(category));
    }

    // NOVO (versionamento): representação da v2, com a quantidade de games
    public EntityModel<CategoryV2ResponseDTO> toV2Model(Category category, long gamesCount){
        return EntityModel.of(new CategoryV2ResponseDTO(category, gamesCount),
                linkTo(methodOn(CategoryV2Controller.class).findById(category.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(CategoryV2Controller.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(CategoryController.class).findById(category.getId())).withRel("v1").withType("GET"));
    }

    // Links do HATEOAS. create(null, null) e delete(id, null): os parâmetros extras são os headers
    // (X-Idempotency-Key e X-API-Key), que não entram na URL, então passamos null.
    private List<Link> links(Category category){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CategoryController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).findById(category.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).findByName(category.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(CategoryController.class).update(category.getId(), null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(CategoryController.class).delete(category.getId(), null)).withRel("delete").withType("DELETE"));
        // NOVO: navegação para os games desta categoria e para a mesma categoria na v2
        links.add(linkTo(methodOn(GameController.class).findByCategory(category.getId(), Pageable.unpaged())).withRel("games").withType("GET"));
        links.add(linkTo(methodOn(CategoryV2Controller.class).findById(category.getId())).withRel("v2").withType("GET"));
        return links;
    }
}
