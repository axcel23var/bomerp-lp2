package pe.edu.upeu.clinica.tratamientos.plan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TratamientoRequest {

    @NotNull(message = "El id del paciente es obligatorio")
    private Long idPaciente;

    @Valid
    private List<DetalleTratamientoRequest> detalles;
}
