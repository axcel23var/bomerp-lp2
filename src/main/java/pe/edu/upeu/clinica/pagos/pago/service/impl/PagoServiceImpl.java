package pe.edu.upeu.clinica.pagos.pago.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.clinica.pagos.pago.dto.PagoRequest;
import pe.edu.upeu.clinica.pagos.pago.dto.PagoResponse;
import pe.edu.upeu.clinica.pagos.pago.entity.DetallePago;
import pe.edu.upeu.clinica.pagos.pago.entity.Pago;
import pe.edu.upeu.clinica.pagos.pago.mapper.PagoMapper;
import pe.edu.upeu.clinica.pagos.pago.repository.MetodoPagoRepository;
import pe.edu.upeu.clinica.pagos.pago.repository.PagoRepository;
import pe.edu.upeu.clinica.pagos.pago.service.PagoService;
import pe.edu.upeu.clinica.shared.exception.MontoExcedidoException;
import pe.edu.upeu.clinica.shared.exception.ResourceNotFoundException;
import pe.edu.upeu.clinica.tratamientos.TratamientoSaldoPort;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("null")
@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_PAGADO = "PAGADO";

    private final PagoRepository pagoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PagoMapper pagoMapper;
    private final TratamientoSaldoPort tratamientoSaldoPort;

    @Override
    @Transactional
    public PagoResponse registrarAbono(PagoRequest request) {
        if (!metodoPagoRepository.existsById(request.getIdMetodoPago())) {
            throw new ResourceNotFoundException("MetodoPago", request.getIdMetodoPago());
        }

        BigDecimal saldoPendiente = tratamientoSaldoPort.obtenerSaldoPendiente(request.getIdTratamiento());
        if (request.getMontoAbono().compareTo(saldoPendiente) > 0) {
            throw new MontoExcedidoException("El abono excede el saldo pendiente del tratamiento");
        }

        Pago pago = pagoRepository.findFirstByIdTratamientoOrderByIdAsc(request.getIdTratamiento())
                .orElseGet(() -> Pago.builder()
                        .idTratamiento(request.getIdTratamiento())
                        .montoTotal(BigDecimal.ZERO)
                        .build());

        DetallePago detalle = DetallePago.builder()
                .idMetodoPago(request.getIdMetodoPago())
                .montoAbonado(request.getMontoAbono())
                .numeroOperacion(request.getNumeroOperacion())
                .build();
        pago.agregarDetalle(detalle);

        Pago guardado = pagoRepository.saveAndFlush(pago);
        tratamientoSaldoPort.aplicarAbono(request.getIdTratamiento(), request.getMontoAbono());
        return toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Long id) {
        return toResponse(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listar() {
        return pagoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorTratamiento(Long idTratamiento) {
        return pagoRepository.findByIdTratamiento(idTratamiento).stream()
                .map(this::toResponse)
                .toList();
    }

    private PagoResponse toResponse(Pago pago) {
        PagoResponse response = pagoMapper.toResponse(pago);
        BigDecimal saldo = tratamientoSaldoPort.obtenerSaldoPendiente(pago.getIdTratamiento());
        response.setEstado(saldo.compareTo(BigDecimal.ZERO) == 0 ? ESTADO_PAGADO : ESTADO_PENDIENTE);
        return response;
    }

    private Pago buscar(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
    }
}
