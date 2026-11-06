//package br.com.indiegamesapi.controller;
//
//import br.com.indiegamesapi.dto.LoginRequestDTO;
//import br.com.indiegamesapi.service.LoginService;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/login")
//public class LoginController {
//
//    private final LoginService service;
//
//    public LoginController(LoginService service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> onLogin(@RequestBody @Valid LoginRequestDTO dto){
//        return service.onLogin(dto.email(), dto.password());
//    }
//}
