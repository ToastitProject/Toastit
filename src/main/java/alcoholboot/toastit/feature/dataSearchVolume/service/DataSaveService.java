package alcoholboot.toastit.feature.dataSearchVolume.service;

import alcoholboot.toastit.feature.dataSearchVolume.entity.CocktailDataEntity;
import alcoholboot.toastit.feature.dataSearchVolume.repository.DataSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataSaveService {
    private final DataSaveRepository dataSaveRepository;

    @Transactional
    public void save(CocktailDataEntity cocktailDataEntity) {
        dataSaveRepository.save(cocktailDataEntity);
    }

    public void saveAll(List<CocktailDataEntity> cocktailDataEntity){
        dataSaveRepository.saveAll(cocktailDataEntity);
    }
}
