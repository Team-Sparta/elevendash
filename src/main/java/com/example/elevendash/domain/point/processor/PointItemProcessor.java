package com.example.elevendash.domain.point.processor;

import com.example.elevendash.domain.point.entity.Point;
import org.springframework.batch.item.ItemProcessor;

public class PointItemProcessor implements ItemProcessor<Point, Point> {

    @Override
    public Point process(Point item) throws Exception {
        // We just return the point since it's expired
        return item;
    }
}