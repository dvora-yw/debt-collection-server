package com.debtcollection.service;

import com.debtcollection.dto.payment.PaymentCreateDto;
import com.debtcollection.dto.payment.PaymentDto;
import com.debtcollection.dto.payment.PaymentUpdateDto;
import com.debtcollection.dto.payment.PaymentViewDto;
import com.debtcollection.entity.EndClient;
import com.debtcollection.entity.Payment;
import com.debtcollection.mapper.PaymentMapper;
import com.debtcollection.repository.EndClientRepository;
import com.debtcollection.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EndClientRepository endClientRepository;
    private final PaymentMapper mapper;

    public PaymentService(
            PaymentRepository paymentRepository,
            EndClientRepository endClientRepository,
            PaymentMapper mapper
    ) {
        this.paymentRepository = paymentRepository;
        this.endClientRepository = endClientRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Transactional
    public PaymentDto create(PaymentCreateDto dto) {
        Payment payment = mapper.toEntity(dto);

        EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                .orElseThrow(() ->
                        new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                );

        payment.setEndClient(endClient);

        Payment saved = paymentRepository.save(payment);
        return mapper.toResponseDto(saved);
    }

    // UPDATE
    @Transactional
    public PaymentDto update(Long id, PaymentUpdateDto dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found with id " + id)
                );

        mapper.updateEntityFromDto(dto, payment);

        if (dto.getEndClientId() != null) {
            EndClient endClient = endClientRepository.findById(dto.getEndClientId())
                    .orElseThrow(() ->
                            new RuntimeException("EndClient not found with id " + dto.getEndClientId())
                    );
            payment.setEndClient(endClient);
        }

        return mapper.toResponseDto(payment);
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public PaymentDto getById(Long id) {
        return paymentRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found with id " + id)
                );
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id " + id);
        }
        paymentRepository.deleteById(id);
    }
    public List<PaymentViewDto> getPaymentsForView() {
        return paymentRepository.findAllForView();
    }
}