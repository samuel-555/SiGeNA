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
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "enriquecimento");
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title>SiGeNA - Cadastrar Enriquecimento</title>
        <link rel="stylesheet" href="CSS/styleespecies.css">
        <link rel="stylesheet" href="CSS\\style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
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
                <%
                    String busca = request.getParameter("busca") != null ? request.getParameter("busca") : "";
                    String habitatFiltro = request.getParameter("habitat") != null ? request.getParameter("habitat") : "";
                    String ordem = request.getParameter("ordem") != null ? request.getParameter("ordem") : "";
                    List<String> habitats = (List<String>) request.getAttribute("habitats");
                %>
                <form action="${pageContext.request.contextPath}/enriquecimento" method="get" class="filtro-enriquecimento">
                    <input type="text" name="busca" placeholder="Buscar por nome" value="<%= busca %>">
                    <select name="habitat">
                        <option value="" <%= habitatFiltro.isBlank() ? "selected" : "" %>>Todos os habitats</option>
                        <%
                            if (habitats != null) {
                                for (String h : habitats) {
                        %>
                        <option value="<%= h %>" <%= h.equalsIgnoreCase(habitatFiltro) ? "selected" : "" %>><%= h %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <select name="ordem">
                        <option value="" <%= ordem.isBlank() ? "selected" : "" %>>Ordem padrao</option>
                        <option value="nome_az" <%= "nome_az".equalsIgnoreCase(ordem) ? "selected" : "" %>>Nome A-Z</option>
                        <option value="nome_za" <%= "nome_za".equalsIgnoreCase(ordem) ? "selected" : "" %>>Nome Z-A</option>
                        <option value="mais_recente" <%= "mais_recente".equalsIgnoreCase(ordem) ? "selected" : "" %>>Mais recente</option>
                        <option value="mais_antigo" <%= "mais_antigo".equalsIgnoreCase(ordem) ? "selected" : "" %>>Mais antigo</option>
                    </select>
                    <button type="submit" class="btn-pequeno">Filtrar</button>
                    <a href="${pageContext.request.contextPath}/enriquecimento" class="btn-pequeno">Limpar</a>
                </form>
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
                                    
                                <% if (podeGerenciar) { %>
                                <form action="${pageContext.request.contextPath}/enriquecimento" method="get" style="display:inline;" onsubmit="return confirm('Confirmar exclusão?');">
                                    <input type="hidden" name="action" value="deletar"/>
                                    <input type="hidden" name="id" value="<%= en.getId()%>"/>
                                    <button type="submit" class="btn-pequeno excluir">Desalocar</button>
                                </form>
                                <% } %>
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
