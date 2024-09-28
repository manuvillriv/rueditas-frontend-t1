package pe.edu.cibertec.exament1_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.exament1_frontend.dto.BuscarRequestDTO;
import pe.edu.cibertec.exament1_frontend.dto.BuscarResponseDTO;
import pe.edu.cibertec.exament1_frontend.viewmodel.BuscarModel;

@Controller
@RequestMapping("/buscar")
public class BuscarController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/ingreso")
    public String inicio(Model model) {
        BuscarModel buscarModel = new BuscarModel("00", "",
                "", "", 0, 0.0, "");
        model.addAttribute("buscarModel", buscarModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("numPlaca") String numPlaca,
                             Model model) {

        if (numPlaca == null || numPlaca.trim().length() == 0) {
            BuscarModel buscarModel = new BuscarModel("01", "El campo es requerido",
                    "", "",0, 0.0, "");
            model.addAttribute("buscarModel", buscarModel);
            return "inicio";
        }

        try {
            String endpoint = "http://localhost:8081/autenticacion/buscar";
            BuscarRequestDTO buscarRequestDTO = new BuscarRequestDTO(numPlaca);
            BuscarResponseDTO buscarResponseDTO = restTemplate.postForObject(endpoint, buscarRequestDTO, BuscarResponseDTO.class);

            if (buscarResponseDTO.codigo().equals("00")) {

                BuscarModel buscarModel = new BuscarModel("00", "",
                                                            buscarResponseDTO.marca(),
                                                            buscarResponseDTO.modelo(),
                                                            buscarResponseDTO.nroAsientos(),
                                                            buscarResponseDTO.precio(),
                                                            buscarResponseDTO.color());

                model.addAttribute("buscarModel", buscarModel);
                return "detalles";

            } else {

                BuscarModel buscarModel = new BuscarModel("02", "Ingrese una placa correcta", "", "", 0, 0.0, "");
                model.addAttribute("buscarModel", buscarModel);
                return "inicio";

            }

        } catch (Exception e) {

            BuscarModel buscarModel = new BuscarModel("99", "Error: Ocurrió un problema en la autenticación", "", "", 0, 0.0, "");
            model.addAttribute("buscarModel", buscarModel);
            System.out.println(e.getMessage());
            return "inicio";

        }
    }
}
