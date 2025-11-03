<%@ page import="java.util.List" %>
<%@ page import="sigena.model.domain.Funcionario" %>
<%
    List<Funcionario> funcionarios = (List<Funcionario>) request.getAttribute("funcionarios");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gerenciamento de Funcionários - SiGeNA</title>
    <link rel="stylesheet" href="CSS/style.css">
</head>
<body>
    <h2>Gerenciamento de Funcionários</h2>
    <a href="funcionarioForm.jsp">+ Novo Funcionário</a>
    <table border="1" cellpadding="8">
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Email</th>
            <th>Cargo</th>
            <th>Data de Cadastro</th>
            <th>Ações</th>
        </tr>
        <%
            if (funcionarios != null) {
                for (Funcionario f : funcionarios) {
        %>
        <tr>
            <td><%= f.getId() %></td>
            <td><%= f.getNome() %></td>
            <td><%= f.getEmail() %></td>
            <td><%= f.getCargo() %></td>
            <td><%= f.getDataCadastroFormatada() %></td>
            <td>
                <form action="FuncionarioServlet" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= f.getId() %>">
                    <button type="submit" formaction="ExcluirFuncionarioServlet">Excluir</button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>