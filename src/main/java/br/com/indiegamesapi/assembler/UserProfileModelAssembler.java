package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.UserController;
import br.com.indiegamesapi.controller.UserProfileController;
import br.com.indiegamesapi.dto.response.UserProfileResponseDTO;
import br.com.indiegamesapi.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserProfileModelAssembler implements RepresentationModelAssembler<UserProfile, EntityModel<UserProfile>> {
    public EntityModel<UserProfile> toModel(UserProfile userProfile){
        return EntityModel.of(userProfile, links(userProfile));
    }

    // Mesmos links, mas com o UserProfileResponseDTO no corpo (é o que os controllers usam)
    public EntityModel<UserProfileResponseDTO> toDtoModel(UserProfile userProfile){
        return EntityModel.of(new UserProfileResponseDTO(userProfile), links(userProfile));
    }

    // Links do HATEOAS. create(null, null) e delete(id, null): os parâmetros extras são os headers
    // (X-Idempotency-Key e X-API-Key), que não entram na URL, então passamos null.
    private List<Link> links(UserProfile userProfile){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserProfileController.class).findAll(Pageable.unpaged())).withRel("user-profiles").withType("GET"));
        links.add(linkTo(methodOn(UserProfileController.class).findById(userProfile.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(UserProfileController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(UserProfileController.class).update(userProfile.getId(), null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(UserProfileController.class).delete(userProfile.getId(), null)).withRel("delete").withType("DELETE"));
        // NOVO: navegação para o usuário dono do perfil (One-to-One)
        if (userProfile.getUser() != null) {
            links.add(linkTo(methodOn(UserController.class).findById(userProfile.getUser().getId())).withRel("user").withType("GET"));
        }
        return links;
    }
}
