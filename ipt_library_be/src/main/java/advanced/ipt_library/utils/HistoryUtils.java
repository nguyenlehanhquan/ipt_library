package advanced.ipt_library.utils;

import advanced.ipt_library.entity.History;
import advanced.ipt_library.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryUtils {

    private final HistoryRepository historyRepository;

    public void addHistory(String object, String action, String dataBefore, String dataAfter, String description, String userLogin) {
        History newRecord = new History();
        newRecord.setObject(object);
        newRecord.setAction(action);
        newRecord.setDataBefore(dataBefore);
        newRecord.setDataAfter(dataAfter);
        newRecord.setDescription(description);
        newRecord.setUserLogin(userLogin);
        historyRepository.save(newRecord);
    }

}
