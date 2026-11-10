package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.DeveloperController;
import br.com.indiegamesapi.controller.GameController;
import br.com.indiegamesapi.dto.response.DeveloperResponseDTO;
import br.com.indiegamesapi.model.Developer;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DeveloperModelAssembler implements RepresentationModelAssembler<Developer, EntityModel<Developer>> {
    public EntityModel<Developer> toModel(Developer developer){
        return EntityModel.of(developer, links(developer));
    }

    // Mesmos links, mas com o DeveloperResponseDTO no corpo (é o que os controllers usam)
    public EntityModel<DeveloperResponseDTO> toDtoModel(Developer developer){
        return EntityModel.of(new DeveloperResponseDTO(developer), links(developer));
    }

    // Links do HATEOAS. create(null, null) e delete(id, null): os parâmetros extras são os headers
    // (X-Idempotency-Key e X-API-Key), que não entram na URL, então passamos null.
    private List<Link> links(Developer developer){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(DeveloperController.class).findAll(Pageable.unpaged())).withRel("developers").withType("GET"));
        links.add(linkTo(methodOn(DeveloperController.class).findById(developer.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(DeveloperController.class).findByName(developer.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(DeveloperController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(DeveloperController.class).update(developer.getId(), null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(DeveloperController.class).delete(developer.getId(), null)).withRel("delete").withType("DELETE"));
        // NOVO: navegação para os games deste desenvolvedor
        links.add(linkTo(methodOn(GameController.class).findByDeveloper(developer.getId(), Pageable.unpaged())).withRel("games").withType("GET"));
        return links;
    }
}
