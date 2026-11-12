package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.PlatformController;
import br.com.indiegamesapi.controller.GameController;
import br.com.indiegamesapi.dto.response.PlatformResponseDTO;
import br.com.indiegamesapi.model.Platform;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PlatformModelAssembler implements RepresentationModelAssembler<Platform, EntityModel<Platform>> {
    public EntityModel<Platform> toModel(Platform platform){
        return EntityModel.of(platform, links(platform));
    }

    public EntityModel<PlatformResponseDTO> toDtoModel(Platform platform){
        return EntityModel.of(new PlatformResponseDTO(platform), links(platform));
    }

    private List<Link> links(Platform platform){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(PlatformController.class).findAll(Pageable.unpaged())).withRel("platforms").withType("GET"));
        links.add(linkTo(methodOn(PlatformController.class).findById(platform.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(PlatformController.class).findByName(platform.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(PlatformController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(PlatformController.class).update(platform.getId(), null, null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(PlatformController.class).delete(platform.getId(), null)).withRel("delete").withType("DELETE"));
        links.add(linkTo(methodOn(GameController.class).findByPlatform(platform.getId(), Pageable.unpaged())).withRel("games").withType("GET"));
        return links;
    }
}
