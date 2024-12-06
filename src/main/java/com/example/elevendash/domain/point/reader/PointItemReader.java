package com.example.elevendash.domain.point.reader;

import com.example.elevendash.domain.point.entity.Point;
import com.example.elevendash.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class PointItemReader implements ItemReader<Point> {

    @Autowired
    private PointRepository pointRepository;


    @Override
    public Point read() throws Exception {
        // Return one expiring point at a time
        return pointRepository.findTopByExpirationDateBefore(LocalDateTime.now());
    }
}