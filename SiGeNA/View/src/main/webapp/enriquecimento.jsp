<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.Enriquecimento"%>
<%@page import="java.util.List"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeCadastrar = temPermissaoCadastro(cargo, "enriquecimento");
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title>SiGeNA - Cadastrar Enriquecimento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS\\style.css">
    </head>
    <body>
        <header>
            <div class="titulo">
                <a href="<%= request.getContextPath() + "/home.jsp"%>">SiGeNA</a>
            </div>
        </header>
        <div class="container">
            <h1>Gestão de Enriquecimento Ambiental</h1>
            <div class="botoes-acoes">
                <% if (podeCadastrar) { %>
                <a href="${pageContext.request.contextPath}/enriquecimento?action=cadastrar" class="btn">Cadastrar Enriquecimento</a>
                <% } %>
            </div>

            <div class="historico">
                <h2>Enriquecimentos Cadastrados</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Tipo</th>
                            <th>Espécie</th>
                            <th>Frequência</th>
                            <th>Habitats</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Enriquecimento> lista = (List<Enriquecimento>) request.getAttribute("listaEnriquecimentos");
                            if (lista != null && !lista.isEmpty()) {
                                for (Enriquecimento en : lista) {
                        %>
                        <tr>
                            <td><%= en.getNome()%></td>
                            <td><%= en.getTipo()%></td>
                            <td><%= en.getEspecieDestinada()%></td>
                            <td><%= en.getFrequencia()%></td>
                            <td><%= String.join(", ", en.getHabitats())%></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/enriquecimento" method="get" style="display:inline;">
                                    <input type="hidden" name="action" value="ver"/>
                                    <input type="hidden" name="id" value="<%= en.getId()%>"/>
                                    <button type="submit" class="btn-pequeno">Ver</button>
                                </form>
                                    
                                <form action="${pageContext.request.contextPath}/enriquecimento" method="get" style="display:inline;">
                                    <input type="hidden" name="action" value="deletar"/>
                                    <input type="hidden" name="id" value="<%= en.getId()%>"/>
                                    <button type="submit" class="btn-pequeno excluir">Desalocar</button>
                                </form>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr><td colspan="6">Nenhum enriquecimento cadastrado.</td></tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
