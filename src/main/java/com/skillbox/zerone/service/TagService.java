package com.skillbox.zerone.service;

import com.skillbox.zerone.entity.Post;
import com.skillbox.zerone.entity.Tag;
import com.skillbox.zerone.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TagService {
    private static final String REGEX = "[^[\\p{L}\\d\\s]+]";
    private final TagRepository tagRepository;

    public List<Tag> saveAll(List<Tag> tags) {
        replaceTag(tags);
        final List<Tag> tagsToSave = new ArrayList<>();
        final List<Tag> alreadySavedTags = new ArrayList<>();
        tags.forEach(tag2check -> findByTag(tag2check.getTag())
                .ifPresentOrElse(
                        alreadySavedTags::add,
                        () -> {
                            final Tag newTag = new Tag();
                            newTag.setTag(tag2check.getTag());
                            tagsToSave.add(newTag);
                        }
                ));
        final Iterable<Tag> savedTagsIterable = tagRepository.saveAll(tagsToSave);
        final List<Tag> savedTags = new ArrayList<>();
        savedTagsIterable.forEach(savedTags::add);
        savedTags.addAll(alreadySavedTags);
        return savedTags;
    }

    public List<Tag> saveAll(List<String> tagStrings, Post post) {
        List<String> tagStringsList = replaceStringTag(tagStrings);
        final List<Tag> tagsToSave = new ArrayList<>();
        tagStringsList.forEach(tagString -> findByTag(tagString).ifPresentOrElse(tag -> {
                    tag.getPosts().add(post);
                    tagsToSave.add(tag);
                },
                () -> {
                    final Tag newTag = new Tag();
                    newTag.setTag(tagString);
                    newTag.setPosts(new ArrayList<>(Collections.singletonList(post)));
                    tagsToSave.add(newTag);
                }));
        final List<Tag> savedTags = new ArrayList<>();
        tagRepository.saveAll(tagsToSave).forEach(savedTags::add);
        return savedTags;
    }

    public void unlinkUnusedTagsFromPost(List<Tag> tags, List<String> tagStrings, Post post) {
        for (Tag tag : tags) {
            if (!tagStrings.contains(tag.getTag())) {
                tag.getPosts().remove(post);
                tagRepository.save(tag);
            }
        }
    }

    public Optional<Tag> findByTag(String tagString) {
        return tagRepository.findByTag(tagString);
    }

    public void replaceTag(List<Tag> tags) {
        if (tags.isEmpty()) {
            return;
        }
        for (Tag tag : tags) {
            tag.setTag(tag.getTag().trim().replaceAll(REGEX, "").replaceAll("\\s", "_"));
        }
    }

    public List<String> replaceStringTag(List<String> tagStrings) {
        List<String> list = new ArrayList<>();
        if (tagStrings == null) {
            return list;
        }
        for (String tag : tagStrings) {
            list.add(tag.trim().replaceAll(REGEX, "").replaceAll("\\s", "_"));
        }
        return list;
    }
}
