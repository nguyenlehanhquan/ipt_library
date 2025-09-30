package advanced.ipt_library.service;

import advanced.ipt_library.request.CreateArchiveRequest;
import advanced.ipt_library.request.UpdateArchiveRequest;
import advanced.ipt_library.response.ArchiveResponse;

import java.util.List;

public interface ArchiveService {
    List<ArchiveResponse> findAll();
    void create(CreateArchiveRequest request);
    void update(int id, UpdateArchiveRequest request);
    void delete(int id);
}
