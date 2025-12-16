package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import sigena.model.dao.OcorrenciaDAO;
import sigena.model.domain.Ocorrencia;
import sigena.model.domain.OcorrenciaTipo;
import sigena.model.service.GestaoOcorrenciaService;
import sigena.model.domain.StatusOcorrencia;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/ocorrencias")
public class OcorrenciaController extends HttpServlet {

    private GestaoOcorrenciaService service;

    @Override
    public void init() {
        try {
            Connection con = sigena.model.util.ConexaoDB.getConnection();
            service = new GestaoOcorrenciaService(new OcorrenciaDAO(con));

            System.out.println(">>> Conexão criada via ConexaoDB");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao iniciar OcorrenciaController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if (acao == null) {
            List<Ocorrencia> lista = service.listar();
            request.setAttribute("ocorrencias", lista);
            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
            return;
        }

        if ("editar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));

            Ocorrencia oc = service.buscar(id);
            List<Ocorrencia> lista = service.listar();

            request.setAttribute("ocorrencias", lista);
            request.setAttribute("ocorrenciaEdicao", oc);

            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
        }

        response.sendRedirect("ocorrencias");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {

            if ("cadastrar".equals(acao)) {

                Ocorrencia oc = montarOcorrencia(request);

                System.out.println("DEBUG OCORRENCIA:");
                System.out.println("Descricao: " + oc.getDescricao());
                System.out.println("Tipo: " + oc.getTipo());
                System.out.println("Status: " + oc.getStatus());
                System.out.println("Data: " + oc.getData());

                service.criar(oc);

                response.sendRedirect("ocorrencias");
                return;
            }

            if ("atualizar".equals(acao)) {

                Long id = Long.parseLong(request.getParameter("id"));
                Ocorrencia oc = montarOcorrencia(request);
                oc.setId(id);

                service.atualizar(oc);

                response.sendRedirect("ocorrencias");
                return;
            }

            if ("cancelar".equals(acao)) {
                Long id = Long.parseLong(request.getParameter("id"));
                service.cancelar(id);
                response.sendRedirect("ocorrencias");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Erro ao processar ocorrência.");
            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
        }
    }

    private Ocorrencia montarOcorrencia(HttpServletRequest request) {

        String tipoStr = request.getParameter("tipo");
        String descricao = request.getParameter("descricao");
        String dataStr = request.getParameter("data");
        String horaStr = request.getParameter("hora");

        if (tipoStr == null || tipoStr.isBlank()) {
            throw new IllegalArgumentException("Tipo da ocorrência é obrigatório.");
        }

        if (dataStr == null || dataStr.isBlank()) {
            throw new IllegalArgumentException("Data da ocorrência é obrigatória.");
        }

        if (horaStr == null || horaStr.isBlank()) {
            throw new IllegalArgumentException("Hora da ocorrência é obrigatória.");
        }

        LocalDate data = LocalDate.parse(dataStr);
        LocalTime hora = LocalTime.parse(horaStr);

        Ocorrencia oc = new Ocorrencia();
        oc.setTipo(OcorrenciaTipo.valueOf(tipoStr));
        oc.setDescricao(descricao);
        oc.setData(LocalDateTime.of(data, hora));
        oc.setStatus(StatusOcorrencia.PENDENTE);

        return oc;
    }
}
