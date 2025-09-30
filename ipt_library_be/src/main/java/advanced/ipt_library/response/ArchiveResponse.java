package advanced.ipt_library.response;

import advanced.ipt_library.entity.Archive;
import advanced.ipt_library.entity.ArchiveType;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ArchiveResponse {
    private int id;

    private String archiveType;
    // có 3 loại EnumType.String

    private String shelf;

    public ArchiveResponse(Archive archive) {
        this.id = archive.getId();
        this.archiveType = archive.getArchiveType().name(); // cái name này để làm quái gì? trả về String?
        this.shelf = archive.getShelf();
    }
}
