package com.skillbox.zerone.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.*;
import com.skillbox.zerone.dto.request.StorageRq;
import com.skillbox.zerone.dto.response.PageRs;

import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.entity.Storage;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.StorageMapper;
import com.skillbox.zerone.repository.StorageRepository;

import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${dropbox.folder}")
    private String storageControllerFolder;

    private final StorageRepository storageRepository;

    private final DbxClientV2 dbxClientV2;

    private final PersonService personService;

    private final StorageMapper storageMapper;

    @Transactional
    public PageRs<Storage> uploadImage(String type, MultipartFile file) {
        String fileName = storageControllerFolder + System.currentTimeMillis() + file.getOriginalFilename();
        Long id = CommonUtil.getCurrentUserId();

        try {
            saveImageToDropbox(file, fileName);
            Person person = personService.getPersonById(id);
            String photo = getImageLink(fileName);
            person.setPhoto(photo);
            personService.updatePerson(person);
            deleteImage(id);
        } catch (IOException | DbxException e) {
            throw new BadRequestException("Something went wrong with image processing.");
        }

        StorageRq storageRq = StorageRq.builder()
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileType(type)
                .ownerId(id)
                .build();
        return getStorageResponse(storageRq);
    }

    private void saveImageToDropbox(MultipartFile file, String path) throws IOException, DbxException {
        dbxClientV2.files().uploadBuilder(path).uploadAndFinish(file.getInputStream());
    }

    private String getImageLink(String path) throws DbxException {
        String link = dbxClientV2.sharing().createSharedLinkWithSettings(path,
                SharedLinkSettings.newBuilder()
                        .withAccess(RequestedLinkAccessLevel.MAX)
                        .withAudience(LinkAudience.PUBLIC)
                        .withRequestedVisibility(RequestedVisibility.PUBLIC)
                        .build()).getUrl();
        return link.substring(0, link.length() - 1).concat("1");
    }

    public void deleteImage(Long ownerId) throws DbxException {
        if (storageRepository.existsByOwnerId(ownerId)) {
            Storage storage = storageRepository.findByOwnerId(ownerId);
            String path = storage.getFileName();
            dbxClientV2.files().deleteV2(path);
            storageRepository.deleteByOwnerId(ownerId);
        }
    }

    private PageRs<Storage> getStorageResponse(StorageRq storageRq) {
        Storage storage = storageMapper.toEntity(storageRq);
        storageRepository.saveAndFlush(storage);
        return PageRs.<Storage>builder()
                .data(storage)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total(1L)
                .build();
    }
}


