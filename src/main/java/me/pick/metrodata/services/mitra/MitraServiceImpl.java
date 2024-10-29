package me.pick.metrodata.services.mitra;

import lombok.RequiredArgsConstructor;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.models.entity.Mitra;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.repositories.MitraRepository;
import me.pick.metrodata.repositories.TalentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MitraServiceImpl implements MitraService{

    private final TalentRepository talentRepository;
    private final MitraRepository mitraRepository;

    public Page<Talent> getMitraTalents(Long mitraId, Integer page, Integer size) {
        Mitra mitra = mitraRepository.findById(mitraId).orElseThrow(() -> new MitraDoesNotExistException(mitraId));
        List<Talent> talents = talentRepository.findTalentByMitraId(mitraId);
        Pageable pageable =  PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), talents.size());

        return new PageImpl<>(talents.subList(start, end), pageable, talents.size());
    }

}
