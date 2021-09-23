package com.nnlk.z1zontodoserver.dto.user.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GithubUserResponseDto {

        private GithubUserResponseDto(){

        }
        String login; //실질적인 아이디
        String id;
        String nodeId;
        String avatarUrl;
        String gravatarId;
        String url;
        String htmlUrl;
        String followersUrl;
        String followingUrl;
        String gistsUrl;
        String starredUrl;
        String subscriptionsUrl;
        String organizationsUrl;
        String reposUrl;
        String eventsUrl;
        String receivedEventsUrl;
        String type;
        String siteAdmin;
        String name;
        String company;
        String blog;
        String location;
        String email;
        String hireable;
        String bio;
        String twitterUsername;
        String publicRepos;
        String publicGists;
        String followers;
        String following;
        String createdAt;
        String updatedAt;

}
