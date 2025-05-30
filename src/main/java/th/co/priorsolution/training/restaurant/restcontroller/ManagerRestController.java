package th.co.priorsolution.training.restaurant.restcontroller;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.service.ManagerService;

import java.io.IOException;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerRestController {

    private final ManagerService managerService;

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportCsv() throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(managerService.getCsvResource());
    }

    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportExcel() throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(managerService.getExcelResource());
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportPdf() throws JRException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(managerService.getPdfResource("/orders.jrxml"));
    }


}
