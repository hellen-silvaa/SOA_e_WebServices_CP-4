package br.com.fiap3esr.autoescola3esr.temp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilitário de linha de comando para gerar o hash BCrypt de uma senha,
 * útil para criar seeds de usuários em migrations do Flyway.
 *
 * Uso:  java ... GerarSenhaHash "minhaSenha"   (sem argumento, usa "123456")
 */
public class GerarSenhaHash {
    public static void main(String[] args) {
        String senha = args.length > 0 ? args[0] : "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(senha));
    }
}
