package com.eclectics.io.kafkabroker.department;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/api/v1/department")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentService departmentService, DepartmentRepository departmentRepository) {
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Department department) {
        try {
            return ResponseEntity.ok().body(departmentService.create(department));
        } catch (Exception e) {
            log.info("Caught Error" + e);
            return null;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        try {
            return ResponseEntity.ok().body(departmentService.all());
        } catch (Exception e) {
            log.info("Caught Error" + e);
            return null;
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().body(departmentService.find(id));
        } catch (Exception e) {
            log.info("Caught Error" + e);
            return null;
        }
    }

    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Sort.Order> orders = new ArrayList<Sort.Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
//                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
//                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Department> tutorials = new ArrayList<Department>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Department> pageTuts;
            if (title == null)
                pageTuts = departmentRepository.findAll(pagingSort);
            else
                pageTuts = departmentRepository.findByDepartmentNameContaining(title, pagingSort);

            tutorials = pageTuts.getContent();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
