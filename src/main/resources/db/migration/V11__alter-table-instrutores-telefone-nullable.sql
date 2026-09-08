-- Telefone do instrutor passa a ser opcional no cadastro
-- ("ocultar essa informação, inicialmente, para posterior atualização").
alter table instrutores modify telefone varchar(20) null;
