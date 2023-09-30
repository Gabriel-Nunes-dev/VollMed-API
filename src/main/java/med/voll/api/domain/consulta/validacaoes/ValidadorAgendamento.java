package med.voll.api.domain.consulta.validacaoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamento {

    void validar(DadosAgendamentoConsulta dados);
}
