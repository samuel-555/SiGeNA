<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Novo Funcionário - SiGeNA</title>
</head>
<body>
    <h2>Cadastrar Novo Funcionário</h2>

    <form action="FuncionarioServlet" method="post">
        <label>Nome:</label><br>
        <input type="text" name="nome" required><br><br>

        <label>Email:</label><br>
        <input type="email" name="email" required><br><br>

        <label>Senha:</label><br>
        <input type="password" name="senha" required><br><br>

        <label>Cargo:</label><br>
        <select name="cargo" required>
            <option value="Gerente">Gerente</option>
            <option value="Zootecnista">Zootecnista</option>
            <option value="Tratador de Animais">Tratador de Animais</option>
            <option value="Veterinário">Veterinário</option>
        </select><br><br>

        <button type="submit">Cadastrar</button>
    </form>

    <br>
    <a href="FuncionarioServlet">Voltar</a>
</body>
</html>
