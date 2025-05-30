package th.co.priorsolution.training.restaurant.service;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.model.OrderExportDtoModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final OrderRepository orderRepository;

    public List<OrderExportDtoModel> getAllOrderExportDto() {
        List<OrderEntity> orders = orderRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return orders.stream()
                .flatMap(order -> order.getItems().stream().map(item -> {
                    OrderExportDtoModel dto = new OrderExportDtoModel();
                    dto.setOrderId(order.getId());
                    dto.setTableNumber(order.getTableNumber());
                    dto.setCreatedAt(order.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .withZoneSameInstant(ZoneId.of("Asia/Bangkok"))
                            .format(formatter));
                    dto.setStatus(order.getStatus().name());
                    dto.setMenuName(item.getMenuName());
                    dto.setPrice(item.getPrice());
                    return dto;
                }))
                .toList();
    }

    public void exportOrdersToCSV(OutputStream out) throws IOException {
        List<OrderExportDtoModel> dtos = getAllOrderExportDto();
        try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
            writer.write("Order ID,Table Number,Created At,Status,Menu Name,Price\n");

            for (OrderExportDtoModel dto : dtos) {
                writer.write(String.format("%d,%d,%s,%s,%s,%.2f\n",
                        dto.getOrderId(),
                        dto.getTableNumber(),
                        dto.getCreatedAt(),
                        dto.getStatus(),
                        dto.getMenuName(),
                        dto.getPrice()));
            }

            double total = dtos.stream().mapToDouble(OrderExportDtoModel::getPrice).sum();
            writer.write("\nTotal Revenue,,,,," + total + "\n");
        }
    }

    public void exportOrdersToExcel(OutputStream out) throws IOException {
        List<OrderExportDtoModel> dtos = getAllOrderExportDto();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Details");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Order ID");
        header.createCell(1).setCellValue("Table Number");
        header.createCell(2).setCellValue("Created At");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Menu Name");
        header.createCell(5).setCellValue("Price");

        int rowIdx = 1;
        for (OrderExportDtoModel dto : dtos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(dto.getOrderId());
            row.createCell(1).setCellValue(dto.getTableNumber());
            row.createCell(2).setCellValue(dto.getCreatedAt());
            row.createCell(3).setCellValue(dto.getStatus());
            row.createCell(4).setCellValue(dto.getMenuName());
            row.createCell(5).setCellValue(dto.getPrice());
        }

        double total = dtos.stream().mapToDouble(OrderExportDtoModel::getPrice).sum();
        Row totalRow = sheet.createRow(rowIdx + 1);
        totalRow.createCell(4).setCellValue("Total Revenue");
        totalRow.createCell(5).setCellValue(total);

        workbook.write(out);
        workbook.close();
    }

    public void exportOrdersToJasper(String jrxmlClasspath, OutputStream outputPdfPath) throws JRException {
        List<OrderExportDtoModel> dtos = getAllOrderExportDto();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dtos);

        double total = dtos.stream().mapToDouble(OrderExportDtoModel::getPrice).sum();
        Map<String, Object> params = new HashMap<>();
        params.put("createdBy", "ManagerService");
        params.put("totalRevenue", total);

        InputStream reportStream = getClass().getResourceAsStream(jrxmlClasspath);
        if (reportStream == null) {
            throw new RuntimeException("ไม่พบไฟล์ JRXML ที่: " + jrxmlClasspath);
        }

        JasperReport report = JasperCompileManager.compileReport(reportStream);
        JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
        JasperExportManager.exportReportToPdfStream(print, outputPdfPath);
    }

    public double calculateTotalRevenueForDate(LocalDate date) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().isEqual(date))
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(OrderItemEntity::getPrice)
                .sum();
    }

    public double calculateTotalRevenueForMonth(int year, int month) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().getYear() == year &&
                        order.getCreatedAt().getMonthValue() == month)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(OrderItemEntity::getPrice)
                .sum();
    }
}
