//package br.com.indiegamesapi.service;
//
//import br.com.indiegamesapi.dto.LoginResponseDTO;
//import br.com.indiegamesapi.model.User;
//import br.com.indiegamesapi.repository.UserRepository;
//import br.com.indiegamesapi.security.TokenService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class LoginService {
//
//    private final UserRepository repository;
//    private final TokenService tokenService;
//
//
//    public LoginService(UserRepository repository, TokenService tokenService) {
//        this.repository = repository;
//        this.tokenService = tokenService;
//    }
//
//    public ResponseEntity<?> onLogin(String email, String password){
//        Optional<User> entity = repository.findByEmailIgnoreCase(email);
//
//        if(entity != null && entity.isPresent()){
//            User usuario = entity.get();
//            if(usuario.getPassword().equals(password)){
//                String token = tokenService.gerarToken(usuario.getEmail(), usuario.getName());
//                LoginResponseDTO responseDTO = new LoginResponseDTO(token, usuario.getName(), usuario.getEmail(), "Logado");
//                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
//}
