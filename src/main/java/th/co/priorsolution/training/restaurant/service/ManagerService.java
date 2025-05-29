package th.co.priorsolution.training.restaurant.service;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final OrderRepository orderRepository;

    public void notify(OrderEntity orderEntity) {
        System.out.println("Manager notified with order summary for table " + orderEntity.getTableNumber());
    }

    public void exportOrdersToCSV(String filePath) throws IOException {
        List<OrderEntity> orders = orderRepository.findAll();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Order ID,Table Number,Created At,Status,Menu Name,Price\n");

            for (OrderEntity order : orders) {
                for (OrderItemEntity item : order.getItems()) {
                    writer.append(order.getId().toString()).append(",")
                            .append(String.valueOf(order.getTableNumber())).append(",")
                            .append(order.getCreatedAt()
                                    .atZone(ZoneId.systemDefault())
                                    .withZoneSameInstant(ZoneId.of("Asia/Bangkok"))
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append(",")
                            .append(order.getStatus().name()).append(",")
                            .append(item.getMenuName()).append(",")
                            .append(String.valueOf(item.getPrice())).append("\n");
                }
            }

            // รวมยอดขาย
            double total = orders.stream()
                    .flatMap(o -> o.getItems().stream())
                    .mapToDouble(OrderItemEntity::getPrice)
                    .sum();

            writer.append("\nTotal Revenue,,,,,,").append(String.valueOf(total)).append("\n");
        }
    }


    public void exportOrdersToExcel(OutputStream out) throws IOException {
        List<OrderEntity> orders = orderRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Details");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Order ID");
        header.createCell(1).setCellValue("Table Number");
        header.createCell(2).setCellValue("Created At");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Menu Name");
        header.createCell(5).setCellValue("Price");

        int rowIdx = 1;
        for (OrderEntity order : orders) {
            for (OrderItemEntity item : order.getItems()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getTableNumber());
                row.createCell(2).setCellValue(
                        order.getCreatedAt()
                                .atZone(ZoneId.systemDefault()) // แปลงจากเวลาเซิร์ฟเวอร์ (เช่น UTC)
                                .withZoneSameInstant(ZoneId.of("Asia/Bangkok")) // ✅ เป็นเวลาประเทศไทย
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                );

                row.createCell(3).setCellValue(order.getStatus().name());
                row.createCell(4).setCellValue(item.getMenuName());
                row.createCell(5).setCellValue(item.getPrice());
            }
        }

        // รวมยอดขาย
        double total = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(OrderItemEntity::getPrice)
                .sum();

        Row totalRow = sheet.createRow(rowIdx + 1);
        totalRow.createCell(4).setCellValue("Total Revenue");
        totalRow.createCell(5).setCellValue(total);

        workbook.write(out);
        workbook.close();
    }


    public void exportOrdersToJasper(String jrxmlClasspath,  OutputStream outputPdfPath) throws JRException {
        List<OrderEntity> orders = orderRepository.findAll();

        // flatten OrderItemEntity พร้อมข้อมูลจาก OrderEntity
        List<Map<String, Object>> data = orders.stream()
                .flatMap(order -> order.getItems().stream().map(item -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("orderId", order.getId());
                    row.put("tableNumber", order.getTableNumber());
                    row.put("createdAt",
                            order.getCreatedAt()
                                    .atZone(ZoneId.systemDefault())
                                    .withZoneSameInstant(ZoneId.of("Asia/Bangkok"))
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    );

                    row.put("status", order.getStatus().name());
                    row.put("menuName", item.getMenuName());
                    row.put("price", item.getPrice());
                    return row;
                }))
                .toList();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        double total = data.stream()
                .mapToDouble(row -> (double) row.get("price"))
                .sum();

        Map<String, Object> params = new HashMap<>();
        params.put("createdBy", "ManagerService");
        params.put("totalRevenue", total);

        InputStream reportStream = getClass().getResourceAsStream(jrxmlClasspath);
        if (reportStream == null) {
            try {
                throw new FileNotFoundException("ไม่พบไฟล์ JRXML ที่: " + jrxmlClasspath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        JasperReport report = JasperCompileManager.compileReport(reportStream);
        JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
        JasperExportManager.exportReportToPdfStream(print, outputPdfPath);

    }


    public double calculateTotalRevenueForDate(LocalDate date) {
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().isEqual(date))
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(OrderItemEntity::getPrice)
                .sum();
    }

    public double calculateTotalRevenueForMonth(int year, int month) {
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getCreatedAt().getYear() == year && order.getCreatedAt().getMonthValue() == month)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(OrderItemEntity::getPrice)
                .sum();
    }
}
