package me.pick.metrodata.services.talent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pick.metrodata.exceptions.mitra.MitraDoesNotExistException;
import me.pick.metrodata.exceptions.role.RoleDoesNotExistException;
import me.pick.metrodata.exceptions.talent.TalentAlreadyExistException;
import me.pick.metrodata.models.dto.requests.TalentFromVacancyRequest;
import me.pick.metrodata.models.entity.Account;
import me.pick.metrodata.models.entity.Applicant;
import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements  TalentService{
    private final TalentRepository talentRepository;
    private final MitraRepository mitraRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public TalentServiceImpl(TalentRepository talentRepository, MitraRepository mitraRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AccountRepository accountRepository) {
        this.talentRepository = talentRepository;
        this.mitraRepository = mitraRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Talent createViaVacancy(TalentFromVacancyRequest request) {
        Talent existing = talentRepository.findByNik(request.getTalentNik()).orElse(null);
        if (existing != null) {
            throw new TalentAlreadyExistException(request.getTalentName());
        }

        Account newTalentAccount = new Account();
        Applicant newTalentApplicant = new Applicant();
        User newTalentUser = new User();
        Talent newTalent = new Talent();

        newTalentAccount.setUsername(request.getTalentEmail().split("@")[0].replace(".", ""));
        newTalentAccount.setPassword(passwordEncoder.encode(request.getTalentNik()Nik()));
        newTalentAccount.setRole(roleRepository.findById(6L).orElseThrow(() -> new RoleDoesNotExistException(6L)));

        String[] nameParts = request.getTalentName().split(" ");

        newTalentUser.setFirstName(nameParts[0]);

        String lastName = "";

        if (nameParts.length > 1) {
            lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));
        }

        newTalentUser.setLastName(lastName);
        newTalentUser.setMitra(mitraRepository.findById(request.getTalentMitraId()).orElseThrow(() -> new MitraDoesNotExistException(request.getTalentMitraId())));
        userRepository.save(newTalentUser);

        newTalentAccount.setUser(newTalentUser);
        accountRepository.save(newTalentAccount);

        newTalent.setUser(newTalentUser);
        newTalent.setName(request.getTalentName());
        newTalent.setEmail(request.getTalentEmail());
        newTalent.setNik(request.getTalentNik());
        newTalent.setMitra(newTalentUser.getMitra());
        newTalent.setUser(newTalentUser);

        talentRepository.save(newTalent);

        return null;
    }
}
