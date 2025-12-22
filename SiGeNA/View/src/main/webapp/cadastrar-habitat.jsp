<%-- 
    Document   : habitats
    Created on : 3 de nov. de 2025, 09:26:00
    Author     : USUARIO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Habitat</title>
  <link rel="stylesheet" href="CSS/style.css">
  <link rel="stylesheet" href="CSS/stylehabitat.css">
  <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
</head>
<body>
  <header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
   </header>

  <div class="container">
    <h1>Gestão de Habitat</h1>

    <div class="botoes-acoes">
      <a href="HabitatController?acao=listar" class="btn">Voltar</a>
    </div>
    
    <div class="formulario">
      <h2>Cadastrar Novo Habitat</h2>
      
    <form action="${pageContext.request.contextPath}/HabitatController" method="post">
    <input type="hidden" name="acao" value="inserir">
        
        <c:if test="${not empty msgErro}">
            <p style="color:red;font-weight:bold">${msgErro}</p>
        </c:if>
        
        <label for="nome">Nome do Habitat:</label>
        <input type="text" id="nome" name="nome" placeholder="Ex: Savana Africana">

        <label for="tipo">Tipo de Habitat:</label>
        <input type="text" id="tipo" name="tipo" placeholder="Ex: Cerrado, Floresta">

        <label for="tamanho">Tamanho em m³:</label>
        <input type="number" name="tamanho" id="tamanho" min="1" placeholder="Ex: 10 m³">
        
        <label for="manutencao">Precisa de Manutenção</label>
        <input type="checkbox" id="manutencao" name="manutencao">

        
        <button type="submit" class="btn-enviar">Salvar Habitat</button>
      </form>
    </div>
    </div>
 
</body>
</html>

