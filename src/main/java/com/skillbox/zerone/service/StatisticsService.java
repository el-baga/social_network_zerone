package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.response.RegionStatisticsRs;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.repository.*;
import com.skillbox.zerone.specification.Specs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final PersonRepository personRepository;
    private final PersonService personService;
    private final CountryRepository countryRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CityRepository cityRepository;
    private final MessageRepository messageRepository;
    private final DialogService dialogService;
    private final DialogRepository dialogRepository;


    public Long getUserCount() {
        return personRepository.count();
    }

    public Long getUserCountByCountry(String country) {
        return personRepository.count(Specification.where(Specs.eq(Person_.country, country)));
    }

    public Long getUserCountByCity(String city) {
        return personRepository.count(Specification.where(Specs.eq(Person_.city, city)));
    }

    public Long getTagCount() {
        return tagRepository.count();
    }

    public Long getTagCountByPostId(Long postId) {
        return (long) postService.findById(postId).getTags().size();
    }

    public Long getPostCount() {
        return postRepository.count();
    }

    public Long getPostCountByUserId(Long userId) {
        return postRepository.count(Specification.where(Specs.eq(Post_.author, personRepository.getPersonById(userId))));
    }

    public Long getMessageCount() {
        return messageRepository.count();
    }

    public Long getMessageCountByDialogId(Long dialogId) {
        return messageRepository.count(Specification.where(Specs.eq(Message_.dialog, dialogService.getDialogById(dialogId))));
    }

    public Map<String, Long> getMessageByIdOfTwoPersons(Long firstUserId, Long secondUserId) {
        final Person firstPerson = personService.getPersonById(firstUserId);
        final Person secondPerson = personService.getPersonById(secondUserId);
        final Map<String, Long> result = new LinkedHashMap<>();
        final List<Message> firstUserMessages = messageRepository.findByAuthorAndRecipient(firstPerson, secondPerson)
                .stream().filter(message -> message.getAuthor().getId().equals(firstUserId)).toList();
        final List<Message> secondUserMessages = messageRepository.findByAuthorAndRecipient(secondPerson, firstPerson)
                .stream().filter(message -> message.getAuthor().getId().equals(secondUserId)).toList();
        result.put(firstPerson.getFirstName(), (long) firstUserMessages.size());
        result.put(secondPerson.getFirstName(), (long) secondUserMessages.size());
        return result;
    }

    public Long getLikeCount() {
        return likeRepository.count();
    }

    public Long getLikeCountByEntityId(Long commentId) {
        return likeRepository.count(Specification.where(Specs.eq(Like_.entityId, commentId)));
    }

    public Long getDialogCount() {
        return dialogRepository.count();
    }

    public Long getDialogCountByUserId(Long userId) {
        final Person person = personService.getPersonById(userId);
        return dialogRepository.count(Specification
                .where(Specs.eq(Dialog_.firstPerson, person))
                .or(Specs.eq(Dialog_.secondPerson, person)));
    }

    public Long getCountryCount() {
        return countryRepository.count();
    }

    public List<RegionStatisticsRs> getUsersCountInEachCountry() {
        long totalUsersCount = personRepository.count();
        List<RegionStatisticsRs> regionStatistic = new ArrayList<>(countryRepository.findAll().stream()
                .map(country -> {
                    final RegionStatisticsRs regionStatisticsRs = new RegionStatisticsRs();
                    regionStatisticsRs.setRegion(country.getName());
                    regionStatisticsRs.setCountUsers(getUserCountByCountry(country.getName()));
                    return regionStatisticsRs;
                })
                .filter(response -> response.getCountUsers() > 0 && response.getRegion() != null)
                .toList());
        regionStatistic.add(new RegionStatisticsRs("Не указан", totalUsersCount - regionStatistic.size()));
        return regionStatistic;
    }

    public Long getCommentCountByPostId(Long postId) {
        return commentRepository.count(Specification.where(Specs.eq(Comment_.postId, postId)));
    }

    public Long getCityCount() {
        return cityRepository.count();
    }

    public List<RegionStatisticsRs> getUsersCountInEachCity() {
        long totalUsersCount = personRepository.count();
        List<RegionStatisticsRs> countryStatistic = new ArrayList<>(cityRepository.findAll().stream()
                .map(city -> {
                    final RegionStatisticsRs regionStatisticsRs = new RegionStatisticsRs();
                    regionStatisticsRs.setRegion(city.getName());
                    regionStatisticsRs.setCountUsers(getUserCountByCity(city.getName()));
                    return regionStatisticsRs;
                })
                .filter(response -> response.getCountUsers() > 0 && response.getRegion() != null)
                .toList());
        countryStatistic.add(new RegionStatisticsRs("Не указан", totalUsersCount - countryStatistic.size()));
        return countryStatistic;
    }

    public Long getBlockedUserCount() {
        return personRepository.count(Specification.where(Specs.eq(Person_.isBlocked, true)));
    }

    public Long getSoftDeletedUserCount() {
        return personRepository.count(Specification.where(Specs.eq(Person_.isDeleted, true)));
    }
}
