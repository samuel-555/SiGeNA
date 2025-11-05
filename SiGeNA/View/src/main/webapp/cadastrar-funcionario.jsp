<%@ page import="sigena.model.domain.Cargo" %>
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
    <div class="titulo"><a href="funcionario.jsp" style="color: inherit; text-decoration: none;">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Funcionários</h1>

    <div class="botoes-acoes">
      <a href="funcionario.jsp" class="btn">Voltar</a>
    </div>

    <div class="formulario">
      <h2>Adicionar Novo Funcionário</h2>
      <form action="FuncionarioServlet" method="post">
        <input type="hidden" name="acao" value="cadastrar">

        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" placeholder="Ex: Maria Oliveira" required>

        <label for="cargo">Cargo:</label>
        <select id="cargo" name="cargo" required>
          <% for (Cargo c : Cargo.values()) { %>
            <option value="<%= c.name() %>"><%= c.getDescricao() %></option>
          <% } %>
        </select>

        <label for="area">Área de Atuação:</label>
        <input type="text" id="area" name="area" placeholder="Ex: Clínica, Alimentação, Limpeza">

        <label for="observacoes">Observações:</label>
        <textarea id="observacoes" name="observacoes" rows="3" placeholder="Ex: Especialista em animais felinos"></textarea>

        <button type="submit" class="btn-enviar">Salvar Funcionário</button>
      </form>
    </div>
  </div>
</body>
</html>
