package com.indiegames.assembler;

import com.indiegames.controller.GameController;
import com.indiegames.model.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
    public EntityModel<Game> toModel(Game game){
        return EntityModel.of(game,
                linkTo(methodOn(GameController.class).findAll(Pageable.unpaged())).withRel("games").withType("GET"),
                linkTo(methodOn(GameController.class).findById(game.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(GameController.class).findByName(game.getName(), Pageable.unpaged())).withRel("find by name").withType("GET"),
                linkTo(methodOn(GameController.class).create(game)).withRel("create").withType("POST"),
                linkTo(methodOn(GameController.class).update(game.getId(), game)).withRel("update").withType("PUT"),
                linkTo(methodOn(GameController.class).delete(game.getId())).withRel("delete").withType("DELETE")
                );
    }
}
