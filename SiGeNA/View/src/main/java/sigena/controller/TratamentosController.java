package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;
import sigena.model.domain.util.TipoTratamento;
import sigena.model.service.GestaoAnimalService;
import sigena.model.service.GestaoNotificacaoService;
import sigena.model.service.GestaoTratamentosService;

@WebServlet(name = "TratamentosController", urlPatterns = {"/TratamentosController"})
public class TratamentosController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        try {
            if (acao == null || acao.equals("listar")) {
                listar(response, request);
            } else if (acao.equals("ver")) {
                ver(request, response);
            } else if (acao.equals("cancelar")) {
                cancelar(request, response);
            }
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("tratamentos.jsp").forward(request, response);
        } catch (DatabaseException ex) {
            Logger.getLogger(TratamentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("salvar".equals(acao)) {
            try {
                cadastrar(request);
                request.setAttribute("mensagemSucesso", "Tratamento cadastrado com sucesso!");
                GestaoNotificacaoService not = new GestaoNotificacaoService();
                not.criarParaTodos("Novo tratamento cadastrado");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensagemErro", "Erro ao cadastrar tratamento: " + e.getMessage());
            }
            response.sendRedirect("TratamentosController?acao=listar");
        } else if ("editar".equals(acao)) {
            try {
                editar(request, response);
            } catch (Exception ex) {
                Logger.getLogger(TratamentosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            request.setAttribute("mensagemErro", "Ação inválida");
            response.sendRedirect("TratamentosController?acao=listar&erro");
        }
    }

    public void cadastrar(HttpServletRequest request) throws Exception {
        GestaoAnimalService serviceAnimal = new GestaoAnimalService();
        Long animalId = Long.valueOf(request.getParameter("animal"));
        Animal animal = serviceAnimal.buscarAnimal(animalId);
        if (animal == null) {
            throw new Exception("Animal não encontrado.");
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");

        if (usuario == null) {
            throw new Exception("Usuário não autenticado.");
        }
        String diagnostico = request.getParameter("diagnostico");
        String medicacao = request.getParameter("medicacao");
        int frequencia = 0;
        try {
            frequencia = Integer.parseInt(request.getParameter("frequencia"));
        } catch (NumberFormatException e) {
            frequencia = 0;
        }

        String obs = request.getParameter("observacoes");

        TipoTratamento tipo = TipoTratamento.valueOf(request.getParameter("tipoTratamento"));
        LocalTime horario;
        if (request.getParameter("horario") != null && !request.getParameter("horario").isBlank()) {
            horario = LocalTime.parse(request.getParameter("horario"));
        } else {
            horario = null;
        }

        LocalDate dataFinal = LocalDate.parse(request.getParameter("data"));

        Tratamento tratamento = new Tratamento(animal, usuario, diagnostico, medicacao, frequencia, obs, tipo, dataFinal, horario);

        GestaoTratamentosService service = new GestaoTratamentosService();
        service.cadastrar(animal, usuario, tratamento);
    }

    public void listar(HttpServletResponse response, HttpServletRequest request) throws PersistenciaException, ServletException, IOException, DatabaseException {

        String busca = request.getParameter("busca");
        String status = request.getParameter("status");
        String tipo = request.getParameter("tipo");
        if (busca == null) {
            busca = "";
        }
        if (status == null) {
            status = "";
        }
        if (tipo == null) {
            tipo = "";
        }
        GestaoTratamentosService service = new GestaoTratamentosService();
        List<Tratamento> lista = service.listar(busca, status, tipo);
        request.setAttribute("lista", lista);
        request.getRequestDispatcher("tratamentos.jsp").forward(request, response);
    }

    private void ver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException, DatabaseException {

        int id = Integer.parseInt(request.getParameter("id"));

        GestaoTratamentosService service = new GestaoTratamentosService();
        Tratamento t = service.buscarPorId(id);
        System.out.println("ID recebido: " + id);
        System.out.println("Tratamento retornado: " + t);

        request.setAttribute("tratamento", t);
        request.getRequestDispatcher("editar-tratamento.jsp").forward(request, response);
    }

    public void editar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        GestaoAnimalService serviceAnimal = new GestaoAnimalService();
        Long animalId = Long.valueOf(request.getParameter("animal"));
        Animal animal = serviceAnimal.buscarAnimal(animalId);
        if (animal == null) {
            throw new Exception("Animal não encontrado.");
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");

        if (usuario == null) {
            throw new Exception("Usuário não autenticado.");
        }
        String diagnostico = request.getParameter("diagnostico");
        String medicacao = request.getParameter("medicacao");
        int frequencia = 0;
        try {
            frequencia = Integer.parseInt(request.getParameter("frequencia"));
        } catch (NumberFormatException e) {
            frequencia = 0;
        }

        String obs = request.getParameter("observacoes");

        TipoTratamento tipo = TipoTratamento.valueOf(request.getParameter("tipoTratamento"));
        LocalTime horario;
        if (request.getParameter("horario") != null && !request.getParameter("horario").isBlank()) {
            horario = LocalTime.parse(request.getParameter("horario"));
        } else {
            horario = null;
        }

        LocalDate dataFinal = LocalDate.parse(request.getParameter("data"));

        GestaoTratamentosService service = new GestaoTratamentosService();
        Tratamento t = service.buscarPorId(id);
        t.setAnimal(animal);
        t.setMedico(usuario);
        t.setDiagnostico(diagnostico);
        t.setMedicacao(medicacao);
        t.setFrequencia(frequencia);
        t.setHorario(horario);
        t.setTipoTratamento(tipo);
        t.setDataFinal(dataFinal);
        t.setObservacao(obs != null ? obs : "");

        service.editar(t);

        response.sendRedirect("TratamentosController?acao=listar");
    }

    public void cancelar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, DatabaseException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        GestaoTratamentosService s = new GestaoTratamentosService();
        s.cancelar(id);

        response.sendRedirect("TratamentosController?acao=listar");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
