<%@ page import="java.util.*, sigena.model.dao.FuncionarioDAO, sigena.model.domain.Funcionario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Simulação de sessão do gerente (em produção, viria do login)
    String cargoUsuario = (String) session.getAttribute("cargoUsuario");
    if (cargoUsuario == null || !"Gerente".equals(cargoUsuario)) {
        response.sendRedirect("login.jsp");
        return;
    }

    FuncionarioDAO dao = new FuncionarioDAO();
    List<Funcionario> funcionarios = dao.listar();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Funcionários</title>
  <link rel="stylesheet" href="CSS/stylefuncionario.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="..\Tela Inicial\telainicial.html" style="color: inherit; text-decoration: none; cursor: pointer;">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Funcionários</h1>

    <div class="botoes-acoes">
      <a href="cadastrar-funcionario.jsp" class="btn">Cadastrar Funcionário</a>
    </div>

    <div class="historico">
      <h2>Lista de Funcionários</h2>
      <table>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Cargo</th>
            <th>Área de Atuação</th>
            <th>Data de Cadastro</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <% for (Funcionario f : funcionarios) { %>
          <tr>
            <td><%= f.getNome() %></td>
            <td><%= f.getCargo().getDescricao() %></td>
            <td><%= f.getAreaAtuacao() %></td>
            <td><%= f.getDataCadastro() %></td>
            <td>
              <form action="FuncionarioServlet" method="post" style="display:inline;">
                <input type="hidden" name="acao" value="deletar">
                <input type="hidden" name="id" value="<%= f.getId() %>">
                <button class="btn-pequeno excluir" type="submit">Excluir</button>
              </form>
            </td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
