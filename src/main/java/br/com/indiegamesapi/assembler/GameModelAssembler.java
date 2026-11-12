package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.CategoryController;
import br.com.indiegamesapi.controller.CategoryV2Controller;
import br.com.indiegamesapi.controller.DeveloperController;
import br.com.indiegamesapi.controller.GameController;
import br.com.indiegamesapi.controller.GameV2Controller;
import br.com.indiegamesapi.dto.response.GameResponseDTO;
import br.com.indiegamesapi.dto.response.GameV2ResponseDTO;
import br.com.indiegamesapi.model.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
    public EntityModel<Game> toModel(Game game){
        return EntityModel.of(game, links(game));
    }

    public EntityModel<GameResponseDTO> toDtoModel(Game game){
        return EntityModel.of(new GameResponseDTO(game), links(game));
    }

    public EntityModel<GameV2ResponseDTO> toV2Model(Game game){
        return EntityModel.of(new GameV2ResponseDTO(game),
                linkTo(methodOn(GameV2Controller.class).findAll(Pageable.unpaged())).withRel("games").withType("GET"),
                linkTo(methodOn(GameV2Controller.class).findById(game.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(GameController.class).findByName(game.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"),
                linkTo(methodOn(GameV2Controller.class).create(null, null)).withRel("create").withType("POST"),
                linkTo(methodOn(GameV2Controller.class).update(game.getId(), null, null)).withRel("update").withType("PUT"),
                linkTo(methodOn(GameController.class).delete(game.getId(), null)).withRel("delete").withType("DELETE"),
                linkTo(methodOn(DeveloperController.class).findById(game.getDeveloper().getId())).withRel("developer").withType("GET"),
                linkTo(methodOn(CategoryV2Controller.class).findById(game.getCategory().getId())).withRel("category").withType("GET"),
                linkTo(methodOn(GameController.class).findByStatus(game.getStatus(), Pageable.unpaged())).withRel("same-status").withType("GET"),
                linkTo(methodOn(GameController.class).findById(game.getId())).withRel("v1").withType("GET"));
    }

    private List<Link> links(Game game){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(GameController.class).findAll(Pageable.unpaged())).withRel("games").withType("GET"));
        links.add(linkTo(methodOn(GameController.class).findById(game.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(GameController.class).findByName(game.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(GameController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(GameController.class).update(game.getId(), null, null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(GameController.class).delete(game.getId(), null)).withRel("delete").withType("DELETE"));
        links.add(linkTo(methodOn(DeveloperController.class).findById(game.getDeveloper().getId())).withRel("developer").withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).findById(game.getCategory().getId())).withRel("category").withType("GET"));
        links.add(linkTo(methodOn(GameV2Controller.class).findById(game.getId())).withRel("v2").withType("GET"));
        return links;
    }
}
