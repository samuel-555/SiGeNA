<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ page import="sigena.model.domain.Ocorrencia" %>

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
    <title>SiGeNA - Ocorrências</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylefuncionario.css">
</head>
<body>

<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + (cargo == Cargo.GERENTE ? "/home-gerente.jsp" : "/home.jsp") %>">
            SiGeNA
        </a>
    </div>
</header>

<div class="container">
    <h1>Gestão de Ocorrências</h1>

    <div class="botoes-acoes">
        <a href="cadastrar-ocorrencias.jsp" class="btn">Registrar Nova Ocorrência</a>
    </div>

    <%
        String erro = (String) request.getAttribute("mensagemErro");
        if (erro != null) {
    %>
        <div style="color:#b00; padding:8px 0;"><strong><%= erro %></strong></div>
    <%
        }
    %>

    <div class="historico">
        <h2>Histórico de Ocorrências</h2>
        <table>
            <thead>
            <tr>
                <th>Tipo</th>
                <th>Data</th>
                <th>Hora</th>
                <th>Descrição</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>

            <%
                java.util.List<Ocorrencia> lista =
                        (java.util.List<Ocorrencia>) request.getAttribute("ocorrencias");

                if (lista != null) {
                    for (Ocorrencia oc : lista) {
            %>
                <tr>
                    <td><%= oc.getTipo().name() %></td>
                    <td><%= oc.getData() != null ? oc.getData().toLocalDate() : "-" %></td>
                    <td><%= oc.getData() != null ? oc.getData().toLocalTime() : "-" %></td>
                    <td><%= oc.getDescricao() != null ? oc.getDescricao() : "-" %></td>

                    <td>
                        <a class="btn-pequeno editar"
                           href="ocorrencias?acao=editar&id=<%= oc.getId() %>">
                            Editar
                        </a>

                        <form action="ocorrencias" method="post" style="display:inline"
                              onsubmit="return confirm('Cancelar ocorrência?');">
                            <input type="hidden" name="acao" value="cancelar">
                            <input type="hidden" name="id" value="<%= oc.getId() %>">
                            <button class="btn-pequeno excluir" type="submit">
                                Cancelar
                            </button>
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
        Ocorrencia ocEd = (Ocorrencia) request.getAttribute("ocorrenciaEdicao");
        if (ocEd != null) {
    %>

    <div class="editar-bloco" style="margin-top:20px;">
        <h2>Editando Ocorrência: ID <%= ocEd.getId() %></h2>

        <form action="ocorrencias" method="post">
            <input type="hidden" name="acao" value="atualizar"/>
            <input type="hidden" name="id" value="<%= ocEd.getId() %>"/>

            <label>Tipo da Ocorrência:</label>
            <input type="text" value="<%= ocEd.getTipo().name() %>" readonly/>
            <input type="hidden" name="tipo" value="<%= ocEd.getTipo().name() %>"/>

            <label>Descrição:</label>
            <textarea name="descricao" rows="3"><%= ocEd.getDescricao() %></textarea>

            <label>Data:</label>
            <input type="date" name="data"
                   value="<%= ocEd.getData() != null ? ocEd.getData().toLocalDate() : "" %>" required/>

            <label>Hora:</label>
            <input type="time" name="hora"
                   value="<%= ocEd.getData() != null ? ocEd.getData().toLocalTime() : "" %>" required/>

            <button type="submit" class="btn">Salvar</button>
            <a href="ocorrencias" class="btn cancelar">Cancelar</a>
        </form>
    </div>

    <%
        }
    %>

</div>
</body>
</html>
