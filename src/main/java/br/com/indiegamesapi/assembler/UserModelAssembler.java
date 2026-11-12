package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.UserController;
import br.com.indiegamesapi.controller.UserProfileController;
import br.com.indiegamesapi.dto.response.UserResponseDTO;
import br.com.indiegamesapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    public EntityModel<User> toModel(User user){
        return EntityModel.of(user, links(user));
    }

    public EntityModel<UserResponseDTO> toDtoModel(User user){
        return EntityModel.of(new UserResponseDTO(user), links(user));
    }

    private List<Link> links(User user){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).findAll(Pageable.unpaged())).withRel("users").withType("GET"));
        links.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(UserController.class).findByName(user.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(UserController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(UserController.class).update(user.getId(), null, null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(UserController.class).delete(user.getId(), null)).withRel("delete").withType("DELETE"));
        if (user.getUserProfile() != null) {
            links.add(linkTo(methodOn(UserProfileController.class).findById(user.getUserProfile().getId())).withRel("user-profile").withType("GET"));
        }
        return links;
    }
}
