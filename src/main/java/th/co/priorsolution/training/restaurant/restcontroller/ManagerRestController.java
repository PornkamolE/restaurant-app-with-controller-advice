package th.co.priorsolution.training.restaurant.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.service.ManagerService;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerRestController {

    private final ManagerService managerService;

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportCsv() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            managerService.exportOrdersToCSV(out);
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportExcel() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            managerService.exportOrdersToExcel(out);
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportPdf() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            managerService.exportOrdersToJasper("/orders.jrxml", out);

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
