package com.joyfarm.file.services;

import com.joyfarm.file.constants.FileStatus;
import com.joyfarm.file.entities.FileInfo;
import com.joyfarm.file.repositories.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadDoneService {
    private final FileInfoRepository repository;
    private final FileInfoService infoService;

    public void process(String gid, String location) {

        List<FileInfo> items = infoService.getList(gid, location, FileStatus.ALL);
        items.forEach(i -> i.setDone(true));

        repository.saveAllAndFlush(items);
    }

    public void process(String gid) {
        process(gid, null);
    }
}