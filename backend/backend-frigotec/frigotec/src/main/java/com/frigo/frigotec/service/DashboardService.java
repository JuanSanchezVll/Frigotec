package com.frigo.frigotec.service;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.DashboardTecnicoDto;
import com.frigo.frigotec.dto.DashboardTecnicoDto.VisitaResumo;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.util.StatusChamadoEnum;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;

/**
 * KPIs do painel do técnico. O técnico vê a fila geral (chamados novos) e os números da própria carteira;
 * o administrador vê a carteira de todos (SDD seção 3).
 */
@Service
public class DashboardService {

    private final ChamadoRepository chamadoRepository;
    private final OrcamentoRepository orcamentoRepository;

    public DashboardService(ChamadoRepository chamadoRepository, OrcamentoRepository orcamentoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.orcamentoRepository = orcamentoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardTecnicoDto doTecnico(AutorAutenticado autor) {
        List<Chamado> todos = chamadoRepository.findAll();
        List<Chamado> daCarteira = todos.stream().filter(c -> daCarteira(c, autor)).toList();

        long novos = todos.stream()
                .filter(c -> c.getTecnico() == null && c.statusAtual() == StatusChamadoEnum.ENVIADO).count();
        long meus = daCarteira.stream().filter(c -> c.statusAtual() != StatusChamadoEnum.FINALIZADO).count();

        LocalDate segunda = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime inicioSemana = segunda.atStartOfDay();
        LocalDateTime fimSemana = segunda.plusDays(7).atStartOfDay();
        List<Chamado> comVisita = daCarteira.stream()
                .filter(c -> c.statusAtual() == StatusChamadoEnum.VISITA_AGENDADA && c.getVisitaAgendadaEm() != null)
                .toList();
        long visitasSemana = comVisita.stream()
                .filter(c -> !c.getVisitaAgendadaEm().isBefore(inicioSemana) && c.getVisitaAgendadaEm().isBefore(fimSemana)).count();
        List<VisitaResumo> proximas = comVisita.stream()
                .filter(c -> c.getVisitaAgendadaEm().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Chamado::getVisitaAgendadaEm))
                .limit(3)
                .map(c -> new VisitaResumo(c.getProtocolo(), c.getUsuario().getNomeCompleto(),
                        c.getEquipamentoUsuario().getEquipamento().getModelo(), c.getVisitaAgendadaEm()))
                .toList();

        long orcamentos = orcamentoRepository.pendentes().stream()
                .filter(o -> daCarteira(o.getChamado(), autor)).count();

        return new DashboardTecnicoDto(novos, meus, visitasSemana, orcamentos, proximas);
    }

    private boolean daCarteira(Chamado c, AutorAutenticado autor) {
        return autor.ehAdmin() || (c.getTecnico() != null && c.getTecnico().getId().equals(autor.id()));
    }
}
