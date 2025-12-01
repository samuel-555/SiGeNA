<%-- 
    Document   : cadastrar-tarefa
    Created on : 17 de nov. de 2025, 10:06:39
    Author     : aluno
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Cadastrar Tarefa</title>
  <link rel="stylesheet" href="CSS/style.css">
  <link rel="stylesheet" href="CSS/stylehabitat.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Cadastrar Tarefa</h1>

    <div class="botoes-acoes">
      <a href="home.jsp" class="btn">Voltar</a>
    </div>
    
    <div class="formulario">
      <h2>Cadastrar Nova Tarefa</h2>
      
    <form action="${pageContext.request.contextPath}/TarefaController" method="post">
    <input type="hidden" name="acao" value="inserir">
        
        <c:if test="${not empty msgErro}">
            <p style="color:red;font-weight:bold">${msgErro}</p>
        </c:if>
        
        <label for="nome">Nome da tarefa:</label>
        <input type="text" id="nome" name="nome" placeholder="Ex: Limpar aquário">

        <!-- possivelmente colocar selecao de tipo pra separar os funcionarios que vao aparecer -->
        <label for="encarregado">Funcionario encarregado:</label> <!--  campo de seleção apenas com os funcionarios ativos -->
        <input type="text" id="encarregado" name="encarregado" placeholder="Ex: Jorge veterinário">

        <label for="data-conclusao">Data para conclusão:</label>
        <input type="date" name="data-conclusao" id="data-conclusao"placeholder="Ex: 10/10/2007">
        
        
        <button type="submit" class="btn-enviar">Enviar tarefa</button>
      </form>
    </div>
    </div>
 
</body>
</html>

