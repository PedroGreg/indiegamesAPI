package br.com.indiegamesapi.repository;

import br.com.indiegamesapi.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByEmailIgnoreCase(String email);
}
