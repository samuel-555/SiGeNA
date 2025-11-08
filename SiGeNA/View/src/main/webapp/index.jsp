<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <link  rel="stylesheet" href="CSS\login.css">
    <link rel="stylesheet" href="CSS\style.css">
    <meta charset="UTF-8">
    <title>SiGeNA - login</title>
</head>
<body>
    <header>
        <div class="titulo">SiGeNA</div>
    </header>
    <main>
        <div class="login-box">
            <form action="LoginServlet" method="post">
                <label for="cpf">CPF:</label>
                <input type="text" id="cpf" name="cpf" required>

                <label for="senha">Senha:</label>
                <input type="password" id="senha" name="senha" required>

                <button type="submit">Entrar</button>
                
                <%
                    String erro = (String) request.getAttribute("erro");
                    if (erro != null) {
                %>
                    <div class="mensagem"><%= erro %></div>
                <% } %>
            </form>
        </div>
    </main>
</body>
</html>