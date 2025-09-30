package advanced.ipt_library.service.impl;

import advanced.ipt_library.entity.Archive;
import advanced.ipt_library.entity.ArchiveType;
import advanced.ipt_library.exception.BusinessException;
import advanced.ipt_library.exception.ErrorCodeConstant;
import advanced.ipt_library.exception.NotFoundException;
import advanced.ipt_library.repository.ArchiveRepository;
import advanced.ipt_library.request.CreateArchiveRequest;
import advanced.ipt_library.request.UpdateArchiveRequest;
import advanced.ipt_library.response.ArchiveResponse;
import advanced.ipt_library.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ModelMapper mapper;

    @Override
    public List<ArchiveResponse> findAll() {

        List<Archive> archives = archiveRepository.findAll();
        List<ArchiveResponse> archivesResponses = new ArrayList<>();

        for (Archive archive : archives) {
//            ArchiveResponse archiveResponse = mapper.map(archive, ArchiveResponse.class);
            archivesResponses.add(new ArchiveResponse(archive));
        }
        return archivesResponses;
    }

    @Override
    public void create(CreateArchiveRequest request) {
        Archive archive = new Archive();

        ArchiveType archiveType = ArchiveType.valueOf(request.getArchiveType());    // chuyển từ string -> enum

        archive.setArchiveType(archiveType);
        archive.setShelf(request.getShelf());
        archive.setLocation(request.getLocation());

        archiveRepository.save(archive);
    }

    @Override
    public void update(int id, UpdateArchiveRequest request) {

        Archive archive = archiveRepository.findById(id).orElseThrow(() -> new NotFoundException("Shelf does not exist", ErrorCodeConstant.no_existing_data));

        Archive archiveFoundByShelf = archiveRepository.findByShelfIgnoreCase(request.getShelf());
        if (archiveFoundByShelf != null && archiveFoundByShelf.getId() != id) {
            throw new BusinessException("Shelf already exists", ErrorCodeConstant.existing_data);
        }

        archive.setShelf(request.getShelf());
        archive.setLocation(request.getLocation());

        archiveRepository.save(archive);
    }

    @Override
    public void delete(int id) {
        Archive archive = archiveRepository.findById(id).orElseThrow(() -> new NotFoundException("Shelf does not exist", ErrorCodeConstant.no_existing_data));
        archiveRepository.delete(archive);
    }


}
