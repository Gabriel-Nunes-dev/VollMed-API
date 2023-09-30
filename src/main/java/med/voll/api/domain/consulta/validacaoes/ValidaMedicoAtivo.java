package med.voll.api.domain.consulta.validacaoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaMedicoAtivo implements ValidadorAgendamento{

    @Autowired
    private MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados){
        if (dados.idMedico() == null){
            return;
        }
        var medicoAtivo = medicoRepository.findAtivoById(dados.idMedico());
        if (!medicoAtivo){
            throw new ValidacaoException("Consulta não pode ser agendada com médico inativo");
        }
    }
}
