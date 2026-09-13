package pe.edu.upeu.clinica.citas.cita.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequest {

    @NotNull(message = "El id del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "La fecha y hora de la cita son obligatorias")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    @Size(max = 255, message = "El motivo de consulta no debe exceder 255 caracteres")
    private String motivoConsulta;

    @Size(max = 500, message = "Las observaciones no deben exceder 500 caracteres")
    private String observaciones;

    @Valid
    private List<DetalleCitaRequest> detalles;
}
