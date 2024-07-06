package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.request.PersonRq;
import com.skillbox.zerone.dto.response.GeolocationRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.entity.City;
import com.skillbox.zerone.entity.Country;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.GeolocationMapper;
import com.skillbox.zerone.repository.CityRepository;
import com.skillbox.zerone.repository.CountryRepository;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.util.CommonUtil;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import com.vk.api.sdk.queries.database.DatabaseGetCitiesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeolocationService {
    private final PersonRepository personRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    @Value("${vk.token}")
    private String token;
    @Value("${vk.id}")
    private Long vkId;

    public PageRs<List<GeolocationRs>> getCountries()  {
        List<GeolocationRs> geolocationRsList = countryRepository.findAll().stream()
                .map(GeolocationMapper.INSTANCE::toDto).collect(Collectors.toList());
        return PageRs.<List<GeolocationRs>>builder()
                .data(geolocationRsList)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total((long) geolocationRsList.size())
                .build();
    }

    public PageRs<GeolocationRs> getCitiesUses(String countryName) {
        GeolocationRs geolocationRs = null;
        Person currentUser = getPersonById(CommonUtil.getCurrentUserId());
        if(cityMatchesCountry(countryName, currentUser)){
            geolocationRs =  GeolocationRs.builder().title(currentUser.getCity()).build();
        }
        return PageRs.<GeolocationRs>builder()
                .data(geolocationRs)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total(1L)
                .build();
    }

    public PageRs<List<GeolocationRs>> getCitiesDb(String country, String startCity){
        List<GeolocationRs> geolocationRsList = getGeolocationRsListCitiesFromBd(country, startCity);
        return PageRs.<List<GeolocationRs>>builder()
                .data(geolocationRsList)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total((long) geolocationRsList.size())
                .build();
    }

    private List<GeolocationRs> getGeolocationRsListCitiesFromBd(String country, String startCity) {
        if (startCity == null || startCity.trim().equals("")) {
            return cityRepository.findCityByCountry(getCountryByName(country))
                    .stream().map(GeolocationMapper.INSTANCE::toDto).collect(Collectors.toList());
        }
        return cityRepository.findCityByCountryAndNameStartsWith(getCountryByName(country), startCity)
                .stream().map(GeolocationMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public PageRs<List<GeolocationRs>> getCitiesApi(String countryName, String startCity) throws ClientException, ApiException {
        int countryId = Math.toIntExact(getCountryByName(countryName).getExternalId());
        List<GeolocationRs> geolocationRsList = getGeolocationRsListCitiesFromVK(countryId, startCity);
        return PageRs.<List<GeolocationRs>>builder()
                .data(geolocationRsList)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total((long) geolocationRsList.size())
                .build();
    }

    private List<GeolocationRs> getGeolocationRsListCitiesFromVK(Integer countryId, String startCity)
            throws ClientException, ApiException {
        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vkClient = new VkApiClient(transportClient);
            UserActor actor = new UserActor(vkId, token);
            DatabaseGetCitiesQuery citiesQueries = new DatabaseGetCitiesQuery(vkClient, actor).countryId(countryId)
                                                                                              .q(startCity);
            List<com.vk.api.sdk.objects.database.City> cities = citiesQueries.execute().getItems();
            return cities.stream().map(GeolocationMapper.INSTANCE::toDto).collect(Collectors.toList());
        } catch (ApiException e) {
            throw new ApiException(e.getMessage());
        } catch (ClientException e) {
            throw new ClientException(e.getMessage());
        }
    }

    public void addCityToDb(PersonRq personRq) {
        if (cityRepository.findCityByName(personRq.getCity()).isEmpty() && personRq.getCountry() != null) {
            City city  = City.builder()
                    .name(personRq.getCity())
                    .country(getCountryByName(personRq.getCountry()))
                    .build();
            cityRepository.save(city);
        }
    }

    private Country getCountryByName(String countryName) {
        return countryRepository.findCountryByName(countryName)
                .orElseThrow(() -> new BadRequestException("No such Country with name: " + countryName));
    }

    public Person getPersonById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new BadRequestException("User with userName "
                + id + " not found"));
    }

    public  boolean cityMatchesCountry(String countryName, Person currentUser) {
        if (currentUser.getCity()!=null) {
            Optional<City> city = cityRepository.findCityByName(currentUser.getCity());
            if (city.isPresent()) {
                return city.get().getCountry().getName().equals(countryName);
            }
        }
        return false;
    }
}