package com.sabora.server.Services.Implementation;

import com.sabora.server.DTOs.ExperienceDTO;
import com.sabora.server.Entities.Cliente;
import com.sabora.server.Entities.Experience;
import com.sabora.server.Entities.ExperienceSound;
import com.sabora.server.Entities.Sound;
import com.sabora.server.Repositories.*;
import com.sabora.server.Services.ExperienceServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExperienceServicesImplementation implements ExperienceServices {
    private static final Logger log = LoggerFactory.getLogger(ExperienceServicesImplementation.class);
    private final ExperienceRepository experienceRepository;
    private final ScenarioRepository scenarioRepository;
    private final SoundRepository soundRepository;
    private UserRepository userRepository;
    private FoodRepository foodRepository;
    private ExperienceSoundRepository experienceSoundRepository;

    public ExperienceServicesImplementation(ExperienceRepository experienceRepository, ScenarioRepository scenarioRepository, SoundRepository soundRepository, UserRepository userRepository, FoodRepository foodRepository, ExperienceSoundRepository experienceSoundRepository) {
        this.experienceRepository = experienceRepository;
        this.scenarioRepository = scenarioRepository;
        this.soundRepository = soundRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
        this.experienceSoundRepository = experienceSoundRepository;
    }


    @Override
    public ExperienceDTO addExperience(ExperienceDTO experienceDTO) {
        Experience experience = new Experience();
        experience.setClient((Cliente) userRepository.findByUsername(experienceDTO.getClient()));
        experience.setScenario(scenarioRepository.findByName(experienceDTO.getScenario()));
        experience.setSound(soundRepository.findByName(experienceDTO.getSound()));
        experience.setFood(foodRepository.findByName(experienceDTO.getFood()));
        experience.setUserId(experienceDTO.getUserId());
        experienceRepository.save(experience);

        List<Experience> savedExperience = experienceRepository.findByClientAndScenarioAndSoundAndFood(experience.getClient(), experience.getScenario(), experience.getSound(), experience.getFood());
        ExperienceDTO savedExperienceDTO = experienceDTO;
        savedExperienceDTO.setId(savedExperience.get(savedExperience.size() - 1).getId());;
        return savedExperienceDTO;
    }

    @Override
    public void endExperience(ExperienceDTO experienceDTO) {
        List<ExperienceSound> experienceSounds = new ArrayList<>();
        Experience experience = experienceRepository.findById(experienceDTO.getId());
        long time = experienceDTO.getTime();
        List<String> sounds = experienceDTO.getSounds();
        for (String sound : sounds) {
            Sound sound1 = soundRepository.findByName(sound);
            ExperienceSound experienceSound = new ExperienceSound();
            experienceSound.setExperience(experience);
            experienceSound.setSound(sound1);
            experienceSoundRepository.save(experienceSound);
            experienceSounds.add(experienceSound);
            log.info("Sound added to experience: {}", sound);
        }
        experience.setSounds(experienceSounds);
        log.info("Experience sounds added: {}", experience.getSounds());
        experience.setTime(time);
        experienceRepository.save(experience);
    }

    @Override
    public List<ExperienceDTO> getUnfinishedExperiences(String client) {
       List<ExperienceDTO> experiences = new ArrayList<>();
       Cliente cliente = (Cliente) userRepository.findByUsername(client);
       List<Experience> unfinishedExperiences = experienceRepository.findAll().stream().filter(experience -> experience.getClient().equals(cliente) && experience.getTime() == null).toList();
         for (Experience experience : unfinishedExperiences) {
             ExperienceDTO experienceDTO = getExperienceDTO(experience);
             experiences.add(experienceDTO);
         }
       return experiences;
    }

    private static ExperienceDTO getExperienceDTO(Experience experience) {
        ExperienceDTO experienceDTO = new ExperienceDTO();
        experienceDTO.setId(experience.getId());
        experienceDTO.setClient(experience.getClient().getUsername());
        experienceDTO.setScenario(experience.getScenario().getName());
        experienceDTO.setSound(experience.getSound().getName());
        experienceDTO.setFood(experience.getFood().getName());
        return experienceDTO;
    }
}
