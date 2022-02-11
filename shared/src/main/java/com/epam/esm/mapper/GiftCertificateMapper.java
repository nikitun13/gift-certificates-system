package com.epam.esm.mapper;

import com.epam.esm.dto.CreateTagDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateDtoWithoutTags;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UpdateGiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GiftCertificateMapper {

    @Autowired
    protected TagMapper tagMapper;

    public abstract GiftCertificateDto toGiftCertificateDto(GiftCertificate giftCertificate);

    public abstract GiftCertificateDtoWithoutTags toGiftCertificateDtoWithoutTags(GiftCertificate giftCertificate);

    @Mapping(target = "tags")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    public abstract GiftCertificate toGiftCertificate(UpdateGiftCertificateDto updateGiftCertificateDto);

    protected List<TagDto> toTagDtoList(List<Tag> tags) {
        return ObjectUtils.isEmpty(tags)
                ? new ArrayList<>()
                : tags.stream()
                .map(tagMapper::toTagDto)
                .toList();
    }

    protected List<Tag> toTagList(List<CreateTagDto> tags) {
        return ObjectUtils.isEmpty(tags)
                ? new ArrayList<>()
                : tags.stream()
                .map(tagMapper::toTag)
                .toList();
    }
}
