package pe.edu.upeu.clinica.pagos.pago.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequest {

    @NotNull(message = "El id del tratamiento es obligatorio")
    private Long idTratamiento;

    @NotNull(message = "El id del metodo de pago es obligatorio")
    private Long idMetodoPago;

    @NotNull(message = "El monto del abono es obligatorio")
    @DecimalMin(value = "0.01", message = "El abono debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El abono debe tener formato 10,2")
    private BigDecimal montoAbono;

    @Size(max = 50, message = "El numero de operacion no debe exceder 50 caracteres")
    private String numeroOperacion;
}
