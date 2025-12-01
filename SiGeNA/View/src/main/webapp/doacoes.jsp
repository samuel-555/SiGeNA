<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ page import="sigena.model.domain.Doacao" %>
<%@ page import="sigena.model.domain.ReciboDoacao" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    if (cargo == null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Doações</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylefuncionario.css">
</head>
<body>
<header>
    <div class="titulo"><a href="<%= request.getContextPath() + (cargo == Cargo.GERENTE ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
</header>

<div class="container">
    <h1>Gestão de Doações</h1>

    <div class="botoes-acoes">
        <a href="cadastrar-doacao.jsp" class="btn">Cadastrar Nova Doação</a>
    </div>

    <c:if test="${not empty mensagemSucesso}">
        <div style="color:green;padding:8px 0;"><strong>${mensagemSucesso}</strong></div>
    </c:if>
    <c:if test="${not empty mensagemErro}">
        <div style="color:#b00;padding:8px 0;"><strong>${mensagemErro}</strong></div>
    </c:if>
    <c:if test="${not empty erroCadastro}">
        <div style="color:#b00;padding:8px 0;"><strong>${erroCadastro}</strong></div>
    </c:if>
    <c:if test="${not empty erroAtualizacao}">
        <div style="color:#b00;padding:8px 0;"><strong>${erroAtualizacao}</strong></div>
    </c:if>

    <div class="historico">
        <h2>Lista de Doações</h2>
        <table>
            <thead>
                <tr>
                    <th>Doador</th>
                    <th>Tipo</th>
                    <th>Valor/Descrição</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Recibo</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    java.util.List<Doacao> lista = (java.util.List<Doacao>) request.getAttribute("doacoes");
                    if (lista != null) {
                        for (Doacao d : lista) {
                %>
                <tr>
                    <td><%= d.getNomeDoador() %></td>
                    <td><%= d.getTipo().name() %></td>
                    <td>
                        <%
                            if (d.isMonetaria()) {
                                out.print(d.getValorMonetario() != null ? String.format("R$ %.2f", d.getValorMonetario()) : "-");
                            } else {
                                out.print(d.getDescricaoOutro() != null ? d.getDescricaoOutro() : "-");
                            }
                        %>
                    </td>
                    <td><%= d.getDataDoacao() != null ? d.getDataDoacao().toString() : "-" %></td>
                    <td><%= d.getStatus() != null ? d.getStatus().name() : "-" %></td>
                    <td>
                        <%
                            if (d.isReciboEmitido()) {
                                ReciboDoacao r = (ReciboDoacao) request.getAttribute("reciboEdicao");
                                // se o controller carregou o recibo para edição, mostra o código
                                if (r != null && r.getDoacaoId() != null && r.getDoacaoId().equals(d.getId())) {
                                    out.print(r.getCodigo());
                                } else {
                                    out.print("Emitido");
                                }
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <a class="btn-pequeno editar" href="doacoes?acao=editar&id=<%= d.getId() %>">Editar</a>

                        <form action="doacoes" method="post" style="display:inline" onsubmit="return confirm('Confirmar cancelamento?');">
                            <input type="hidden" name="acao" value="cancelar"/>
                            <input type="hidden" name="id" value="<%= d.getId() %>"/>
                            <button class="btn-pequeno excluir" type="submit">Cancelar</button>
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

    <%
        // Se houver doacaoEdicao, exibimos um bloco de edição com campos para atualizar
        Doacao ed = (Doacao) request.getAttribute("doacaoEdicao");
        if (ed != null) {
    %>
    <div class="editar-bloco" style="margin-top:20px;">
        <h2>Editando Doação: ID <%= ed.getId() %></h2>

        <form action="doacoes" method="post">
            <input type="hidden" name="acao" value="atualizar" />
            <input type="hidden" name="id" value="<%= ed.getId() %>" />

            <label>Tipo:</label>
            <input type="text" name="tipo" value="<%= ed.getTipo().name() %>" readonly />

            <c:if test="${ed.tipo == 'MONETARIA'}">
                <label>Novo Valor (R$):</label>
                <input type="text" name="valorAtualizacao" value="<%= ed.getValorMonetario() != null ? ed.getValorMonetario() : "" %>" />
            </c:if>
            <c:if test="${ed.tipo != 'MONETARIA'}">
                <label>Nova Descrição:</label>
                <input type="text" name="descricaoAtualizacao" value="<%= ed.getDescricaoOutro() != null ? ed.getDescricaoOutro() : "" %>" />
            </c:if>

            <button type="submit" class="btn">Salvar Alterações</button>
            <a href="doacoes" class="btn cancelar">Voltar</a>
        </form>

        <%
            ReciboDoacao recibo = (ReciboDoacao) request.getAttribute("reciboEdicao");
            if (recibo != null) {
        %>
            <div style="margin-top:12px;">
                <strong>Recibo:</strong> Código: <%= recibo.getCodigo() %> - Emitido em: <%= recibo.getDataEmissao() %>
            </div>
        <%
            }
        %>
    </div>
    <%
        }
    %>

</div>
</body>
</html>
