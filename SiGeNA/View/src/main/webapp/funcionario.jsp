<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    if (cargo == null || cargo != Cargo.GERENTE) {
        response.sendRedirect("home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SiGeNA - Gestão de Funcionários</title>
        <link rel="stylesheet" href="CSS\\style.css">
        <link rel="stylesheet" href="CSS\\stylefuncionario.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>

        <div class="container">
            <h1>Gestão de Funcionários</h1>

            <div class="botoes-acoes">
                <a href="cadastrar-funcionario.jsp" class="btn">Cadastrar Novo Funcionário</a>
            </div>

            <c:if test="${not empty erro}">
                <div style="color:#b00;padding:8px 0;"><strong>${erro}</strong></div>
            </c:if>

            <div class="historico">
                <h2>Lista de Funcionários</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Cargo</th>
                            <th>Setor</th>
                            <th>Turno</th>
                            <th>Estado</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            java.util.List<sigena.model.domain.Funcionario> lista
                                    = (java.util.List<sigena.model.domain.Funcionario>) request.getAttribute("funcionarios");
                            if (lista != null) {
                                for (sigena.model.domain.Funcionario f : lista) {
                        %>
                        <tr>
                            <td><%= f.getNome()%></td>
                            <td><%= f.getCargo().getDescricao()%></td>
                            <td><%= f.getAreaAtuacao()%></td>
                            <td><%= f.getTurno().getDescricao()%></td>
                            <td><%= f.getEstado()%></td>
                            <td>
                                <a class="btn-pequeno editar" href="FuncionarioServlet?acao=editar&id=<%= f.getId()%>">Editar</a>
                                <form action="FuncionarioServlet" method="post" style="display:inline" onsubmit="return confirm('Confirmar exclusão?');">
                                    <input type="hidden" name="acao" value="deletar"/>
                                    <input type="hidden" name="id" value="<%= f.getId()%>"/>
                                    <input type="hidden" name="setor" value="<%= f.getAreaAtuacao()%>"/>
                                    <input type="hidden" name="turno" value="<%= f.getTurno().name()%>"/>
                                    <button class="btn-pequeno excluir" type="submit">Excluir</button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
