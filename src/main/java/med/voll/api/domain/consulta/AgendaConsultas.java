package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validacaoes.ValidadorAgendamento;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.repository.ConsultaRepository;
import med.voll.api.repository.MedicoRepository;
import med.voll.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    private  List<ValidadorAgendamento> validadoes;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {

        if (!pacienteRepository.existsById(dadosAgendamentoConsulta.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe");
        }

        if (dadosAgendamentoConsulta.idMedico() != null && !medicoRepository.existsById(dadosAgendamentoConsulta.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe");
        }

        validadoes.forEach(v -> v.validar(dadosAgendamentoConsulta));

        var medico = escolherMedico(dadosAgendamentoConsulta);
        var paciente = pacienteRepository.getReferenceById(dadosAgendamentoConsulta.idPaciente());

        if (medico == null){
            throw new ValidacaoException("Médicos indisponíveis nesta data");
        }

        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data());

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if (dadosAgendamentoConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(dadosAgendamentoConsulta.idMedico());
        }
        if (dadosAgendamentoConsulta.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dadosAgendamentoConsulta.especialidade(),
                dadosAgendamentoConsulta.data());

    }
}
