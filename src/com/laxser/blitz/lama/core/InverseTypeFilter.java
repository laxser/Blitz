package com.laxser.blitz.lama.core;

import java.io.IOException;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
/**
 * 
 * @author laxser
 * @ contact qifan.du@renren-inc.com
 * date: 2012-3-22
 */
class InverseTypeFilter implements TypeFilter {

    TypeFilter filter;

    public InverseTypeFilter(TypeFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException {
        return !filter.match(metadataReader, metadataReaderFactory);
    }
}
