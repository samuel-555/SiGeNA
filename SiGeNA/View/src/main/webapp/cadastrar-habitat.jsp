<%-- 
    Document   : habitats
    Created on : 3 de nov. de 2025, 09:26:00
    Author     : USUARIO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Habitat</title>
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS\stylehabitat.css">
</head>
<body>
  <header>
    <div class="titulo">SiGeNA</div>
  </header>

  <div class="container">
    <h1>Gestão de Habitat</h1>

    <div class="botoes-acoes">
      <a href="habitats.jsp" class="btn">Voltar</a>
    </div>

    <div class="formulario">
      <h2>Cadastrar Novo Habitat</h2>
      <form>
        <label for="nome">Nome do Habitat:</label>
        <input type="text" id="nome" placeholder="Ex: Savana Africana">

        <label for="tipo">Tipo de Habitat:</label>
        <input type="text" id="tipo" placeholder="Ex: Cerrado, Aquático, Floresta">

        <label for="capacidade">Tamanho em m³:</label>
        <input type="number" id="capacidade" min="1" placeholder="Ex: 10 m³">

        <label for="observacoes">Observações:</label>
        <textarea id="observacoes" rows="3" placeholder="Ex: Alimentação específica, cuidados especiais"></textarea>

        <button type="submit" class="btn-enviar">Salvar Habitat</button>
      </form>
    </div>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>

