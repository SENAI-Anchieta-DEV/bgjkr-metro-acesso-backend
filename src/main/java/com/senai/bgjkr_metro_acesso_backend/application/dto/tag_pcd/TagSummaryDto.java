package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;

public record TagSummaryDto(
        String id,
        String codigoTag
) {
    public static TagSummaryDto fromEntity(
            TagPcd tagPcd
    ) {
        return new TagSummaryDto(
                tagPcd.getId(),
                tagPcd.getCodigoTag()
        );
    }
}
