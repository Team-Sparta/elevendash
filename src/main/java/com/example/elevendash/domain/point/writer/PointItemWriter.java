package com.example.elevendash.domain.point.writer;

import com.example.elevendash.domain.point.entity.Point;
import com.example.elevendash.domain.point.repository.PointRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class PointItemWriter implements ItemWriter<Point> {

    @Autowired
    private PointRepository pointRepository;

    @Override
    public void write(Chunk<? extends Point> chunk) throws Exception {
        pointRepository.deleteAll(chunk);
    }
}