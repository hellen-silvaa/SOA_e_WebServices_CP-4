-- Usuário administrador inicial (bootstrap).
-- Senha: 123456  (hash BCrypt gerado pelo BCryptPasswordEncoder).
-- Recomenda-se alterar a senha no primeiro acesso via PUT /usuarios/senha.
insert into usuarios (login, senha, perfil)
values ('admin@autoescola3esr.com.br', '$2a$10$6JCKyHuz8tBl7Ry83ewPReGv913hFtGlnvceXTssSYduHcBq1l9pG', 'ADMIN');
