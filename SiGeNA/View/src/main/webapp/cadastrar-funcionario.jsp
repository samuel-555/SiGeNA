<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Cadastrar Funcionário</title>
  <link rel="stylesheet" href="CSS/style.css">
  <link rel="stylesheet" href="CSS\\stylefuncionario.css">
</head>
<body>
  <header><div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div></header>

  <div class="container">
    <h1>Cadastrar Funcionário</h1>

    <form action="FuncionarioServlet" method="post">
      <input type="hidden" name="acao" value="${funcionario != null ? 'atualizar' : 'salvar'}">
      <c:if test="${funcionario != null}">
        <input type="hidden" name="id" value="${funcionario.id}">
      </c:if>

      <label>Nome:</label>
      <input type="text" name="nome" value="${funcionario.nome}" required>

      <label>CPF:</label>
      <input type="text" name="cpf" value="${funcionario.cpf}" required minlength="11" maxlength="11" pattern="\d{11}" title="Informe exatamente 11 dígitos numéricos">

      <label>Cargo:</label>
      <select name="cargo" required>
        <option value="GERENTE" ${funcionario.cargo == 'GERENTE' ? 'selected' : ''}>Gerente</option>
        <option value="ZOOTECNISTA" ${funcionario.cargo == 'ZOOTECNISTA' ? 'selected' : ''}>Zootecnista</option>
        <option value="VETERINARIO" ${funcionario.cargo == 'VETERINARIO' ? 'selected' : ''}>Veterinário</option>
        <option value="TRATADOR" ${funcionario.cargo == 'TRATADOR' ? 'selected' : ''}>Tratador</option>
      </select>

      <label>Área de Atuação:</label>
      <input type="text" name="area" value="${funcionario.areaAtuacao}" required>

      <label>Turno:</label>
      <select name="turno" required>
        <option value="MANHA">Manhã</option>
        <option value="TARDE">Tarde</option>
        <option value="NOITE">Noite</option>
      </select>

      <label>Estado:</label>
      <select name="estado">
        <option value="ATIVO">Ativo</option>
        <option value="FERIAS">Férias</option>
        <option value="LICENCA_MATERNIDADE">Licença Maternidade</option>
        <option value="LICENCA_PATERNIDADE">Licença Paternidade</option>
        <option value="AFASTADO">Afastado</option>
      </select>

      <label>Senha:</label>
      <input type="password" name="senha" required>

      <label>Observações:</label>
      <textarea name="observacoes" rows="3">${funcionario.observacoes}</textarea>

      <button type="submit" class="btn">Salvar</button>
      <a href="FuncionarioServlet" class="btn cancelar">Cancelar</a>
    </form>
  </div>
</body>
</html>
