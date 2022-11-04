<img src="https://img.shields.io/badge/Status-Concluido-brightgreen">
<h1> Spring Boot Animes API </h1>

<h4>Api construída no curso Spring Boot 2 Essentials da DevDojo, com finalidade de explorar os principais tópicos envolvendo desenvolvimento de APIs com Spring Boot, como:</h4>
<ul>
<li>Criação de endpoints</li>
<li>Spring Data JPA</li>
<li>Spring Security</li>
<li>Mapeamento objeto relacional</li>
<li>Tratamento de exceções</li>
<li>Testes unitários</li>
<li>Testes de integração</li>
</ul>

<h1>Como rodar a aplicação</h1>


<h3> Instalação </h3>
<p>Clone o projeto com: </p>

```
 git clone https://github.com/mmacedoaraujo/springboot2-essentials.git
```

<p>Em seguida abra-o como projeto Maven em uma IDE</p>

<h3> Banco de dados </h3>

<p>Caso possua o docker instalado no seu sistema operacional, bastar abrir um terminal na pasta raiz do projeto e digitar: </p>


```
 docker compose up
```
<p>e um container docker com uma imagem MySQL será criado, expondo a porta 3307. Caso deseje alterar as configurações do banco de dados, basta acessar o arquivo application.yml em: </p>

```
 src/main/resources/application.yml
```
<p>E o arquivo docker-compose.yml na pasta raiz </p>

<h3> Executando a aplicação </h3>
<p>Basta executar a classe: </p>

```
src/main/java/com/mmacedo/springboot2essentials/Springboot2EssentialsApplication.java
```

<p>Em seguida, abra no navegador o endereço http://localhost:8080/swagger-ui.html, onde será pedido um usuário e senha, insira: </p>

```
usuário: marcosadmin
senha: password
```

<p>Pronto, agora você tem acesso à interface do Swagger para testar os métodos de requisição da API!</p>


<h1 align="center"> Dependências utilizadas no projeto </h1>
<ul>
<li>Spring Boot Starter Validation</li>
<li>SpringDoc OpenAPI UI</li>
<li>Spring Boot Starter Security</li>
<li>Spring Boot Starter Data JPA</li>
<li>MySQL Connector Java</li>
<li>Spring Boot Starter Web</li>
<li>Spring Boot Starter DevTools</li>
<li>Lombok</li>
<li>Mapstruct</li>
<li>Spring Boot Starter Test</li>
<li>H2</li>
</ul>
