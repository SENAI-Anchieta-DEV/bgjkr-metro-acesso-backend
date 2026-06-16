package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.EntradaService;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.application.service.PendenciaService;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentificacaoServiceTest {
    @Mock
    private PendenciaService pendenciaService;

    @Mock
    private EntradaService entradaService;

    @InjectMocks
    private IdentificacaoService service;

    @Test
    @DisplayName("Deve criar payload correto para pendência")
    void deveCriarPayloadCorretoParaPendencia() {
        // ARRANGE
        IdentificacaoDto dto = new IdentificacaoDto(
                "TAG123",
                "BSSID123"
        );

        Entrada entrada = mock(Entrada.class);
        Estacao estacao = mock(Estacao.class);

        when(entradaService.procurarEntradaPorBssid("BSSID123"))
                .thenReturn(entrada);

        when(entrada.getEstacao())
                .thenReturn(estacao);

        when(estacao.getCodigoEstacao())
                .thenReturn("EST01");

        when(entrada.getCodigoEntrada())
                .thenReturn("ENT01");

        when(pendenciaService.criarPendencia(any(PendenciaRequestDto.class)))
                .thenReturn(mock(PendenciaResponseDto.class));

        // ACT
        service.solicitarPendencia(dto);

        // ASSERT
        ArgumentCaptor<PendenciaRequestDto> captor =
                ArgumentCaptor.forClass(PendenciaRequestDto.class);

        verify(pendenciaService).criarPendencia(captor.capture());
        verify(entradaService).procurarEntradaPorBssid("BSSID123");

        PendenciaRequestDto payload = captor.getValue();

        assertAll(
                () -> assertEquals("TAG123", payload.codigoTag()),
                () -> assertEquals("EST01", payload.codigoEstacao()),
                () -> assertEquals("ENT01", payload.codigoEntrada())
        );
    }

    @Test
    @DisplayName("RF16 - Deve registrar entrada com estação e data/hora válidas")
    void deveRegistrarEntradaCompleta() {
        // ARRANGE
        EntradaService service = mock(EntradaService.class);

        Entrada entrada = mock(Entrada.class);
        Estacao estacao = mock(Estacao.class);

        when(entrada.getEstacao()).thenReturn(estacao);
        when(estacao.getCodigoEstacao()).thenReturn("EST01");
        when(entrada.getCodigoEntrada()).thenReturn("ENT01");

        when(service.procurarEntradaPorBssid("BSSID123"))
                .thenReturn(entrada);

        // ACT
        Entrada result = service.procurarEntradaPorBssid("BSSID123");

        // ASSERT
        assertEquals("EST01", result.getEstacao().getCodigoEstacao());
        assertEquals("ENT01", result.getCodigoEntrada());

        verify(service, times(1)).procurarEntradaPorBssid("BSSID123");
    }
}